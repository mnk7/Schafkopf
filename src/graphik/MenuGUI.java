package graphik;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import client.Client;

public class MenuGUI extends JFrame{

	/**
	 * Startet den Client und die Graphik
	 * @param args
	 */
	public static void main(String[] args) {
		MenuGUI menu = new MenuGUI();
	}
	
	private Client client;
	
	//GUI
	private JButton connect;
	private JLabel IPlabel;
	private JLabel NAMElabel;
	private JTextField IPtf;
	private JTextField NAMEtf;
	
	public MenuGUI() {
		super();
		
		try {
			initGUI();
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler beim Programmstart");
		}
		
	}
	
	/**
	 * Versucht Verbindung mit dem Server herzustellen
	 */
	public void verbinden() {
		try {
			client = new Client(IPtf.getText(), NAMEtf.getText());
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler beim Verbindungsaufbau");
			//Fehlende Einträge markieren
			if(IPtf.getText().equals("")) IPlabel.setForeground(Color.RED);
			if(NAMEtf.getText().equals("")) NAMElabel.setForeground(Color.RED);
			repaint();
		}
	}
	
	private void fehler() {
		
	}
	
	/**
	 * Erstellt die GUI
	 */
	public void initGUI() {
		//Fenster
		this.setSize(330, 150);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SCHOAFKOPF-ÄPP");
		this.setResizable(false);
		//GUI.class.getResource("");
		//ImageIcon icon = new ImageIcon(GUI.class.getResource(""));
		//this.setIconImage(icon.getImage());
		this.setLayout(null);
		//Lässt alles so aussehen wie im jeweiligen OS üblich
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		//Beschriftung
		IPlabel = new JLabel();
		getContentPane().add(IPlabel);
		IPlabel.setText("IP-Adresse des Servers: ");
		IPlabel.setBounds(10, 10, 180, 30);
		IPlabel.setVisible(true);
		
		NAMElabel = new JLabel();
		getContentPane().add(NAMElabel);
		NAMElabel.setText("Name:");
		NAMElabel.setBounds(200, 10, 120, 30);
		NAMElabel.setVisible(true);
		
		//Eingabe
		IPtf = new JTextField();
		getContentPane().add(IPtf);
		IPtf.setBounds(10, 50, 180, 30);
		IPtf.setVisible(true);
		
		NAMEtf = new JTextField();
		getContentPane().add(NAMEtf);
		NAMEtf.setBounds(200, 50, 120, 30);
		NAMEtf.setVisible(true);
		
		//Knopf
		connect = new JButton();
		getContentPane().add(connect);
		connect.setBounds(10, 90, 310, 30);
		connect.setText("Verbinde mit Server");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				verbinden();
			}
		});
		connect.setVisible(true);
	}

}
