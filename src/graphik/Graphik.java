package graphik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import regeln.Controll;
import lib.Karte;
import lib.Model;  
import lib.Model.modus;
import client.ModelMVC;
import client.View; 
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Graphik extends JFrame implements View {	
	
	//Enthält die Controll -> kontrolliert einen Spielzug
	private Controll controll;
	
	//Das Model
	private ModelMVC model;
	
	private int ID;
	
	//GUI
	//Layout
	private BorderLayout layout;
	//Karten des Spielers
	private Spieler spielerKarten;
	//Nachrichten des Clients
	private Meldungen spielerMeldungen;
	
	//Karten der Gegenspieler
	private Gegenspieler[] gegenspielerKarten;
	
	//Tisch
	private Tisch tisch;
	
	//Hintergrund
	private JLabel hintergrund;
	
	//Auswahldialoge
	
	
	
	public Graphik() {
		super();
		//Vorerst keine ID setzen
		ID = -1;
		
		model = new ModelMVC();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Schoafkopf-Äpp");
		this.setSize(825, 620);
		//Äußeres Layout nicht vorhanden
		this.setLayout(null);
		this.initGUI(); 
		this.setVisible(true);
	}	
	
	/**
	 * Erstellt eine neue GUI
	 */
	private void initGUI() {
		layout = new BorderLayout();
		
		hintergrund = new JLabel();
		getContentPane().add(hintergrund);
		//Das letzte fünftel des Fensters ist für Spielermeldungen
		hintergrund.setBounds(0, 0, this.getWidth() - this.getWidth() / 5, this.getHeight());
		hintergrund.setLayout(layout);
		hintergrund.setVisible(true);
		
		//-------------------------------------------------------------hintergrund
		//Karten auf dem Tisch
		tisch = new Tisch();
		hintergrund.add(tisch);
		layout.addLayoutComponent(tisch, layout.CENTER);
		tisch.setVisible(true);
		
		gegenspielerKarten = new Gegenspieler[3];
		
		for(int i = 0; i < gegenspielerKarten.length; i++) {
			//Name muss noch gesetzt werden
			gegenspielerKarten[i] = new Gegenspieler();
			hintergrund.add(gegenspielerKarten[i]);
			gegenspielerKarten[i].setVisible(true);
		}
		
		layout.addLayoutComponent(gegenspielerKarten[0], layout.WEST);
		layout.addLayoutComponent(gegenspielerKarten[1], layout.NORTH);
		layout.addLayoutComponent(gegenspielerKarten[2], layout.EAST);
		
		//Anzeige der Karten der Spieler
		spielerKarten = new Spieler();
		hintergrund.add(spielerKarten);
		layout.addLayoutComponent(spielerKarten, layout.SOUTH);
		spielerKarten.setVisible(true);
		//-------------------------------------------------------------hintergrund
		
		//Meldungen des Spielers (10 Meldungen werden gebuffert)
		spielerMeldungen = new Meldungen(10);
		getContentPane().add(spielerMeldungen);
		//Die Meldungen laufen im letzten Fünftel des Fensters
		spielerMeldungen.setBounds(this.getWidth() * (4 / 5), 0, this.getWidth() / 5, this.getHeight());
		spielerMeldungen.setVisible(true);
	}
	
	/**
	 * aktualisiert das Model
	 */
	public void update(ModelMVC model) {
		this.model = model;		
	}
	
	/**
	 * Setzt die ID des Spielers, damit die Daten aus dem Model ausgelesen werden können
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	/**
	 * Setzt die Namen der Spieler
	 * @param namen
	 */
	public void setzeNamen(String[] namen) {
		String[] spieler = namen;
		
		spielerMeldungen.festeAnzeige(spieler[ID]);
		
		int start = ID;
		
		for(int i = 0; i < 3; i++) {
			//Der Spieler links vom vorherigen Spieler
			start++;
			//Nach Nr. 3 wird neu begonnen
			start %= 4;
			
			//Gegenspieler den richtigen Namen zuweisen
			gegenspielerKarten[i].name(spieler[start]);
		}
	}
	
	/**
	 * Spielzug des Spielers durchführen
	 */
	public void spiel() {
		
	}
	
	/**
	 * Ändert den Modus des Spiels und ändert die Controll
	 * @param mod
	 */
	public void setzeModus(modus mod) {
		
	}
	
	/**
	 * Fragt den Nutzer, ob er etwas spielen will und gibt den Spielmodus zurück
	 * @param model
	 * @return
	 */
	public modus spielstDu() {
		return null;
	}
	
	/**
	 * Gibt zurück, ob der Spieler klopft
	 * @return
	 */
	public boolean klopfstDu() {
		return false;
	}
	
	/**
	 * Zeigt den/die Sieger eines Spiels an
	 * Wenn s2 4 ist, so gibt es keinen zweiten Sieger,
	 * ist s1/s2 zwischen 10 und 13 (11 - 10 = 1 -> der zweite Spieler hat verloren), 
	 * so hat jemand sein Spiel verloren
	 * @param s1
	 * @param s2
	 */
	public void sieger(int s1, int s2) {
		
	}

	/**
	 * Gibt "JA" zurück, wenn der Spieler heiraten will
	 * @return
	 */
	public String hochzeit() {
		return null;
	}

	/**
	 * Gibt die Karte zurück, die der Spieler bei der Hochzeit tauschen will
	 * Wird aufgerufen, wenn jmd. eine Hochzeit anbietet und wenn eine angenommen
	 * wird
	 * @return
	 */
	public Karte hochzeitKarte() {
		return null;
	}

	/**
	 * Empfängt, wer spielt. Wird keine Hochzeit gespielt ist mitspieler gleich 4
	 * @param spielt
	 * @param mitspieler 
	 */
	public void spielt(int spielt, int mitspieler) { 
		 
	}

	/**
	 * Zeigt an, wer Kontra gegeben hat
	 * @param kontra
	 */
	public void kontra(boolean[] kontra) {
		
	}

	/**
	 * Zeigt an, wer geklopft hat
	 * @param geklopft
	 */
	public void geklopft(boolean[] geklopft) {
		
	}	

	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		//Hintergrundlabel an Fenster anpassen
		hintergrund.setSize(this.getWidth() - this.getWidth() / 5, this.getHeight());
		//Meldungen an Fenster anpassen
		spielerMeldungen.setBounds(this.getWidth() * (4 / 5), 0, this.getWidth() / 5, this.getHeight());
	}
}

