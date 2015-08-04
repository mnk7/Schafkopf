package client;

import javax.swing.SwingUtilities;

import lib.Karte;
import lib.Model;
import lib.Model.modus;
import graphik.Graphik;
import graphik.Menu;
import graphik.MenuGUI;
import graphik.View;

public class Client {

	//Enthält die Verbindung zum Server
	private Netzwerk netzwerk;
	
	//Hört auf Befehle des Servers
	private Thread listener;
	
	//IP des Servers
	private String IP;
	
	//erstellt eine GUI
	private View graphik;
	
	private Menu menu;
	 
	//Erstellt ein Model
	private Model model;
	
	//Modus des Spiels speichern
	private modus mod;
	
	//Name des Spielers
	private String name;
	//ID des Spielers
	private int ID;
	
	private boolean beenden;
	
	
	public Client(String IP, String name, Menu menu) throws Exception {				
		this.IP = IP;
		this.name = name;
		ID = -1;
		
		this.menu = menu;
		
		beenden = false;
		
		netzwerk = new Netzwerk(ID, IP);
		
		//Vorerst leeres Model erzeugen
		model = new Model();
		
		listener = new Thread() {
			public void run() {
				listen();
			}
		};
		listener.setName("Schafkopf-Client");
		listener.start();
	}
	
	/**
	 * Empfängt Daten vom Server 
	 */
	private void listen() {
		while(!beenden) {
			try {				
				Object[] data = netzwerk.read();

				switch(data[0].toString()) {
				case "!NAME":
					name(data);
					continue;
				case "!MITSPIELER":
					mitspieler(data);
					continue;
				case "!ERSTE3":
					erste3(data);
					continue;
				case "!SPIEL":
					spiel(data);
					continue;
				case "!SPIELSTDU":
					spielstdu(data);
					continue;
				case "!MODUS":
					modus(data);
					continue;
				case "!BESTESSPIEL":
					bestesspiel(data);
					continue;
				case "!SPIELT":
					spielt(data);
					continue;
				case "!SIEGER":
					sieger(data);
					continue;
				case "!ID":
					id(data);
					continue;
				case "!HOCHZEIT":
					hochzeit(data);
					continue;
				case "!KONTRA":
					kontra(data);
					continue;
				case "!GEKLOPFT":
					geklopft(data);
					continue;
				case "!UPDATE":
					graphikUpdate((Model) data[1]);
					continue;
				case "!DRAN":
					weristdran(Integer.parseInt(data[1].toString()));
					continue;
				case "!ENDE":
					abschliessen(Integer.parseInt(data[1].toString()), Integer.parseInt(data[2].toString()));
					continue;
				case "!KONTO":
					konto(Integer.parseInt(data[1].toString()), Integer.parseInt(data[2].toString()));
					continue;
				case "!BEENDEN":
					beenden();
					continue;
				}
			} catch (Exception e) {
				//Wenn ein Fehler auftritt aus der Schleife ausbrechen
				e.printStackTrace();
				beenden = true;
				abmelden();
			}
		}
	}
	
	public void graphik(View view) {
		graphik = view;
	}
	
	private void bestesspiel(Object[] data) {
		//zeigt dem Spieler an, welches Spiel das bisher höchste ist
		graphik.bestesspiel(modus.valueOf(data[1].toString()));
	}

	private synchronized void name(Object[] data) throws Exception {
		//Senden des Namens
		netzwerk.print("!NAME", name);
	}
	
	private synchronized void mitspieler(Object[] data) {
		//Namen der Mitspieler empfangen
		String[] namen = new String[4];
		for(int i = 0; i < 4; i++) {
			namen[i] = data[i+1].toString();
		}
		//Speichert die Namen der Mitspieler
		graphik.setzeNamen(namen);
	}
	
	private synchronized void erste3(Object[] data) throws Exception {
		//Model empfangen
		graphikUpdate((Model) data[1]);
		//Klopfen des Spielers abwarten
		netzwerk.print("!ERSTE3", graphik.klopfstDu());
	}
	
	private synchronized void spiel(Object[] data) throws Exception {
		//Model empfangen
		graphikUpdate((Model) data[1]);
		graphik.spiel();
	}
	
	/**
	 * Wird aufgerufen, wenn eine Karte gespielt wurde
	 * @param model
	 * @throws Exception
	 */
	public void gespielt(Model model) throws Exception {
		netzwerk.printModel("!SPIEL", model);
	}
	
	private synchronized void spielstdu(Object[] data) throws Exception {
		//empfängt das neue Model
		graphikUpdate((Model) data[1]);
		//Sendet den Spielmodus
		String antwort = graphik.spielstDu().toString();
		netzwerk.print("!SPIELSTDU", antwort);
		
		//Wenn eine Hochzeit gespielt werden soll, wird die angebotene Karte gesendet
		if(antwort.equals("HOCHZEIT")) {
			graphik.hochzeitKarte();
		}
	}
	
	public void hochzeitKarteGespielt(Karte angebot) throws Exception {
		String[] output = new String[] {
			angebot.gibFarbe().toString(), 
			angebot.gibWert().toString()
		};
		netzwerk.print("!KARTE", output);
	}
	
	private synchronized void modus(Object[] data) throws Exception {
		//Empfangen des Modus des Spiels
		mod = modus.valueOf(data[1].toString());
		graphik.setzeModus(mod);
		netzwerk.print("!KONTRA", String.valueOf(graphik.kontra()));
	}
	
	private synchronized void spielt(Object[] data) {
		int spielt = Integer.parseInt(data[1].toString());
		int mitspieler = Integer.parseInt(data[2].toString());
		graphik.spielt(spielt, mitspieler);
	}
	
	private synchronized void sieger(Object[] data) {
		//empfängt die Sieger
		int s1 = Integer.parseInt(data[1].toString());
		int s2 = Integer.parseInt(data[2].toString());

		//und gibt sie an die Graphik weiter
		graphik.sieger(s1, s2);
	}
	
	private synchronized void id(Object[] data) {
		//ID des Spielers empfangen
		ID = Integer.parseInt(data[1].toString());
		
		netzwerk.setID(ID);
		graphik.setID(ID);
	}
	
	private synchronized void hochzeit(Object[] data) throws Exception {
		String antwort = graphik.hochzeit();
		
		if(antwort.equals("JA")) {
			netzwerk.print("!HOCHZEIT", antwort);
			graphik.hochzeitKarte();
		} else {
			netzwerk.print("!HOCHZEIT", antwort);
		}
	}
	
	private synchronized void kontra(Object[] data) {
		boolean[] kontra = new boolean[4];
		//data[0] enthält das Flag
		for(int i = 1; i < 5; i++) {
			if(data[i].toString().equals("JA")) {
				kontra[i - 1] = true;
			} else {
				kontra[i - 1] = false;
			}
		}
		graphik.kontra(kontra);
	}
	
	private synchronized void geklopft(Object[] data) {
		boolean[] geklopft = new boolean[4];
		//data[0] enthält das Flag
		for(int i = 1; i < 5; i++) {
			if(data[i].toString().equals("JA")) {
				geklopft[i - 1] = true;
			} else {
				geklopft[i - 1] = false;
			}
		}
		graphik.geklopft(geklopft);
	}
	
	private synchronized void abschliessen(int konto, int stock) {
		graphik.konto(konto, stock);
		model = new Model();
		graphik.setModel(model);
	}
	
	private synchronized void konto(int konto, int stock) {
		graphik.konto(konto, stock);
	}
	
	/**
	 * Aktualisiert die Graphik
	 * @param m
	 */
	private void graphikUpdate(Model m) {
		graphik.setModel(m);
	}
	
	/**
	 * Zeigt an, wer dran ist
	 * @param ID
	 */
	private void weristdran(int ID) {
		graphik.weristdran(ID);
	}
	
	public void beenden() {
		beenden = true;
		netzwerk.beenden();
		netzwerk = null;
		try {
			graphik.beenden();
			graphik = null;
		} catch(NullPointerException e) {
			//Dann eben nicht.
		}
		menu.beenden();
	}
	
	public void abmelden() {
		try {
			netzwerk.print("!BEENDEN", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		beenden();
	}

	public void dispose() {
		graphik.beenden();
		beenden = true;
		netzwerk = null;
	}
}
