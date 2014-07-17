package dsp.unige.receiver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.DatagramSocket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JEditorPane;

public class Main extends JFrame {

	private static final long serialVersionUID = -5632906239127044315L;
	private static final int WIN_WIDTH = 800;
	private static final int WIN_HEIGHT = 600;

	DatagramSocket ds;

	public Main(){
		initUI();
	}


	private void initUI() {

		JFrame frmSatelliteEmulator = new JFrame("JSpinner Sample");
		frmSatelliteEmulator.setTitle("Satellite Emulator");
		frmSatelliteEmulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSatelliteEmulator.getContentPane().setLayout(new GridLayout(2,2));
		
		String orbits[] = new String[4];
		orbits[0]=Orbits.getOrbitName(Orbits.ORBIT_TYPE_GEO);
		orbits[1]=Orbits.getOrbitName(Orbits.ORBIT_TYPE_HEO);
		orbits[2]=Orbits.getOrbitName(Orbits.ORBIT_TYPE_LEO);
		orbits[3]=Orbits.getOrbitName(Orbits.ORBIT_TYPE_MEO);;
		
		JPanel main_panel = new JPanel();
		frmSatelliteEmulator.getContentPane().add(main_panel);
		GridBagLayout gbl_main_panel = new GridBagLayout();
		main_panel.setLayout(gbl_main_panel);
		
				
				JComboBox orbitList = new JComboBox(orbits);
				orbitList.setFont(new Font("Comfortaa", Font.PLAIN, 20));
				JLabel label1 = new JLabel("Orbit Type");
				label1.setFont(new Font("Comfortaa", Font.PLAIN, 17));
				JPanel orbit_panel = new JPanel();
				GridBagConstraints gbc_orbit_panel = new GridBagConstraints();
				gbc_orbit_panel.anchor = GridBagConstraints.WEST;
				gbc_orbit_panel.insets = new Insets(10, 10, 10, 10);
				gbc_orbit_panel.gridx = 0;
				gbc_orbit_panel.gridy = 0;
				main_panel.add(orbit_panel, gbc_orbit_panel);
				orbit_panel.setLayout(new FlowLayout());
				orbit_panel.add(label1);
				orbit_panel.add(orbitList);
						
						JButton btnNewButton = new JButton("Set");
						GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
						gbc_btnNewButton.insets = new Insets(10, 10, 10, 10);
						gbc_btnNewButton.gridx = 1;
						gbc_btnNewButton.gridy = 0;
						main_panel.add(btnNewButton, gbc_btnNewButton);
						
						JPanel rain_panel = new JPanel();
						GridBagConstraints gbc_rain_panel = new GridBagConstraints();
						gbc_rain_panel.anchor = GridBagConstraints.WEST;
						gbc_rain_panel.insets = new Insets(10, 10, 10, 10);
						gbc_rain_panel.gridx = 0;
						gbc_rain_panel.gridy = 1;
						main_panel.add(rain_panel, gbc_rain_panel);
						rain_panel.setLayout(new FlowLayout());
						JLabel label2 = new JLabel("Rain Attenuation");
						label2.setFont(new Font("Comfortaa", Font.PLAIN, 17));
						JSlider rainSlider=new JSlider(JSlider.HORIZONTAL,0,(int)Constants.L_RAIN_MAX,0);
						JLabel label3 = new JLabel("0 [dB]");
						label3.setFont(new Font("Comfortaa", Font.PLAIN, 17));
						rain_panel.add(label2);
						rain_panel.add(rainSlider);
						rain_panel.add(label3);
				
						JPanel feedback_panel = new JPanel();
						GridBagConstraints gbc_feedback_panel = new GridBagConstraints();
						gbc_feedback_panel.insets = new Insets(10, 10, 10, 10);
						gbc_feedback_panel.gridx = 0;
						gbc_feedback_panel.gridy = 2;
						main_panel.add(feedback_panel, gbc_feedback_panel);
						JLabel footerLabel=new JLabel();
						footerLabel.setFont(new Font("Comfortaa", Font.PLAIN, 17));
						footerLabel.setText("Interesting Channel data:\n La=120 dB, \n\n\nLr=10 dB, C=400 kbps, etc.");
						feedback_panel.add(footerLabel);
						
						JEditorPane editorPane = new JEditorPane();
						GridBagConstraints gbc_editorPane = new GridBagConstraints();
						gbc_editorPane.insets = new Insets(0, 0, 0, 5);
						gbc_editorPane.gridx = 0;
						gbc_editorPane.gridy = 3;
						main_panel.add(editorPane, gbc_editorPane);
		
		frmSatelliteEmulator.pack();
		frmSatelliteEmulator.setVisible (true);

	}

	public static void main(String[] args) {

		Main mainclass=new Main();
	}



}
