package dsp.netem.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;

import dsp.netem.NETEMctrl;
import dsp.unige.figures.ChannelHelper;
import dsp.unige.figures.ChannelHelper.Satellite;
import dsp.unige.figures.ChannelHelper.Station;
import dsp.unige.figures.Modulation;
import dsp.unige.figures.Orbits;
import dsp.unige.figures.SimConstants;

import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.DropMode;
import javax.swing.JScrollPane;

public class Main extends JFrame {

	private static final long serialVersionUID = -5632906239127044315L;
	private static final int WIN_WIDTH = 800;
	private static final int WIN_HEIGHT = 600;

	private JFormattedTextField txtEirptf;
	private JFormattedTextField txtTranspbw;
	private JFormattedTextField txtCarfreq;
	private JFormattedTextField latitude_tf;
	private JFormattedTextField txtAltitudetf;
	private JFormattedTextField txtElevtf;
	private JFormattedTextField frmtdtxtfldNoisetemp; 
	
	private JFrame frmSatelliteEmulator;
	private JButton btnSet;
	private JSlider slider;
	private JComboBox orbitComboBox, modulationComboBox;
	JTextArea txtrDatamisc, txtrNetemoutput;
	
	public Main(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initUI();
	}


	private void initUI() {

		frmSatelliteEmulator = new JFrame("JSpinner Sample");
		
		frmSatelliteEmulator.setTitle("Satellite Emulator");
		frmSatelliteEmulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel sat_panel = new JPanel();
		frmSatelliteEmulator.getContentPane().add(sat_panel, BorderLayout.WEST);
		GridBagLayout gbl_sat_panel = new GridBagLayout();
		gbl_sat_panel.columnWidths = new int[]{0, 0, 51, 0};
		gbl_sat_panel.rowHeights = new int[]{0, 15, 0, 0, 0, 0, 0, 0};
		gbl_sat_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_sat_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		sat_panel.setLayout(gbl_sat_panel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 0;
		sat_panel.add(verticalStrut, gbc_verticalStrut);
		
		JLabel satLabel = new JLabel("Satellite");
		satLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		GridBagConstraints gbc_satLabel = new GridBagConstraints();
		gbc_satLabel.insets = new Insets(0, 0, 5, 5);
		gbc_satLabel.anchor = GridBagConstraints.NORTH;
		gbc_satLabel.gridx = 1;
		gbc_satLabel.gridy = 1;
		sat_panel.add(satLabel, gbc_satLabel);
		
		Component verticalStrut_4 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_4 = new GridBagConstraints();
		gbc_verticalStrut_4.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_4.gridx = 1;
		gbc_verticalStrut_4.gridy = 2;
		sat_panel.add(verticalStrut_4, gbc_verticalStrut_4);
		
		JLabel lblEirp = new JLabel("EIRP");
		GridBagConstraints gbc_lblEirp = new GridBagConstraints();
		gbc_lblEirp.anchor = GridBagConstraints.EAST;
		gbc_lblEirp.insets = new Insets(0, 0, 5, 5);
		gbc_lblEirp.gridx = 1;
		gbc_lblEirp.gridy = 3;
		sat_panel.add(lblEirp, gbc_lblEirp);
		
		txtEirptf = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtEirptf.setText("45");
		GridBagConstraints gbc_txtEirptf = new GridBagConstraints();
		gbc_txtEirptf.anchor = GridBagConstraints.WEST;
		gbc_txtEirptf.insets = new Insets(0, 0, 5, 0);
		gbc_txtEirptf.gridx = 2;
		gbc_txtEirptf.gridy = 3;
		sat_panel.add(txtEirptf, gbc_txtEirptf);
		txtEirptf.setColumns(10);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 4;
		sat_panel.add(horizontalStrut, gbc_horizontalStrut);
		
		JLabel lblTransponderBandwidth = new JLabel("Transponder Bandwidth");
		GridBagConstraints gbc_lblTransponderBandwidth = new GridBagConstraints();
		gbc_lblTransponderBandwidth.anchor = GridBagConstraints.EAST;
		gbc_lblTransponderBandwidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblTransponderBandwidth.gridx = 1;
		gbc_lblTransponderBandwidth.gridy = 4;
		sat_panel.add(lblTransponderBandwidth, gbc_lblTransponderBandwidth);
		
		txtTranspbw = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtTranspbw.setText("600000000");
		GridBagConstraints gbc_txtTranspbw = new GridBagConstraints();
		gbc_txtTranspbw.anchor = GridBagConstraints.WEST;
		gbc_txtTranspbw.insets = new Insets(0, 0, 5, 0);
		gbc_txtTranspbw.gridx = 2;
		gbc_txtTranspbw.gridy = 4;
		sat_panel.add(txtTranspbw, gbc_txtTranspbw);
		txtTranspbw.setColumns(10);
		
		JLabel lblOrbitType = new JLabel("Orbit Type");
		GridBagConstraints gbc_lblOrbitType = new GridBagConstraints();
		gbc_lblOrbitType.anchor = GridBagConstraints.EAST;
		gbc_lblOrbitType.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrbitType.gridx = 1;
		gbc_lblOrbitType.gridy = 5;
		sat_panel.add(lblOrbitType, gbc_lblOrbitType);
		
		String[] orbitsStrings = {
				Orbits.getOrbitName(0),
				Orbits.getOrbitName(1),
				Orbits.getOrbitName(2),
				Orbits.getOrbitName(3),
		};
		orbitComboBox = new JComboBox(orbitsStrings);

		GridBagConstraints gbc_orbitComboBox = new GridBagConstraints();
		gbc_orbitComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_orbitComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_orbitComboBox.gridx = 2;
		gbc_orbitComboBox.gridy = 5;
		sat_panel.add(orbitComboBox, gbc_orbitComboBox);
		
		JLabel lblModulation = new JLabel("Modulation");
		GridBagConstraints gbc_lblModulation = new GridBagConstraints();
		gbc_lblModulation.anchor = GridBagConstraints.EAST;
		gbc_lblModulation.insets = new Insets(0, 0, 0, 5);
		gbc_lblModulation.gridx = 1;
		gbc_lblModulation.gridy = 6;
		sat_panel.add(lblModulation, gbc_lblModulation);
		
		String []  modulation = new String[Modulation.mods];
		for(int i=0;i<modulation.length;i++){
			modulation[i]=Modulation.getHRname(i);
		}
		modulationComboBox = new JComboBox(modulation);
		GridBagConstraints gbc_modulationComboBox = new GridBagConstraints();
		gbc_modulationComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_modulationComboBox.gridx = 2;
		gbc_modulationComboBox.gridy = 6;
		sat_panel.add(modulationComboBox, gbc_modulationComboBox);
		
		JPanel sta_panel = new JPanel();
		frmSatelliteEmulator.getContentPane().add(sta_panel, BorderLayout.EAST);
		GridBagLayout gbl_sta_panel = new GridBagLayout();
		gbl_sta_panel.columnWidths = new int[]{0, 0, 0, 0, 45, 0, 0};
		gbl_sta_panel.rowHeights = new int[]{0, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_sta_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_sta_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		sta_panel.setLayout(gbl_sta_panel);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 3;
		gbc_verticalStrut_1.gridy = 0;
		sta_panel.add(verticalStrut_1, gbc_verticalStrut_1);
		
		JLabel staLabel = new JLabel("Station");
		staLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		GridBagConstraints gbc_staLabel = new GridBagConstraints();
		gbc_staLabel.insets = new Insets(0, 0, 5, 5);
		gbc_staLabel.anchor = GridBagConstraints.NORTH;
		gbc_staLabel.gridx = 1;
		gbc_staLabel.gridy = 1;
		sta_panel.add(staLabel, gbc_staLabel);
		
		Component verticalStrut_5 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_5 = new GridBagConstraints();
		gbc_verticalStrut_5.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_5.gridx = 1;
		gbc_verticalStrut_5.gridy = 2;
		sta_panel.add(verticalStrut_5, gbc_verticalStrut_5);
		
		JLabel lblLatitude = new JLabel("Latitude");
		GridBagConstraints gbc_lblLatitude = new GridBagConstraints();
		gbc_lblLatitude.anchor = GridBagConstraints.EAST;
		gbc_lblLatitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblLatitude.gridx = 1;
		gbc_lblLatitude.gridy = 3;
		sta_panel.add(lblLatitude, gbc_lblLatitude);
		
		latitude_tf = new JFormattedTextField(NumberFormat.getNumberInstance());
		latitude_tf.setText("45");
		GridBagConstraints gbc_latitude_tf = new GridBagConstraints();
		gbc_latitude_tf.fill = GridBagConstraints.HORIZONTAL;
		gbc_latitude_tf.insets = new Insets(0, 0, 5, 5);
		gbc_latitude_tf.gridx = 4;
		gbc_latitude_tf.gridy = 3;
		sta_panel.add(latitude_tf, gbc_latitude_tf);
		latitude_tf.setColumns(10);
		
		JLabel lblAltitude = new JLabel("Altitude");
		GridBagConstraints gbc_lblAltitude = new GridBagConstraints();
		gbc_lblAltitude.anchor = GridBagConstraints.EAST;
		gbc_lblAltitude.insets = new Insets(0, 0, 5, 5);
		gbc_lblAltitude.gridx = 1;
		gbc_lblAltitude.gridy = 4;
		sta_panel.add(lblAltitude, gbc_lblAltitude);
		
		txtAltitudetf = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtAltitudetf.setText("0");
		GridBagConstraints gbc_txtAltitudetf = new GridBagConstraints();
		gbc_txtAltitudetf.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAltitudetf.insets = new Insets(0, 0, 5, 5);
		gbc_txtAltitudetf.gridx = 4;
		gbc_txtAltitudetf.gridy = 4;
		sta_panel.add(txtAltitudetf, gbc_txtAltitudetf);
		txtAltitudetf.setColumns(10);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 5;
		gbc_horizontalStrut_1.gridy = 5;
		sta_panel.add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
		gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_2.gridx = 0;
		gbc_horizontalStrut_2.gridy = 6;
		sta_panel.add(horizontalStrut_2, gbc_horizontalStrut_2);
		
		JLabel lblPolarization = new JLabel("Polarization");
		GridBagConstraints gbc_lblPolarization = new GridBagConstraints();
		gbc_lblPolarization.anchor = GridBagConstraints.EAST;
		gbc_lblPolarization.insets = new Insets(0, 0, 5, 5);
		gbc_lblPolarization.gridx = 1;
		gbc_lblPolarization.gridy = 6;
		sta_panel.add(lblPolarization, gbc_lblPolarization);
		
		JLabel lblCircular = new JLabel("Circular");
		lblCircular.setEnabled(false);
		lblCircular.setForeground(Color.BLACK);
		GridBagConstraints gbc_lblCircular = new GridBagConstraints();
		gbc_lblCircular.insets = new Insets(0, 0, 5, 5);
		gbc_lblCircular.gridx = 4;
		gbc_lblCircular.gridy = 6;
		sta_panel.add(lblCircular, gbc_lblCircular);
		
		JLabel lblElevationAngle = new JLabel("Elevation Angle");
		GridBagConstraints gbc_lblElevationAngle = new GridBagConstraints();
		gbc_lblElevationAngle.anchor = GridBagConstraints.EAST;
		gbc_lblElevationAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblElevationAngle.gridx = 1;
		gbc_lblElevationAngle.gridy = 7;
		sta_panel.add(lblElevationAngle, gbc_lblElevationAngle);
		
		txtElevtf = new JFormattedTextField(NumberFormat.getNumberInstance());
		txtElevtf.setText("47.1");
		GridBagConstraints gbc_txtElevtf = new GridBagConstraints();
		gbc_txtElevtf.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtElevtf.insets = new Insets(0, 0, 5, 5);
		gbc_txtElevtf.gridx = 4;
		gbc_txtElevtf.gridy = 7;
		sta_panel.add(txtElevtf, gbc_txtElevtf);
		txtElevtf.setColumns(10);
		
		JLabel lblRainfallRate = new JLabel("Rainfall Rate");
		GridBagConstraints gbc_lblRainfallRate = new GridBagConstraints();
		gbc_lblRainfallRate.anchor = GridBagConstraints.EAST;
		gbc_lblRainfallRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblRainfallRate.gridx = 1;
		gbc_lblRainfallRate.gridy = 8;
		sta_panel.add(lblRainfallRate, gbc_lblRainfallRate);
		
		slider = new JSlider();
		slider.setMaximum(SimConstants.MAXRAINFALLRATE);
		slider.setMinimum(0);
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(10);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		
		slider.setPaintTicks(true);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 4;
		gbc_slider.gridy = 8;
		sta_panel.add(slider, gbc_slider);
		
		JLabel lblCarrierFrequency = new JLabel("Carrier Frequency");
		GridBagConstraints gbc_lblCarrierFrequency = new GridBagConstraints();
		gbc_lblCarrierFrequency.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarrierFrequency.gridx = 1;
		gbc_lblCarrierFrequency.gridy = 9;
		sta_panel.add(lblCarrierFrequency, gbc_lblCarrierFrequency);
		
		JLabel lblNoiseTemperature = new JLabel("Noise Temperature");
		GridBagConstraints gbc_lblNoiseTemperature = new GridBagConstraints();
		gbc_lblNoiseTemperature.insets = new Insets(0, 0, 5, 5);
		gbc_lblNoiseTemperature.gridx = 1;
		gbc_lblNoiseTemperature.gridy = 10;
		sta_panel.add(lblNoiseTemperature, gbc_lblNoiseTemperature);
		
		frmtdtxtfldNoisetemp = new JFormattedTextField();
		frmtdtxtfldNoisetemp.setText("60");
		GridBagConstraints gbc_frmtdtxtfldNoisetemp = new GridBagConstraints();
		gbc_frmtdtxtfldNoisetemp.fill = GridBagConstraints.HORIZONTAL;
		gbc_frmtdtxtfldNoisetemp.insets = new Insets(0, 0, 5, 5);
		gbc_frmtdtxtfldNoisetemp.gridx = 4;
		gbc_frmtdtxtfldNoisetemp.gridy = 10;
		sta_panel.add(frmtdtxtfldNoisetemp, gbc_frmtdtxtfldNoisetemp);

		Component verticalStrut_3 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_3 = new GridBagConstraints();
		gbc_verticalStrut_3.gridy = 11;
		gbc_verticalStrut_3.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_3.gridx = 2;
		sta_panel.add(verticalStrut_3, gbc_verticalStrut_3);
		
		txtCarfreq = new JFormattedTextField(NumberFormat.getNumberInstance());
		GridBagConstraints gbc_txtCarfreq = new GridBagConstraints();
		gbc_txtCarfreq.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCarfreq.insets = new Insets(0, 0, 5, 5);
		gbc_txtCarfreq.gridx = 4;
		gbc_txtCarfreq.gridy = 9;
		sta_panel.add(txtCarfreq, gbc_txtCarfreq);
		txtCarfreq.setText("19.7");
		txtCarfreq.setColumns(10);
		
		JPanel status_panel = new JPanel();
		frmSatelliteEmulator.getContentPane().add(status_panel, BorderLayout.SOUTH);
		GridBagLayout gbl_status_panel = new GridBagLayout();
		gbl_status_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_status_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_status_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_status_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		status_panel.setLayout(gbl_status_panel);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_2.gridx = 1;
		gbc_verticalStrut_2.gridy = 0;
		status_panel.add(verticalStrut_2, gbc_verticalStrut_2);
		
		btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setParameters();
			}
		});
		
		GridBagConstraints gbc_btnSet = new GridBagConstraints();
		gbc_btnSet.insets = new Insets(0, 0, 5, 0);
		gbc_btnSet.gridx = 1;
		gbc_btnSet.gridy = 1;
		status_panel.add(btnSet, gbc_btnSet);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_3 = new GridBagConstraints();
		gbc_horizontalStrut_3.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_3.gridx = 3;
		gbc_horizontalStrut_3.gridy = 3;
		status_panel.add(horizontalStrut_3, gbc_horizontalStrut_3);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_4 = new GridBagConstraints();
		gbc_horizontalStrut_4.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_4.gridx = 0;
		gbc_horizontalStrut_4.gridy = 4;
		status_panel.add(horizontalStrut_4, gbc_horizontalStrut_4);
		
		Component verticalStrut_7 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_7 = new GridBagConstraints();
		gbc_verticalStrut_7.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_7.gridx = 1;
		gbc_verticalStrut_7.gridy = 4;
		status_panel.add(verticalStrut_7, gbc_verticalStrut_7);
		
		txtrDatamisc = new JTextArea(4,10);
		txtrDatamisc.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
		txtrDatamisc.setTabSize(5);
		txtrDatamisc.setWrapStyleWord(true);
		txtrDatamisc.setEditable(false);
		txtrDatamisc.setText("please apply settings to continue..");
		GridBagConstraints gbc_txtrDatamisc = new GridBagConstraints();
		gbc_txtrDatamisc.insets = new Insets(0, 0, 5, 0);
		gbc_txtrDatamisc.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtrDatamisc.gridx = 1;
		gbc_txtrDatamisc.gridy = 5;
		status_panel.add(txtrDatamisc, gbc_txtrDatamisc);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		status_panel.add(scrollPane, gbc_scrollPane);
		
		
		txtrNetemoutput = new JTextArea(8,10);
		scrollPane.setViewportView(txtrNetemoutput);
		txtrNetemoutput.setLineWrap(true);
		txtrNetemoutput.setEditable(false);
		txtrNetemoutput.setWrapStyleWord(true);
		
		Component verticalStrut_6 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_6 = new GridBagConstraints();
		gbc_verticalStrut_6.gridx = 1;
		gbc_verticalStrut_6.gridy = 7;
		status_panel.add(verticalStrut_6, gbc_verticalStrut_6);
		
		frmSatelliteEmulator.pack();

		JMenuBar menuBar = new JMenuBar();
		frmSatelliteEmulator.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem saveConf = new JMenuItem("Save configuration...");
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
				Station sta = parseStationParameters();
				Satellite sat = parseSatelliteParameters();
				
			}
		});
		JMenuItem loadConf = new JMenuItem("Load configuration...");

		mnFile.add(saveConf);
		mnFile.add(loadConf);
		mnFile.add(exit);
		
		
		JMenu mnHelp = new JMenu("?");
		JMenuItem about = new JMenuItem("About..."); 
		menuBar.add(mnHelp);
		mnHelp.add(about);
		
		frmSatelliteEmulator.setVisible (true);

	}

	protected void setParameters() {
		ChannelHelper.Station sta = parseStationParameters();
		ChannelHelper.Satellite sat = parseSatelliteParameters();
		
		int rate = ChannelHelper.getRate(sta, sat);
		double ber = ChannelHelper.getBER(sta, sat);
		
		NETEMctrl c=new NETEMctrl("FUFFA");  // TODO leva
		StringBuffer sb=c.setNetworkConditions(""+String.format("%6.4f",rate/1000d), String.format("%6.2f",ber), 0+"", Orbits.getDelay(sat.ORBIT_TYPE) +"") ;
		
		setMiscData(sat, sta, rate,ber);
		txtrNetemoutput.append("\n"+sb.toString());
		frmSatelliteEmulator.pack();
	}


	private void setMiscData(Satellite sat, Station sta, int rate, double ber) {
		String st = "Freespace Loss = "+ChannelHelper.getFreeSpaceLoss(sta, sat)+" dB\nRain Attenuation = "+ChannelHelper.getRainAttenuation(sta)+" dB";
		st += "\nSNR = "+(ChannelHelper.getSdB(sta, sat) - ChannelHelper.getNoisePower(sta, sat)) +" dB";
		st += "\n    "+rate+" bps, "+String.format("%6.2f",ber*100)+"% BER";
		txtrDatamisc.setText(st);
	}


	private Satellite parseSatelliteParameters() {
		
		double EIRP;
		int orbType, modulation, transpBW;
		
		EIRP = Double.parseDouble(txtEirptf.getText());
		transpBW = Integer.parseInt(txtTranspbw.getText());
		orbType = orbitComboBox.getSelectedIndex(); 
		modulation = modulationComboBox.getSelectedIndex();
		
		Satellite sat = new Satellite(EIRP, transpBW, orbType, modulation);
		return sat;
	}


	private Station parseStationParameters() {
		
		double latitude, altitude, elAngle, rainfallRate, carFreq;
		latitude = Double.parseDouble(latitude_tf.getText());
		altitude = Double.parseDouble(txtAltitudetf.getText());
		elAngle = Double.parseDouble(txtElevtf.getText());
		rainfallRate = slider.getValue();
		carFreq = Double.parseDouble(txtCarfreq.getText());
		int noiseTemp = Integer.parseInt(frmtdtxtfldNoisetemp.getText());

		Station sta =  new Station(latitude, altitude, rainfallRate, elAngle, carFreq, noiseTemp);
		return sta;
	}


	public static void main(String[] args) {

		Main mainclass=new Main();
	}
}
