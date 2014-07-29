/*
 * 
 * 
 * 
 * 
		Copyright (C) 2014 Giulio Luzzati
		giulio.luzzati@edu.unige.it

    JSATSIM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSATSIM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSATSIM.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */

package dsp.netem.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.InternationalFormatter;

import dsp.netem.NETEMctrl;
import dsp.unige.figures.ChannelHelper;
import dsp.unige.figures.ChannelHelper.Satellite;
import dsp.unige.figures.ChannelHelper.Station;
import dsp.unige.figures.FEC;
import dsp.unige.figures.Modulation;
import dsp.unige.figures.Orbits;
import dsp.unige.figures.SimConstants;

public class Main extends JFrame {

	private static final long serialVersionUID = -5632906239127044315L;
	private static final int WIN_WIDTH = 800;
	private static final int WIN_HEIGHT = 600;
	private static final String VERSION_STRING = "<html>DSP Java Satellite Link Simulator<br><font color=\"FF5555\">Giulio Luzzati, 2014</font></html>";

	private JFormattedTextField txtEirptf;
	private JFormattedTextField txtTranspbw;
	private JFormattedTextField txtCarfreq;
	private JFormattedTextField latitude_tf;
	private JFormattedTextField txtAltitudetf;
	private JFormattedTextField txtElevtf;
	private JFormattedTextField txtNoisetemp, txtampliNoiseTemp,
			txtAntennaDiameter;

	private JFrame frmSatelliteEmulator;
	private JButton btnSet;
	private JSlider slider;
	private JComboBox orbitComboBox, modulationComboBox;
	JTextArea txtrDatamisc, txtrNetemoutput;
	private NETEMctrl netemController;
	private JLabel nicLabel;
	private JComboBox fecComboBox;

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initUI();
		setShortCut();
		try {
			chooseNIC();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}

	private void setShortCut() {
		KeyboardFocusManager kfm = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent k) {
				if (k.getID() == KeyEvent.KEY_PRESSED) {

					if (k.getKeyCode() == KeyEvent.VK_ENTER
							&& ((k.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK)) {
						setParameters();
						return true;
					}
					if (k.getKeyCode() == KeyEvent.VK_L
							&& ((k.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK)) {
						loadFile();
						return true;
					}
					if (k.getKeyCode() == KeyEvent.VK_S
							&& ((k.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK)) {
						saveFile();
						return true;
					}
					if (k.getKeyCode() == KeyEvent.VK_R
							&& ((k.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK)) {
						netemController.reset();
						return true;
					}
				}
				return false;

			}

		});
	}

	private void initUI() {

		Locale.setDefault(Locale.US);

		frmSatelliteEmulator = new JFrame("JSpinner Sample");

		BorderLayout borderLayout = (BorderLayout) frmSatelliteEmulator
				.getContentPane().getLayout();
		borderLayout.setVgap(5);
		borderLayout.setHgap(5);
		frmSatelliteEmulator.setResizable(false);

		frmSatelliteEmulator.setTitle("Satellite Emulator");
		frmSatelliteEmulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel sat_panel = new JPanel();
		sat_panel.setBorder(new CompoundBorder(new EmptyBorder(5, 10, 5, 5),
				new TitledBorder(new LineBorder(new Color(100, 149, 237), 3,
						true), "Satellite", TitledBorder.TRAILING,
						TitledBorder.TOP, null, new Color(100, 149, 237))));
		frmSatelliteEmulator.getContentPane().add(sat_panel, BorderLayout.WEST);
		GridBagLayout gbl_sat_panel = new GridBagLayout();
		gbl_sat_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 0.0 };
		gbl_sat_panel.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0 };
		gbl_sat_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_sat_panel.columnWidths = new int[] { 0 };
		sat_panel.setLayout(gbl_sat_panel);

		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 0;
		sat_panel.add(verticalStrut, gbc_verticalStrut);

		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_5 = new GridBagConstraints();
		gbc_horizontalStrut_5.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_5.gridx = 3;
		gbc_horizontalStrut_5.gridy = 1;
		sat_panel.add(horizontalStrut_5, gbc_horizontalStrut_5);

		JLabel lblEirp = new JLabel("EIRP [dBW]");
		GridBagConstraints gbc_lblEirp = new GridBagConstraints();
		gbc_lblEirp.anchor = GridBagConstraints.EAST;
		gbc_lblEirp.insets = new Insets(0, 0, 5, 5);
		gbc_lblEirp.gridx = 1;
		gbc_lblEirp.gridy = 1;
		sat_panel.add(lblEirp, gbc_lblEirp);

		txtEirptf = new JFormattedTextField(
				NumberFormat.getNumberInstance(Locale.US));
		txtEirptf.setText("45");
		GridBagConstraints gbc_txtEirptf = new GridBagConstraints();
		gbc_txtEirptf.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEirptf.insets = new Insets(0, 0, 5, 5);
		gbc_txtEirptf.gridx = 2;
		gbc_txtEirptf.gridy = 1;
		sat_panel.add(txtEirptf, gbc_txtEirptf);
		txtEirptf.setColumns(10);

		JLabel lblFec = new JLabel("FEC");
		GridBagConstraints gbc_lblFec = new GridBagConstraints();
		gbc_lblFec.anchor = GridBagConstraints.EAST;
		gbc_lblFec.insets = new Insets(0, 0, 5, 5);
		gbc_lblFec.gridx = 1;
		gbc_lblFec.gridy = 5;
		sat_panel.add(lblFec, gbc_lblFec);

		String[] fecs = new String[FEC.fecs];
		for (int i = 0; i < fecs.length; i++) {
			fecs[i] = FEC.getHRname(i);
		}

		fecComboBox = new JComboBox(fecs);
		GridBagConstraints gbc_fecdropdown = new GridBagConstraints();
		gbc_fecdropdown.insets = new Insets(0, 0, 5, 5);
		gbc_fecdropdown.fill = GridBagConstraints.HORIZONTAL;
		gbc_fecdropdown.gridx = 2;
		gbc_fecdropdown.gridy = 5;
		sat_panel.add(fecComboBox, gbc_fecdropdown);

		Component verticalStrut_4 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_4 = new GridBagConstraints();
		gbc_verticalStrut_4.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_4.gridx = 1;
		gbc_verticalStrut_4.gridy = 6;
		sat_panel.add(verticalStrut_4, gbc_verticalStrut_4);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 7;
		sat_panel.add(horizontalStrut, gbc_horizontalStrut);

		JLabel lblTransponderBandwidth = new JLabel("Transp. BW [KHz]");
		GridBagConstraints gbc_lblTransponderBandwidth = new GridBagConstraints();
		gbc_lblTransponderBandwidth.anchor = GridBagConstraints.EAST;
		gbc_lblTransponderBandwidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblTransponderBandwidth.gridx = 1;
		gbc_lblTransponderBandwidth.gridy = 2;
		sat_panel.add(lblTransponderBandwidth, gbc_lblTransponderBandwidth);

		txtTranspbw = new JFormattedTextField(new InternationalFormatter());
		txtTranspbw.setText("600000");
		GridBagConstraints gbc_txtTranspbw = new GridBagConstraints();
		gbc_txtTranspbw.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTranspbw.insets = new Insets(0, 0, 5, 5);
		gbc_txtTranspbw.gridx = 2;
		gbc_txtTranspbw.gridy = 2;
		sat_panel.add(txtTranspbw, gbc_txtTranspbw);
		txtTranspbw.setColumns(10);

		JLabel lblOrbitType = new JLabel("Orbit Type");
		GridBagConstraints gbc_lblOrbitType = new GridBagConstraints();
		gbc_lblOrbitType.anchor = GridBagConstraints.EAST;
		gbc_lblOrbitType.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrbitType.gridx = 1;
		gbc_lblOrbitType.gridy = 3;
		sat_panel.add(lblOrbitType, gbc_lblOrbitType);

		String[] orbitsStrings = { Orbits.getOrbitName(0),
				Orbits.getOrbitName(1), Orbits.getOrbitName(2),
				Orbits.getOrbitName(3), };
		orbitComboBox = new JComboBox(orbitsStrings);

		GridBagConstraints gbc_orbitComboBox = new GridBagConstraints();
		gbc_orbitComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_orbitComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_orbitComboBox.gridx = 2;
		gbc_orbitComboBox.gridy = 3;
		sat_panel.add(orbitComboBox, gbc_orbitComboBox);

		JLabel lblModulation = new JLabel("Modulation");
		GridBagConstraints gbc_lblModulation = new GridBagConstraints();
		gbc_lblModulation.anchor = GridBagConstraints.EAST;
		gbc_lblModulation.insets = new Insets(0, 0, 5, 5);
		gbc_lblModulation.gridx = 1;
		gbc_lblModulation.gridy = 4;
		sat_panel.add(lblModulation, gbc_lblModulation);

		String[] modulation = new String[Modulation.mods];
		for (int i = 0; i < modulation.length; i++) {
			modulation[i] = Modulation.getHRname(i);
		}

		modulationComboBox = new JComboBox(modulation);
		GridBagConstraints gbc_modulationComboBox = new GridBagConstraints();
		gbc_modulationComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_modulationComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_modulationComboBox.gridx = 2;
		gbc_modulationComboBox.gridy = 4;
		sat_panel.add(modulationComboBox, gbc_modulationComboBox);

		JPanel sta_panel = new JPanel();
		sta_panel.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 10),
				new TitledBorder(new LineBorder(new Color(143, 188, 143), 3,
						true), "Earth Station", TitledBorder.LEADING,
						TitledBorder.TOP, null, new Color(143, 188, 143))));
		frmSatelliteEmulator.getContentPane().add(sta_panel, BorderLayout.EAST);
		GridBagLayout gbl_sta_panel = new GridBagLayout();
		gbl_sta_panel.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0 };
		gbl_sta_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		sta_panel.setLayout(gbl_sta_panel);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 1;
		gbc_verticalStrut_1.gridy = 0;
		sta_panel.add(verticalStrut_1, gbc_verticalStrut_1);

		JLabel lblLatitude = new JLabel("Latitude [deg]");
		GridBagConstraints gbc_lblLatitude = new GridBagConstraints();
		gbc_lblLatitude.anchor = GridBagConstraints.EAST;
		gbc_lblLatitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblLatitude.gridx = 1;
		gbc_lblLatitude.gridy = 1;
		sta_panel.add(lblLatitude, gbc_lblLatitude);

		latitude_tf = new JFormattedTextField(NumberFormat.getNumberInstance());
		latitude_tf.setText("45");
		GridBagConstraints gbc_latitude_tf = new GridBagConstraints();
		gbc_latitude_tf.fill = GridBagConstraints.HORIZONTAL;
		gbc_latitude_tf.insets = new Insets(0, 0, 5, 5);
		gbc_latitude_tf.gridx = 2;
		gbc_latitude_tf.gridy = 1;
		sta_panel.add(latitude_tf, gbc_latitude_tf);
		latitude_tf.setColumns(10);

		JLabel lblAltitude = new JLabel("Altitude [km]");
		GridBagConstraints gbc_lblAltitude = new GridBagConstraints();
		gbc_lblAltitude.anchor = GridBagConstraints.EAST;
		gbc_lblAltitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblAltitude.gridx = 1;
		gbc_lblAltitude.gridy = 2;
		sta_panel.add(lblAltitude, gbc_lblAltitude);

		txtAltitudetf = new JFormattedTextField(
				NumberFormat.getNumberInstance());
		txtAltitudetf.setText("0");
		GridBagConstraints gbc_txtAltitudetf = new GridBagConstraints();
		gbc_txtAltitudetf.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAltitudetf.insets = new Insets(0, 0, 5, 5);
		gbc_txtAltitudetf.gridx = 2;
		gbc_txtAltitudetf.gridy = 2;
		sta_panel.add(txtAltitudetf, gbc_txtAltitudetf);
		txtAltitudetf.setColumns(10);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 3;
		gbc_horizontalStrut_1.gridy = 3;
		sta_panel.add(horizontalStrut_1, gbc_horizontalStrut_1);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
		gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_2.gridx = 0;
		gbc_horizontalStrut_2.gridy = 2;
		sta_panel.add(horizontalStrut_2, gbc_horizontalStrut_2);

		JLabel lblPolarization = new JLabel("Polarization");
		GridBagConstraints gbc_lblPolarization = new GridBagConstraints();
		gbc_lblPolarization.anchor = GridBagConstraints.EAST;
		gbc_lblPolarization.insets = new Insets(0, 0, 5, 5);
		gbc_lblPolarization.gridx = 1;
		gbc_lblPolarization.gridy = 3;
		sta_panel.add(lblPolarization, gbc_lblPolarization);

		JLabel lblCircular = new JLabel("Circular");
		lblCircular.setEnabled(false);
		lblCircular.setForeground(Color.BLACK);
		GridBagConstraints gbc_lblCircular = new GridBagConstraints();
		gbc_lblCircular.insets = new Insets(0, 0, 5, 5);
		gbc_lblCircular.gridx = 2;
		gbc_lblCircular.gridy = 3;
		sta_panel.add(lblCircular, gbc_lblCircular);

		JLabel lblElevationAngle = new JLabel("Elevation Angle [deg]");
		GridBagConstraints gbc_lblElevationAngle = new GridBagConstraints();
		gbc_lblElevationAngle.anchor = GridBagConstraints.EAST;
		gbc_lblElevationAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblElevationAngle.gridx = 1;
		gbc_lblElevationAngle.gridy = 4;
		sta_panel.add(lblElevationAngle, gbc_lblElevationAngle);

		txtElevtf = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtElevtf.setText("47.1");
		GridBagConstraints gbc_txtElevtf = new GridBagConstraints();
		gbc_txtElevtf.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtElevtf.insets = new Insets(0, 0, 5, 5);
		gbc_txtElevtf.gridx = 2;
		gbc_txtElevtf.gridy = 4;
		sta_panel.add(txtElevtf, gbc_txtElevtf);
		txtElevtf.setColumns(10);

		JLabel lblRainfallRate = new JLabel("Rainfall Rate [mm/h]");
		GridBagConstraints gbc_lblRainfallRate = new GridBagConstraints();
		gbc_lblRainfallRate.anchor = GridBagConstraints.EAST;
		gbc_lblRainfallRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblRainfallRate.gridx = 1;
		gbc_lblRainfallRate.gridy = 5;
		sta_panel.add(lblRainfallRate, gbc_lblRainfallRate);

		slider = new JSlider();
		slider.setMaximum(SimConstants.MAXRAINFALLRATE);
		slider.setMinimum(0);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5);
		// slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setToolTipText(slider.getValue() + "");
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider sli = (JSlider) arg0.getSource();
				sli.setToolTipText(sli.getValue() + "");
			}
		});

		slider.setPaintTicks(true);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 2;
		gbc_slider.gridy = 5;
		sta_panel.add(slider, gbc_slider);

		JLabel lblCarrierFrequency = new JLabel("Carrier Frequency [GHz]");
		GridBagConstraints gbc_lblCarrierFrequency = new GridBagConstraints();
		gbc_lblCarrierFrequency.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarrierFrequency.gridx = 1;
		gbc_lblCarrierFrequency.gridy = 6;
		sta_panel.add(lblCarrierFrequency, gbc_lblCarrierFrequency);

		txtCarfreq = new JFormattedTextField(NumberFormat.getNumberInstance());
		GridBagConstraints gbc_txtCarfreq = new GridBagConstraints();
		gbc_txtCarfreq.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCarfreq.insets = new Insets(0, 0, 5, 5);
		gbc_txtCarfreq.gridx = 2;
		gbc_txtCarfreq.gridy = 6;
		sta_panel.add(txtCarfreq, gbc_txtCarfreq);
		txtCarfreq.setText("19.7");
		txtCarfreq.setColumns(10);

		JLabel lblNoiseTemperature = new JLabel("Antenna Noise Temp.[°K]");
		GridBagConstraints gbc_lblNoiseTemperature = new GridBagConstraints();
		gbc_lblNoiseTemperature.insets = new Insets(0, 0, 5, 5);
		gbc_lblNoiseTemperature.gridx = 1;
		gbc_lblNoiseTemperature.gridy = 7;
		sta_panel.add(lblNoiseTemperature, gbc_lblNoiseTemperature);

		txtNoisetemp = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtNoisetemp.setText("60");
		GridBagConstraints gbc_txtNoisetemp = new GridBagConstraints();
		gbc_txtNoisetemp.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNoisetemp.insets = new Insets(0, 0, 5, 5);
		gbc_txtNoisetemp.gridx = 2;
		gbc_txtNoisetemp.gridy = 7;
		sta_panel.add(txtNoisetemp, gbc_txtNoisetemp);

		JLabel lblAmplifierNoiseTempk = new JLabel("Amplifier Noise Temp.[°K]");
		GridBagConstraints gbc_lblAmplifierNoiseTempk = new GridBagConstraints();
		gbc_lblAmplifierNoiseTempk.anchor = GridBagConstraints.EAST;
		gbc_lblAmplifierNoiseTempk.insets = new Insets(0, 0, 5, 5);
		gbc_lblAmplifierNoiseTempk.gridx = 1;
		gbc_lblAmplifierNoiseTempk.gridy = 8;
		sta_panel.add(lblAmplifierNoiseTempk, gbc_lblAmplifierNoiseTempk);

		txtampliNoiseTemp = new JFormattedTextField(
				NumberFormat.getNumberInstance());
		txtampliNoiseTemp.setText("100");
		GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
		gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
		gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextField.gridx = 2;
		gbc_formattedTextField.gridy = 8;
		sta_panel.add(txtampliNoiseTemp, gbc_formattedTextField);

		JLabel lblAntennaEffectiveAperture = new JLabel("Antenna Diameter [m]");
		GridBagConstraints gbc_lblAntennaEffectiveAperture = new GridBagConstraints();
		gbc_lblAntennaEffectiveAperture.anchor = GridBagConstraints.EAST;
		gbc_lblAntennaEffectiveAperture.insets = new Insets(0, 0, 5, 5);
		gbc_lblAntennaEffectiveAperture.gridx = 1;
		gbc_lblAntennaEffectiveAperture.gridy = 9;
		sta_panel.add(lblAntennaEffectiveAperture,
				gbc_lblAntennaEffectiveAperture);

		txtAntennaDiameter = new JFormattedTextField(
				NumberFormat.getNumberInstance(Locale.US));
		txtAntennaDiameter.setText("1");
		GridBagConstraints gbc_formattedTextField1 = new GridBagConstraints();
		gbc_formattedTextField1.insets = new Insets(0, 0, 5, 5);
		gbc_formattedTextField1.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextField1.gridx = 2;
		gbc_formattedTextField1.gridy = 9;
		sta_panel.add(txtAntennaDiameter, gbc_formattedTextField1);

		Component verticalStrut_3 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_3 = new GridBagConstraints();
		gbc_verticalStrut_3.gridy = 10;
		gbc_verticalStrut_3.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_3.gridx = 2;
		sta_panel.add(verticalStrut_3, gbc_verticalStrut_3);

		JPanel status_panel = new JPanel();
		frmSatelliteEmulator.getContentPane().add(status_panel,
				BorderLayout.SOUTH);
		GridBagLayout gbl_status_panel = new GridBagLayout();
		gbl_status_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_status_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_status_panel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_status_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 1.0, Double.MIN_VALUE };
		status_panel.setLayout(gbl_status_panel);

		Component verticalStrut_2 = Box.createVerticalStrut(5);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_2.gridx = 1;
		gbc_verticalStrut_2.gridy = 0;
		status_panel.add(verticalStrut_2, gbc_verticalStrut_2);

		btnSet = new JButton("SET");
		btnSet.setFont(new Font("DejaVu Sans", Font.BOLD, 15));
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setParameters();
			}
		});

		GridBagConstraints gbc_btnSet = new GridBagConstraints();
		gbc_btnSet.insets = new Insets(0, 0, 5, 5);
		gbc_btnSet.gridx = 1;
		gbc_btnSet.gridy = 1;
		status_panel.add(btnSet, gbc_btnSet);

		Component verticalStrut_7 = Box.createVerticalStrut(5);
		GridBagConstraints gbc_verticalStrut_7 = new GridBagConstraints();
		gbc_verticalStrut_7.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_7.gridx = 1;
		gbc_verticalStrut_7.gridy = 2;
		status_panel.add(verticalStrut_7, gbc_verticalStrut_7);

		txtrDatamisc = new JTextArea(8, 10);
		txtrDatamisc.setForeground(Color.YELLOW);
		txtrDatamisc.setBackground(Color.DARK_GRAY);
		txtrDatamisc.setFont(new Font("DejaVu Sans", Font.PLAIN, 9));
		txtrDatamisc.setTabSize(5);
		txtrDatamisc.setWrapStyleWord(true);
		txtrDatamisc.setEditable(false);
		txtrDatamisc.setText("please apply settings to continue..");
		GridBagConstraints gbc_txtrDatamisc = new GridBagConstraints();
		gbc_txtrDatamisc.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtrDatamisc.insets = new Insets(5, 15, 5, 15);
		gbc_txtrDatamisc.gridx = 1;
		gbc_txtrDatamisc.gridy = 3;
		status_panel.add(txtrDatamisc, gbc_txtrDatamisc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new CompoundBorder(new TitledBorder(null,
				"Netem Output", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)), null));
		scrollPane.setEnabled(false);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		status_panel.add(scrollPane, gbc_scrollPane);

		txtrNetemoutput = new JTextArea(8, 10);
		txtrNetemoutput.setBackground(Color.DARK_GRAY);
		txtrNetemoutput.setForeground(new Color(100, 149, 237));
		txtrNetemoutput.setFont(new Font("DejaVu Sans", Font.PLAIN, 9));
		scrollPane.setViewportView(txtrNetemoutput);
		txtrNetemoutput.setLineWrap(true);
		txtrNetemoutput.setEditable(false);
		txtrNetemoutput.setWrapStyleWord(true);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null,
				null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		status_panel.add(panel, gbc_panel);

		nicLabel = new JLabel("Working on null interface");
		nicLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// nicLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		nicLabel.setAlignmentX(1.0f);
		panel.add(nicLabel);

		frmSatelliteEmulator.pack();

		JMenuBar menuBar = new JMenuBar();
		frmSatelliteEmulator.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem saveConf = new JMenuItem("Save configuration [CTRL + S]");
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		saveConf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});

		JMenuItem loadConf = new JMenuItem("Load configuration [CTRL + L]");

		loadConf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadFile();
			}
		});

		JMenuItem setNICitem = new JMenuItem("Set Network Interface...");
		setNICitem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					chooseNIC();
				} catch (SocketException e) {
					e.printStackTrace();
				}
			}
		});

		JMenuItem resetNETEM = new JMenuItem("Reset netem [CTRL + R]");
		resetNETEM.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				netemController.reset();
			}
		});

		mnFile.add(setNICitem);
		mnFile.add(resetNETEM);

		mnFile.add(saveConf);
		mnFile.add(loadConf);
		mnFile.add(exit);

		JMenu mnHelp = new JMenu("?");
		JMenuItem about = new JMenuItem("About...");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAbout();
			}
		});
		menuBar.add(mnHelp);
		mnHelp.add(about);

		frmSatelliteEmulator.setVisible(true);
		netemController = new NETEMctrl("NULL");

	}

	protected void saveFile() {
		try {

			Station sta = parseStationParameters();
			Satellite sat = parseSatelliteParameters();
			JFileChooser fc = new JFileChooser();

			int ret = fc.showOpenDialog(frmSatelliteEmulator);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File saveTo = fc.getSelectedFile();
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(saveTo));
				oos.writeObject(sta);
				oos.writeObject(sat);
				oos.close();
				showFileSaved(saveTo);
			}
		} catch (Exception e1) {
			showFileSaveError(e1);
		}
	}

	protected void loadFile() {
		try {

			Station sta;
			Satellite sat;
			JFileChooser fc = new JFileChooser();

			int ret = fc.showOpenDialog(frmSatelliteEmulator);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File loadFrom = fc.getSelectedFile();
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(loadFrom));
				sta = (Station) ois.readObject();
				sat = (Satellite) ois.readObject();
				ois.close();
				fillFields(sta, sat);
				showFileLoaded();
			}
		} catch (Exception e1) {
			showFileLoadError(e1);
		}
	}

	private void showFileLoadError(Exception e) {
		String error;
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		error = errors.toString();
		JOptionPane.showMessageDialog(frmSatelliteEmulator,
				"Error loading file:\n" + error);
	}

	private void showFileSaveError(Exception e) {
		String error;
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		error = errors.toString();
		JOptionPane.showMessageDialog(frmSatelliteEmulator,
				"Error saving file:\n" + error);
	}

	private void showFileSaved(File f) {
		JOptionPane.showMessageDialog(frmSatelliteEmulator, "File saved to "
				+ f.getAbsolutePath());
	}

	private void showFileLoaded() {
		// JOptionPane.showMessageDialog(frmSatelliteEmulator, "File loaded");
	}

	protected void chooseNIC() throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface
				.getNetworkInterfaces();
		ArrayList<String> nics = new ArrayList<String>();

		for (NetworkInterface netint : Collections.list(nets)) {
			nics.add(netint.getDisplayName());
		}

		String[] choices = new String[nics.size()];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = nics.get(i);
		}

		String nic = (String) JOptionPane.showInputDialog(frmSatelliteEmulator,
				"", "Choose NIC", JOptionPane.PLAIN_MESSAGE, null, choices,
				choices[0]);
		nicLabel.setText("working on " + nic + " interface");
		netemController = new NETEMctrl(nic);
		netemController.reset();
	}

	protected void showAbout() {

		JFrame splashAbout = new JFrame("About");
		int H = 200, W = 400;
		splashAbout.setSize(400, 200);
		splashAbout.getContentPane().setLayout(new FlowLayout());
		splashAbout.setLocation(frmSatelliteEmulator.getWidth() / 2 - W / 2,
				frmSatelliteEmulator.getHeight() / 2 - H / 2);
		JLabel pic = new JLabel(new ImageIcon(getClass()
				.getResource("logo.png")));
		splashAbout.getContentPane().add(pic);
		JLabel text = new JLabel(VERSION_STRING);
		splashAbout.getContentPane().add(text);
		splashAbout.setVisible(true);

	}

	protected void fillFields(Station sta, Satellite sat) {
		latitude_tf.setText(String.format("%2.2f", sta.stationLatitude));
		txtAltitudetf.setText(String.format("%2.4f", sta.stationAltitude));
		txtElevtf.setText(String.format("%2.2f", sta.elevationAngle));
		txtCarfreq.setText(String.format("%3.2f", sta.frequency));
		txtNoisetemp.setText(sta.antennaNoiseTemperature + "");
		txtampliNoiseTemp.setText(sta.amplifierNoiseTemperature + "");

		txtEirptf.setText(String.format("%2.2f", sat.EIRP));
		txtTranspbw.setText(sat.transponderBandwidth + "");
		orbitComboBox.setSelectedIndex(sat.ORBIT_TYPE);
		modulationComboBox.setSelectedIndex(sat.modulation);
		slider.setValue((int) sta.R001);

	}

	protected void setParameters() {
		ChannelHelper.Station sta = parseStationParameters();
		ChannelHelper.Satellite sat = parseSatelliteParameters();

		int rate = ChannelHelper.getInfoRate(sta, sat);
		double ber = ChannelHelper.getBER(sta, sat, rate);
		StringBuffer sb = netemController.setNetworkConditions("" + rate,
				String.format("%e", ber * 100), 0 + "",
				Orbits.getDelay(sat.ORBIT_TYPE) + "");

		setMiscData(sat, sta, ChannelHelper.getRate(sta, sat), ber);
		txtrNetemoutput.append("\n" + sb.toString());
	}

	private void setMiscData(Satellite sat, Station sta, int rate, double ber) {

		double SdBW, NdBW, Eb, N0, EbN0;
		SdBW = ChannelHelper.getSdBW(sta, sat);
		NdBW = ChannelHelper.getNdBW(sta, sat);
		Eb = 10 * Math.log10(Math.pow(10, SdBW / 10d) / (rate * 1000d));
		N0 = ChannelHelper.getN0dBW(sta, sat);
		EbN0 = Eb - N0;

		// codedBER = FEC.getBlockCodePE(EbN0, 255, 187, 9);

		String st = "Total Attenuation = "
				+ twoDec(ChannelHelper.getFreeSpaceLoss(sta, sat)
						+ ChannelHelper.getRainAttenuation(sta)) + " dB  ( "
				+ twoDec(ChannelHelper.getFreeSpaceLoss(sta, sat))
				+ " dB (FSL) + "
				+ twoDec(ChannelHelper.getRainAttenuation(sta))
				+ " dB (Rain)  )";
		st += "\nAntenna Gain = " + twoDec(sta.getAntennaGain())
				+ " dB, Figure of Merit =" + twoDec(sta.getFigureofMerit())
				+ " dB";
		st += "\nC at receiver = " + twoDec(SdBW - sta.getFigureofMerit() + 30)
				+ " dBm";
		st += "\nC = " + twoDec(SdBW + 30) + " dBm, N = " + twoDec(NdBW + 30)
				+ " dBm";
		st += "\nCNR = " + twoDec(SdBW - NdBW) + " dB";
		st += "\nShannon limit = "
				+ twoDec(ChannelHelper.getHSCapacity(sat.transponderBandwidth,
						SdBW, NdBW)) + " kbps\nBitrate = " + twoDec(rate)
				+ " kbps, Information Rate = "
				+ twoDec(ChannelHelper.getInfoRate(sta, sat)) + " kbps";
		st += "\nEb/n0 = " + twoDec(EbN0) + ", uncoded BER = "
				+ ChannelHelper.getUncodedBER(sta, sat, rate)
				+ ", coded BER = " + ber;

		txtrDatamisc.setText(st);
	}

	private static String twoDec(double in) {
		return String.format("%6.2f", in);
	}

	private Satellite parseSatelliteParameters() {

		double EIRP;
		int orbType, modulation, transpBW, fec;

		EIRP = Double.parseDouble(txtEirptf.getText());
		transpBW = Integer.parseInt(txtTranspbw.getText());
		orbType = orbitComboBox.getSelectedIndex();
		modulation = modulationComboBox.getSelectedIndex();
		fec = fecComboBox.getSelectedIndex();

		Satellite sat = new Satellite(EIRP, transpBW, orbType, modulation, fec);
		return sat;
	}

	private Station parseStationParameters() {

		double latitude, altitude, elAngle, rainfallRate, carFreq;
		latitude = Double.parseDouble(latitude_tf.getText());
		altitude = Double.parseDouble(txtAltitudetf.getText());
		elAngle = Double.parseDouble(txtElevtf.getText());
		rainfallRate = slider.getValue();
		carFreq = Double.parseDouble(txtCarfreq.getText());
		int antennaNoiseTemp = Integer.parseInt(txtNoisetemp.getText());
		int amplifierNoiseTemp = Integer.parseInt(txtampliNoiseTemp.getText());
		double antennaDiameter = Double.parseDouble(txtAntennaDiameter
				.getText());

		Station sta = new Station(latitude, altitude, rainfallRate, elAngle,
				carFreq, antennaNoiseTemp, amplifierNoiseTemp, antennaDiameter);
		return sta;
	}

	public static void main(String[] args) {
		Main mainclass = new Main();
	}
}
