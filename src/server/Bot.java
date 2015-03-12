package server;

import ki.Hochzeit;
import ki.KI;
import ki.Spielauswahl;
import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Bot implements Spieler {
	
	private int ID;
	private Model model;
	private int kontostand;
	//wird nur benutzt, wenn eine Hochzeit durchgeführt wird
	private Karte karte;
	
	private int spielt;
	private int mitspieler;
	
	private KI ki;
	private Spielauswahl spielauswahl;
	private Server server;
	
	public Bot(Server server) {
		spielauswahl = new Spielauswahl();
		this.server = server;
		
		spielt = -1;
		mitspieler = -1;
	}

	public boolean erste3(Model model) {
		return spielauswahl.klopfen(model);
	} 

	public void spielen(Model model) {
		model = ki.spiel(model);
	}
	
	public modus spielstDu(Model model, modus m) {
		modus modus = spielauswahl.wasSpielen(model);
		if(modus.equals(modus.HOCHZEIT)) {
			karte = Hochzeit.hochzeitVorschlagen(model, ID);
		}
		return modus;
	}

	public boolean modus(lib.Model.modus m) {
		ki = spielauswahl.gibKI(m, ID);
		ki.spieler(spielt, mitspieler);
		return ki.kontra(model);
	} 

	public void sieger(int s1, int s2) {
		//Der Sieger wird festgestellt
	}
	
	public String gibIP() {
		return "local";
	}

	public String gibName() {
		return "[BOT]";
	}

	public void setzeID(int ID) {
		this.ID = ID;
	}

	public Model gibModel() {
		return model;
	}

	public Karte gibKarte() {
		return karte;
	}

	public boolean hochzeit() {
		karte = Hochzeit.hochzeitAnnehmen(model, ID);
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

	public void setzeModel(Model model) {
		this.model = model;
	}

	public void name() {
		//Der Server weist den Spieler an einen Namen einzugeben (CLIENT)
	}

	public void beenden() {
		//Server beendet das Spiel
		server.entferneSpieler(this);
	}

	public void abmelden() {
		//Server beendet das Spiel (CLIENT)
		beenden();
	}

	public void rundeZuende(int kontostand) {
		this.kontostand = kontostand;
	}

	public void konto(int kontostand) {
		this.kontostand = kontostand;
	}

	public void update(Model model) {
		setzeModel(model);
	}
}
