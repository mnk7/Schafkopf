package graphik;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.Client;

public class MenuGUI extends JFrame implements Menu {

	/**
	 * Startet den Client und die Graphik
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			//Config-Verzeichnis erstellen
			configdir = System.getProperty("user.home");
			configdir += "/.schafkopf/";
			new File(configdir).mkdir();
		} else {
			configdir = args[0];
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				MenuGUI inst = new MenuGUI();
			}
		});  
	}
	
	private static String configdir;
	
	private Client client; 
	
	//GUI
	private JButton connect; 
	private JLabel IPlabel;
	private JLabel NAMElabel; 
	private JLabel hintergrund;
	private JTextField IPtf;
	private JTextField NAMEtf;
	
	private server.Graphik server;
	
	private String logo = "graphik/karten/Logo.gif";
	
	public MenuGUI() {
		super();
		
		try {
			initGUI();
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler während des Programmstarts");
			e.printStackTrace();
		}	
	}
	
	/**
	 * Versucht Verbindung mit dem Server herzustellen
	 */
	public void verbinden() {
		try {
			if(IPtf.getText().equals("")) {
				javax.swing.JOptionPane.showMessageDialog(null, "Geben sie eine Serveradresse an!");
			} else {
				if(NAMEtf.getText().equals("") || NAMEtf.getText().equalsIgnoreCase("BOT")) {
					javax.swing.JOptionPane.showMessageDialog(null, "Geben sie einen Namen an!");
				} else {
					client = new Client(IPtf.getText(), NAMEtf.getText(), this);
					client.graphik(this.gibGraphik());
					//unsichtbar machen wenn der Client nicht auch Server ist
					if(!IPtf.getText().equals("localhost")) {
						this.setVisible(false);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler während des Verbindungsaufbaus");
			//Fehlende Einträge markieren
			if(IPtf.getText().equals("")) IPlabel.setForeground(Color.RED);
			if(NAMEtf.getText().equals("")) NAMElabel.setForeground(Color.RED);
			
			repaint();
		}
	}
	
	/**
	 * Erstellt die GUI
	 */
	public void initGUI() {		
		//Fenster
		this.setSize(330, 530);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SCHAFKOPF-APP");
		this.setResizable(false);
		
		//Icon der Anwendung setzen
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(logo));
		this.setIconImage(icon.getImage());
		
		this.setLayout(null);
		//Lässt alles so aussehen wie im jeweiligen OS üblich
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		hintergrund = new JLabel();
		getContentPane().add(hintergrund);
		hintergrund.setBounds(0, 0, this.getWidth(), this.getHeight());
		hintergrund.setVisible(true);
		
		//Beschriftung
		IPlabel = new JLabel();
		hintergrund.add(IPlabel);
		IPlabel.setText("IP-Adresse des Servers: ");
		IPlabel.setBounds(10, 10, 180, 30);
		IPlabel.setVisible(true);
		
		NAMElabel = new JLabel();
		hintergrund.add(NAMElabel);
		NAMElabel.setText("Name:");
		NAMElabel.setBounds(200, 10, 120, 30);
		NAMElabel.setVisible(true);
		
		//Eingabe
		IPtf = new JTextField();
		hintergrund.add(IPtf);
		IPtf.setBounds(10, 50, 180, 30);
		IPtf.setVisible(true);
		
		NAMEtf = new JTextField();
		hintergrund.add(NAMEtf);
		NAMEtf.setBounds(200, 50, 120, 30);
		NAMEtf.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
					enter();
			}
			public void keyTyped(KeyEvent arg0) {
			}	
			
		});

		NAMEtf.setVisible(true);
		
		//Knopf
		connect = new JButton(); 
		hintergrund.add(connect);
		connect.setBounds(10, 90, 310, 30);
		connect.setText("Verbinde mit Server");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				enter();
			}
		});
		connect.setVisible(true);
		
		//Kann einen Server hosten
		server = new server.Graphik(this, configdir);
		hintergrund.add(server);
		server.setBounds(0, 150, server.getWidth(), server.getHeight());
		
		repaint();
	}

	private void enter() {
		IPlabel.setForeground(Color.BLACK);
		NAMElabel.setForeground(Color.BLACK);
		verbinden();
	}
	
	public View gibGraphik() {
		return new Graphik(new lib.Model(), client);
	}
	
	public void beenden() {
		try {
			client.dispose();
			client = null;
		} catch(Exception e) {
			//kein Client - nur Server
		}
		this.setVisible(true);
	}
}
