package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Graphik extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Graphik inst = new Graphik();
			}
		});
	}
	
	private JButton start;
	private JButton end;
	private JLabel [] PlayerLabel;
	private JLabel wartezeitLabel;
	private JSlider wartezeit;
	private JLabel tarifLabel;
	private JTextField tarif;
	
	private Server server;
	private Graphik g = this;
	
	private String logo = "graphik/karten/logo.gif";
	
	private int spielerzahl;
	
	public Graphik(){
		super();
		
		Graphik.class.getResource(logo);
		//Icon der Anwendung setzen
		ImageIcon icon = new ImageIcon(logo);
		this.setIconImage(icon.getImage());
				
		PlayerLabel = new JLabel [4];
		
		spielerzahl = 0;
		
		try {
			initGUI();
			this.pack();
			this.setSize(330, 350);
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler während des Programmstarts");
			e.printStackTrace();
		}	
	}
	
	public void initGUI() {
		this.setSize(330, 350);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SCHAFKOPF-SERVER");
		this.setResizable(false);
		
		this.setLayout(null);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		for(int i = 0; i < 4; i++){
			PlayerLabel [i] = new JLabel();
			getContentPane().add(PlayerLabel [i]);
			PlayerLabel [i].setText("");
			PlayerLabel [i].setBounds(10, 50 + i*40, getWidth()-20, 30);
			PlayerLabel [i].setVisible(true);
		}
		
		start = new JButton();
		getContentPane().add(start);
		start.setBounds(10, 10, getWidth()/2 - 15, 30);
		start.setText("Server starten");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(server == null) {
					server = new Server(g);
					server.setzeWartezeit(wartezeit.getValue());
					
					setzeTarif();
					
					server.start();
					
					clear();
					PlayerLabel[0].setText("Server gestartet");
					startAktualisieren();
					g.setTitle("Schafkopf: " + server.gibIP());
				} else {
					//Startet den Server mit den Spielern, die anwesend sind und füllt den Rest mit Bots auf
					setzeTarif();
					server.setzeSpielerzahl(spielerzahl);
				}
			}
		});
		start.setVisible(true);
		
		end = new JButton();
		getContentPane().add(end);
		end.setBounds(getWidth()/2 + 5, 10, getWidth()/2 - 15, 30);
		end.setText("Server stoppen");
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(server != null) {
					start.setText("Server starten");
					server.beenden();
					server = null;
				}
			}
		});
		end.setVisible(true);
		
		tarifLabel = new JLabel();
		getContentPane().add(tarifLabel);
		tarifLabel.setBounds(10, this.getHeight() - 130, (int) (this.getWidth() - 20) / 2, 30);
		tarifLabel.setText("Tarif des Spiels:");
		tarifLabel.setVisible(true);
		
		tarif = new JTextField();
		getContentPane().add(tarif);
		tarif.setBounds(this.getWidth() - 80, this.getHeight() - 130, 70, 30);
		tarif.setVisible(true);
		
		wartezeitLabel = new JLabel();
		getContentPane().add(wartezeitLabel);
		wartezeitLabel.setBounds(10, this.getHeight() - 90, this.getWidth() - 20, 30);
		wartezeitLabel.setText("Geschwindigkeit des Spiels:");
		wartezeitLabel.setVisible(true);
		
		wartezeit = new JSlider(100, 5000);
		getContentPane().add(wartezeit);
		wartezeit.setBounds(10, this.getHeight() - 60, this.getWidth() - 20, 40);
		wartezeit.setValue(2000);
		wartezeit.setVisible(true);
	}
	
	public synchronized void textSetzen(ArrayList<Spieler> s) { 
		clear();
		ArrayList <Spieler> spieler = s;	
		spielerzahl = 0;
		
		for(int i = 0; i < 4; i++){
			String aufschrift;
			try {
				aufschrift = spieler.get(i).gibName();
				aufschrift += " - ";
				aufschrift += spieler.get(i).gibIP();
				//Ein Spieler wurde hinzugefügt
				spielerzahl = i + 1;
			} catch(Exception e) {
				aufschrift = "";
				//Kein neuer Spieler
			}
			PlayerLabel[i].setText(aufschrift);
		}
		
		startAktualisieren();
	}
	
	private void startAktualisieren() {
		if(4 - spielerzahl == 0) {
			start.setText("Spiel läuft...");
		}
		if(4 - spielerzahl == 1) {
			start.setText("1 Bot");
		}
		if(4 - spielerzahl > 1) {
			if(server != null) {
				start.setText(4 - spielerzahl + " Bots");
			} else {
				start.setText("Spiel starten");
			}
		}
	}
	
	public synchronized void clear() {
		for(int i = 0; i < 4; i++) {
			PlayerLabel[i].setText("");
		}
	}
	
	public synchronized void setzeTarif() {
		int tarifWert;
		try {
			tarifWert = Integer.parseInt(tarif.getText());
		} catch(Exception e) {
			tarifWert = 10;
			JOptionPane.showMessageDialog(g, "Keine Tarifangabe. Der Tarif ist jetzt 10");
		}
		server.setzeTarif(tarifWert);
	}
	
	public void beenden() {
		clear();
		spielerzahl = 0;
		PlayerLabel[0].setText("Server beendet");
		start.setText("Server starten");
	}
}

