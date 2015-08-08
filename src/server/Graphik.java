package server;

import graphik.MenuGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class Graphik extends JPanel {
	
	private JButton start;
	private JButton end;
	private JLabel [] PlayerLabel;
	private JLabel wartezeitLabel;
	private JSlider wartezeit;
	private JLabel tarifLabel;
	private JTextField tarif;
	
	private Server server;
	private Graphik g = this;
	private MenuGUI fenster;
	
	private String logo = "graphik/karten/logo.gif";
	
	private String configdir;
	
	private int spielerzahl;
	
	public Graphik(MenuGUI fenster, String configdir){
		super();
		
		this.fenster = fenster;
				
		this.configdir = configdir;
		
		PlayerLabel = new JLabel [4];
		
		spielerzahl = 0;
		
		try {
			initGUI();
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler während des Programmstarts");
			e.printStackTrace();
		}	
	}
	
	public void initGUI() {
		this.setSize(330, 370);
		
		this.setLayout(null);
		this.setBorder(new LineBorder(Color.gray, 3, false));
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setVisible(true);
		
		for(int i = 0; i < 4; i++){
			PlayerLabel [i] = new JLabel();
			this.add(PlayerLabel [i]);
			PlayerLabel [i].setText("");
			PlayerLabel [i].setBounds(10, 50 + i*40, getWidth()-20, 30);
			PlayerLabel [i].setVisible(true);
		}
		
		start = new JButton();
		this.add(start);
		start.setBounds(10, 10, getWidth()/2 - 15, 30);
		start.setText("Server starten");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(server == null) {
					server = new Server(g, configdir);
					server.setzeWartezeit(wartezeit.getValue());
					
					setzeTarif();
					
					server.start();
					
					clear();
					PlayerLabel[0].setText("Server gestartet");
					startAktualisieren();
					fenster.setTitle("Schafkopf: " + server.gibIP());
				} else {
					//Startet den Server mit den Spielern, die anwesend sind und füllt den Rest mit Bots auf
					setzeTarif();
					server.setzeSpielerzahl(spielerzahl);
				}
			}
		});
		start.setVisible(true);
		
		end = new JButton();
		this.add(end);
		end.setBounds(getWidth()/2 + 5, 10, getWidth()/2 - 15, 30);
		end.setText("Server stoppen");
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(server != null) {
					start.setText("Server starten");
					fenster.beenden();
					server.beenden();
					server = null;
				}
			}
		});
		end.setVisible(true);
		
		tarifLabel = new JLabel();
		this.add(tarifLabel);
		tarifLabel.setBounds(10, this.getHeight() - 130, (int) (this.getWidth() - 20) / 2, 30);
		tarifLabel.setText("Tarif des Spiels:");
		tarifLabel.setVisible(true);
		
		tarif = new JTextField();
		this.add(tarif);
		tarif.setBounds(this.getWidth() - 80, this.getHeight() - 130, 70, 30);
		tarif.setVisible(true);
		
		wartezeitLabel = new JLabel();
		this.add(wartezeitLabel);
		wartezeitLabel.setBounds(10, this.getHeight() - 90, this.getWidth() - 20, 30);
		wartezeitLabel.setText("Verzögerung des Spiels (ms):");
		wartezeitLabel.setVisible(true);
		
		wartezeit = new JSlider(100, 5000);
		this.add(wartezeit);
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
			tarif.setText(String.valueOf(tarifWert));
		}
		server.setzeTarif(tarifWert);
	}
	
	public void speichern() {
		clear();
		PlayerLabel[1].setText("Speichere Datenbanken");
	}
	
	public void beenden() {
		clear();
		spielerzahl = 0;
		PlayerLabel[0].setText("Server beendet");
		start.setText("Server starten");
		fenster.beenden();
	}
}

