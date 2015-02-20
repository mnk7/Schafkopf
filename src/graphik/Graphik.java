package graphik;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import regeln.Control;
import regeln.Hochzeit;
import regeln.Regelwahl;
import lib.Karte;
import lib.Model;  
import lib.Model.modus;
import client.Client; 

import javax.swing.JPanel;

public class Graphik extends JFrame {	
	
	private String logo = "Logo.gif";
	private String hintergrundbild = "karten" + File.separator + "hintergrund1.jpg";
	
	private Model model;
	
	//Enthält die Controll -> kontrolliert einen Spielzug
	private Control control;
	
	private int ID;
	private String[] namen;
	
	private boolean keinKontra;
	
	//GUI
	//Karten des Spielers
	private Spieler spielerKarten;
	//Nachrichten des Clients
	private Meldungen spielerMeldungen;
	
	//Karten der Gegenspieler
	private Gegenspieler[] gegenspielerKarten;
	
	//Tisch
	private Tisch tisch;
	
	private SpielmodusDialog dialog;
	
	//Hintergrund
	private JLabel hintergrund;	
	
	public Graphik(Model model, final Client client) {
		super();
		//Vorerst keine ID setzen
		ID = -1;
		
		keinKontra = false;
		
		this.model = model;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setTitle("Schafkopf-App");
		//Icon der Anwendung setzen
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(logo));
		this.setIconImage(icon.getImage());
		
		this.setSize(1290, 700);
		//arrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrg
		this.setResizable(false);
		//Äußeres Layout nicht vorhanden
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.initGUI(); 
		this.setVisible(true);
		
		this.addWindowListener(new WindowListener() {

			public void windowClosing(WindowEvent arg0) {
			}
			public void windowActivated(WindowEvent e) {
			}
			public void windowClosed(WindowEvent e) {
				client.abmelden(); 
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowOpened(WindowEvent e) {
			}
		});
	}	
	
	/**
	 * Erstellt eine neue GUI
	 */
	private void initGUI() {
		try {
		
			hintergrund = new JLabel();
			getContentPane().add(hintergrund);
			//Das letzte fünftel des Fensters ist für Spielermeldungen
			hintergrund.setBounds(0, 0, this.getWidth(), this.getHeight());
			hintergrund.setIcon(new ImageIcon(getClass().getClassLoader().getResource(hintergrundbild)));
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
			tisch.kartenPosition();
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
			
			dialog = new SpielmodusDialog();
			hintergrund.add(dialog);
			dialog.setBounds(0, 0, 300, 300);
			dialog.setVisible(false);
			
			//-------------------------------------------------------------hintergrund
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Zeichnet das Fenster neu
	 */
	public void update() {
		setModel(model);
	}
	
	/**
	 * Aktualisiert das Model
	 * @param model
	 */
	public void setModel(Model model) {
		this.model = model;
		//aktualiseren der Anzeige
		spielerKarten.update(this.model.gibSpielerKarten(ID));
		
		//Passt die Karten auf dem Tisch an die Spieler an
		Karte[] gespielt = new Karte[4];
		gespielt[3] = this.model.gibTisch()[ID]; 
		for(int i = 0; i < 3; i++) {
			gespielt[i] = this.model.gibTisch()[(ID + 1 + i)%4];
		}
		
		tisch.setzeKarten(gespielt);
				
		for(int i = 0; i < 3; i++) {
			if(gespielt[i] == null) {
				gegenspielerKarten[i].update(this.model.gibSpielerKarten(ID).size() - 1);
			} else {
				gegenspielerKarten[i].update(this.model.gibSpielerKarten(ID).size() - 2);
			}
		}
	}
	
	/**
	 * Setzt die ID des Spielers, damit die Daten aus dem Model ausgelesen werden können
	 * @param ID
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	/**
	 * Setzt die Namen der Spieler, ->nachdem<- eine ID vergeben wurde
	 * @param namen
	 */
	public void setzeNamen(String[] namen) {
		//speichert die Namen der Mitspieler, die angezeigt werden, sobald die ID verfügbar ist
		this.namen = namen;
		//Wenn die Namen bekannt sind, werden sie angezeigt
		mitspieler();
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
		if(spielerID == ID) {
			spielerMeldungen.nachricht(text);
		} else {
			//Differenz zwischen eigener ID und der des anderen Spielers
			//alias wie weit sitzt dieser Spieler entfernt
			int nr = spielerID - ID;
			
			//Wenn nr negativ ist, "4 mal in die richtige Richtung" - "Schritte in die falsche Richtung"
			if(nr < 0) {
				nr += 4;
			}
			
			gegenspielerKarten[nr - 1].nachricht(text);
		}
	}
	
	/**
	 * Spielzug des Spielers durchführen
	 * @throws Exception 
	 */
	public Model spiel() throws Exception {
		this.toFront();
		//Nochmal Anzeige aktualisieren
		this.update();
		Karte gespielt = spielerKarten.spiel();
		boolean ok = false;
		
		do {
			spielerKarten.update(model.setTisch(ID, gespielt));
			if(control.erlaubt(model)) {
				ok = true;
			} else {
				model.undo(ID);
				JOptionPane.showMessageDialog(this, "Diese Karte ist nicht erlaubt");
				gespielt = spielerKarten.spiel();
			}
		} while(!ok);
		
		//aktualisiert die Anzeige
		this.update();
		
		return model;
	}
	
	/**
	 * Ändert den Modus des Spiels und ändert die Controll
	 * @param mod
	 */
	public void setzeModus(modus mod) {
		control = new Regelwahl().wahl(mod, model, ID);
		nachricht(ID, "Es wird ein " + mod.toString() + " gespielt");
	}
	
	public void bestesspiel(String mod) {
		nachricht(ID, "Bisher höchstes Spiel ist " + mod);
	}
	
	/**
	 * Fragt den Nutzer, ob er etwas spielen will und gibt den Spielmodus zurück
	 * @param model
	 * @return
	 */
	public modus spielstDu() {
		boolean fertig = false;
		this.toFront();
		dialog.setVisible(true);
		while(!fertig) {			
			modus m = dialog.modusWahl();
			
			//Prüft, ob die Karte erlaubt ist
			if(!new Regelwahl().darfGespieltWerden(m, model, ID, dialog.farbe(m))) {
				JOptionPane.showMessageDialog(this, "Das geht nicht!");
				dialog.reset();
				continue;
			}
			dialog.setVisible(false);
			dialog.reset();
			fertig = true;
			return m;
		}
		return modus.NICHTS;
	}
	
	/**
	 * Gibt zurück, ob der Spieler klopft
	 * @return
	 */
	public String klopfstDu() {
		if(javax.swing.JOptionPane.showConfirmDialog(this, "Willst du klopfen?") == 0) {
			return "JA";
		} else {
			return "NEIN";	
		}
	}
	
	/**
	 * Gibt zurück, ob der Spieler klopft
	 * @return
	 */
	public String kontra() {
		if(!keinKontra) {
			if(javax.swing.JOptionPane.showConfirmDialog(this, "Kontra?") == 0) {
				return "JA";
			} else {
				return "NEIN";	
			}
		} else {
			return "NEIN";
		}
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
				if(i == s1) {
					nachricht(i, "Zefix!");
				} else if(i == s2) {
					nachricht(i, "Oh nöö!");
				} else {		 
					nachricht(i, "Na toll.");
				}
			}
		} else { //Ansonsten
			nachricht(s1, "Jawooooohl!");
			if(s2 != 4) {
				nachricht(s2, "Ja geht doch!");
			}
		}
	}

	/**
	 * Gibt "JA" zurück, wenn der Spieler heiraten will
	 * @return
	 */
	public String hochzeit() {
		if(JOptionPane.showConfirmDialog(this, "Willst du heiraten?") == JOptionPane.OK_OPTION) {
			return "JA";
		}
		return "NEIN";
	}

	/**
	 * Gibt die Karte zurück, die der Spieler bei der Hochzeit tauschen will
	 * Wird aufgerufen, wenn jmd. eine Hochzeit anbietet und wenn eine angenommen
	 * wird
	 * @return
	 */
	public Karte hochzeitKarte() {
		JOptionPane.showMessageDialog(this, "Welche gibst du her?");
		return spielerKarten.spiel();
	}

	/**
	 * Empfängt, wer spielt. Wird keine Hochzeit gespielt ist mitspieler gleich 4
	 * @param spielt
	 * @param mitspieler 
	 */
	public void spielt(int spielt, int mitspieler) { 
		nachricht(spielt, "Ich spiel");
		if(mitspieler != 4) {
			nachricht(mitspieler, "Und ich hab geheiratet");
		}
		
		if(ID == spielt || ID == mitspieler) {
			keinKontra = true;
		} else {
			keinKontra = false;
		}
	}

	/**
	 * Zeigt an, wer Kontra gegeben hat
	 * @param kontra
	 */
	public void kontra(boolean[] kontra) {
		for(int i = 0; i < 4; i++) {
			if(kontra[i]) {
				nachricht(i, "Kontra!");
			}
		}
	}

	/**
	 * Zeigt an, wer geklopft hat
	 * @param geklopft
	 */
	public void geklopft(boolean[] geklopft) {
		for(int i = 0; i < 4; i++) {
			if(geklopft[i]) {
				nachricht(i, "[Klopf] [Klopf]");
			}
		}
	}
	
	public void konto(int kontostand) {
		nachricht(ID, "Kontostand: " + String.valueOf(kontostand));
	}
	
	public void beenden() {
		JOptionPane.showMessageDialog(null, "Der Server hat das Spiel beendet");
		this.dispose();
	}
}

