package server;

import ki.Hochzeit;
import ki.KI;
import ki.Spielauswahl;
import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Bot implements Spieler {
	
	private int ID;
	private String name;
	private Model model;
	//wird nur benutzt, wenn eine Hochzeit durchgeführt wird
	private Karte karte;
	
	private int spielt;
	private int mitspieler;
	
	private int wartezeit;
	private int handicap;
	
	private KI ki;
	private Spielauswahl spielauswahl;
	private Server server;
	
	public Bot(Server server, int botnr, Spielauswahl spielauswahl, int wartezeit, int handicap) {
		name = "[BOT]-" + botnr;
		
		this.spielauswahl = spielauswahl;
		this.server = server;
		
		spielt = -1;
		mitspieler = -1;
		
		this.wartezeit = wartezeit;
		this.handicap = handicap;
	}

	public boolean erste3(Model model) {
		setzeModel(model);
		return spielauswahl.klopfen(model, ID);
	} 

	public synchronized void spielen(Model model) {
		setzeModel(model);
	}
	
	public synchronized modus spielstDu(Model model, modus m) {
		setzeModel(model);
		modus modus = spielauswahl.wasSpielen(model, ID);
		
		//Darf das gespielt werden?
		if(new regeln.Regelwahl().darfGespieltWerden(modus, model, ID, spielauswahl.gibFarbe())) {
			if(modus.equals(modus.HOCHZEIT)) {
				karte = Hochzeit.hochzeitVorschlagen(model, ID);
			}
			return modus;
		} else {
			return modus.NICHTS;
		}
	}

	public synchronized boolean modus(lib.Model.modus m) {
		ki = spielauswahl.gibKI(m, ID, handicap);
		ki.spieler(spielt, mitspieler);
		return ki.kontra(model);
	} 

	public void sieger(int s1, int s2) {
		//Der Sieger wird festgestellt
		ki.sieger(s1, s2);
	}
	
	public String gibIP() {
		return "local";
	}

	public String gibName() {
		return name;
	}

	public synchronized void setzeID(int ID) {
		this.ID = ID;
		try {
			ki.setzeID(ID);
		} catch(NullPointerException npe) {
			//ki noch nicht initialisiert
		}
	}

	public synchronized Model gibModel() {
		try {
			//Warten, damit das Spiel ein wenig verzögert wird
			Thread.sleep(wartezeit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ki.spiel(model);
	}

	public synchronized Karte gibKarte() {
		return karte;
	}

	public synchronized boolean hochzeit() {
		Hochzeit hochzeit = new Hochzeit(ID, handicap);
		karte = hochzeit.hochzeitAnnehmen(model, ID);
		if(karte == null) {
			return false;
		} else {
			return true;
		}
	}

	public void spieler(int spielt, int mitspieler) throws Exception {
		//Der Server sendet, wer spielt
		this.spielt = spielt;
		this.mitspieler = mitspieler;
	}

	public void geklopft(boolean[] geklopft) throws Exception {
		//Der Server gibt an, wer geklopft hat (CLIENT)
	}

	public void kontra(boolean[] kontra) throws Exception {
		//Der Server gibt an, wer Kontra gegeben hat (CLIENT)
	}

	public synchronized void setzeModel(Model model) {
		this.model = model;
	}

	public void name() {
		//Der Server weist den Spieler an einen Namen einzugeben (CLIENT)
	}
	
	public void stich(Model model) {
		ki.stich(model);
	}

	public void beenden() {
		//Server beendet das Spiel
		server.entferneSpieler(this);
	}

	public void abmelden() {
		//Server beendet das Spiel (CLIENT)
		beenden();
	}

	public void rundeZuende(int kontostand, int stock) {
	}

	public void konto(int kontostand, int stock) {
	}

	public void update(Model model) {
		setzeModel(model);
	}
	
	public void weristdran(int ID) {
		//Server sendet, wer dran ist (CLIENT)
	}
}
