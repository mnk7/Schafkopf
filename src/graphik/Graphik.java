package graphik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
	private String[] namen;
	
	//GUI
	//Karten des Spielers
	private Spieler spielerKarten;
	//Nachrichten des Clients
	private Meldungen spielerMeldungen;
	
	//Karten der Gegenspieler
	private Gegenspieler[] gegenspielerKarten;
	
	//Tisch
	private Tisch tisch;
	
	//Hintergrund
	private JPanel hintergrund;
	
	//Auswahldialoge
	//Kontra
	//Klopfen
	//Spielmodus
	//Hochzeit
	
	
	public Graphik() {
		super();
		//Vorerst keine ID setzen
		ID = -1;
		
		model = new ModelMVC();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle("Schoafkopf-Äpp");
		this.setSize(1290, 700);
		//arrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrg
		this.setResizable(false);
		//Äußeres Layout nicht vorhanden
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.initGUI(); 
		this.setVisible(true);
	}	
	
	/**
	 * Erstellt eine neue GUI
	 */
	private void initGUI() {
		try {
		
			hintergrund = new JPanel();
			getContentPane().add(hintergrund);
			//Das letzte fünftel des Fensters ist für Spielermeldungen
			hintergrund.setBounds(0, 0, this.getWidth(), this.getHeight());
			hintergrund.setVisible(true);
			hintergrund.setLayout(null);
			
			//-------------------------------------------------------------hintergrund
			
			//Breite der Felder 
			int breite = hintergrund.getWidth() / 3;
			//Höhe der Felder
			int hoehe = hintergrund.getHeight() / 3;
			
			//Karten auf dem Tisch
			tisch = new Tisch();
			hintergrund.add(tisch);
			//Der Hintergrund wird + -förmig aufgeteilt
			tisch.setBounds(breite, hoehe, breite, hoehe);
			tisch.setVisible(true);
			
			gegenspielerKarten = new Gegenspieler[3];
			
			for(int i = 0; i < gegenspielerKarten.length; i++) {
				//Name muss noch gesetzt werden
				gegenspielerKarten[i] = new Gegenspieler();
				gegenspielerKarten[i].setVisible(true);
			}
			
			hintergrund.add(gegenspielerKarten[0]);
			gegenspielerKarten[0].setBounds(0, hoehe, breite, hoehe);
			
			gegenspielerKarten[0].nachricht("Spieler 1");
			
			hintergrund.add(gegenspielerKarten[1]);
			gegenspielerKarten[1].setBounds(breite, 0, breite, hoehe);
			
			gegenspielerKarten[1].nachricht("Spieler 2");
			
			hintergrund.add(gegenspielerKarten[2]);
			gegenspielerKarten[2].setBounds(2 * breite, hoehe, breite, hoehe);
			
			gegenspielerKarten[2].nachricht("Spieler 3");
			
			//Anzeige der Karten der Spieler
			spielerKarten = new Spieler();
			hintergrund.add(spielerKarten);
			//Unterhalb der eigenen Meldungen platziert
			spielerKarten.setBounds(breite, hoehe*2 + 80, breite, hoehe);
			spielerKarten.setVisible(true);
			
			//Meldungen des Spielers (10 Meldungen werden gebuffert)
			spielerMeldungen = new Meldungen(4);
			hintergrund.add(spielerMeldungen);
			//Die Meldungen laufen im letzten Fünftel des Fensters
			spielerMeldungen.setBounds(breite, hoehe*2, breite, hoehe);
			spielerMeldungen.setVisible(true);
			//erste Ausgabe
			spielerMeldungen.nachricht("Mit Server verbunden");
			
			//-------------------------------------------------------------hintergrund
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * aktualisiert das Model
	 */
	public void update(ModelMVC model) {
		this.model = model;	
		//Extrahiert das Model aus dem MVC-Wrapper
		Model m = this.model.gibModel();
		
		//aktualiseren der Anzeige
		spielerKarten.update(m.gibSpielerKarten(ID));
		
		tisch.setzeKarten(m.gibTisch());
		
		for(int i = 0; i < 3; i++) {
			//ermittelt die ID des Spielers
			int spielerID = ID + 1 + i;
			spielerID %= 4;
			gegenspielerKarten[i].entferneKarte(m.gibSpielerKarten(spielerID).size());
		}
	}
	
	/**
	 * Setzt die ID des Spielers, damit die Daten aus dem Model ausgelesen werden können
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
		//setzt die Namen, da nun die ID des lokalen Spielers bekannt ist
		mitspieler();
	}
	
	/**
	 * Setzt die Namen der Spieler, ->nachdem<- eine ID vergeben wurde
	 * @param namen
	 */
	public void setzeNamen(String[] namen) {
		//speichert die Namen der Mitspieler, die angezeigt werden, sobald die ID verfügbar ist
		this.namen = namen;
	}
	
	/**
	 * Setzt die Namen der Mitspieler
	 */
	private void mitspieler() {
		//setzt alle Meldungen des Spielers zurück
		spielerMeldungen.reset();
		spielerMeldungen.festeAnzeige(namen[ID]);
		
		int start = ID;
		
		for(int i = 0; i < 3; i++) {
			//Der Spieler links vom vorherigen Spieler
			start++;
			//Nach Nr. 3 wird neu begonnen
			start %= 4;
			
			//Gegenspieler den richtigen Namen zuweisen
			gegenspielerKarten[i].name(namen[start]);
		}
	}
	
	/**
	 * Gibt eine Nachricht an einen Spieler aus
	 * @param spielerID
	 * @param text
	 */
	private void nachricht(int spielerID, String text) {
		if(spielerID == ID)
			spielerMeldungen.nachricht(text);
		else {
			int start = ID;
			
			for(int i = 0; i < 3; i++) {
				//Der Spieler links vom vorherigen Spieler
				start++;
				//Nach Nr. 3 wird neu begonnen
				start %= 4;
				
				//Gegenspieler den richtigen Namen zuweisen
				gegenspielerKarten[i].nachricht(text);
			}
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
		//Wenn die spielenden verloren haben
		if(s1 > 9) {
			for(int i = 0; i < 4; i++) {
				if(i == s1)
					nachricht(i, "Zefix!");
				else if(i == s2)
					nachricht(i, "Wos soll na des!");
				else {		 
					nachricht(i, "So konns geh!");
				}
			}
		} else { //Ansonsten
			nachricht(s1, "Jawooooohl!");
			if(s2 != 4)
				nachricht(s2, "Ja geht doch!");
		}
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
		 nachricht(spielt, "I spül!");
		 if(mitspieler != 4) 
			 nachricht(mitspieler, "Und i ho g'heirat");
	}

	/**
	 * Zeigt an, wer Kontra gegeben hat
	 * @param kontra
	 */
	public void kontra(boolean[] kontra) {
		for(int i = 0; i < 4; i++) {
			if(kontra[i])
				nachricht(i, "Kontra!");
		}
	}

	/**
	 * Zeigt an, wer geklopft hat
	 * @param geklopft
	 */
	public void geklopft(boolean[] geklopft) {
		for(int i = 0; i < 4; i++) {
			if(geklopft[i]) 
				nachricht(i, "[Klopf] [Klopf]");
		}
	}
}

