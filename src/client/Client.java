package client;

import lib.Karte;
import lib.Model;
import lib.Model.modus;
import graphik.Graphik;
import graphik.MenuGUI;

public class Client implements View{

	//Enthält die Verbindung zum Server
	private Netzwerk netzwerk;
	
	//Hört auf Befehle des Servers
	private Thread listener;
	
	//IP des Servers
	private String IP;
	
	//erstellt eine GUI
	private Graphik graphik;
	
	private MenuGUI menu;
	 
	//Erstellt ein Model
	private ModelMVC model;
	
	//Modus des Spiels speichern
	private modus mod;
	
	//Name des Spielers
	private String name;
	//ID des Spielers
	private int ID;
	
	private boolean beenden;
	
	private boolean update;
	
	
	public Client(String IP, String name, MenuGUI menu) throws Exception{		
		model = new ModelMVC();
		model.addBeobachter(this);
		
		this.IP = IP;
		this.name = name;
		ID = -1;
		
		this.menu = menu;
		
		beenden = false;
		
		update = false;
		
		netzwerk = new Netzwerk(ID, IP);
		
		listener = new Thread() {
			public void run() {
				listen();
			}
		};
		listener.setName("Schafkopf-Client");
		
		listener.start();
		
		graphik = new Graphik(model, this);
		model.addBeobachter(graphik);
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
	
	private void name(Object[] data) throws Exception {
		//Senden des Namens
		netzwerk.print("!NAME", name);
	}
	
	private void mitspieler(Object[] data) {
		//Namen der Mitspieler empfangen
		String[] namen = new String[4];
		for(int i = 0; i < 4; i++) {
			namen[i] = data[i+1].toString();
		}
		//Speichert die Namen der Mitspieler
		graphik.setzeNamen(namen);
	}
	
	private void erste3(Object[] data) throws Exception {
		//Model empfangen
		model.setzeModel((Model) data[1]);
		//Klopfen des Spielers abwarten
		netzwerk.print("!ERSTE3", graphik.klopfstDu());
	}
	
	private void spiel(Object[] data) throws Exception {
		//Model empfangen
		model.setzeModel((Model) data[1]);
		//Signal an Graphik
		graphik.spiel();
		
		while(!update) {
			Thread.sleep(500);
		}
		netzwerk.printModel("!SPIEL", model.gibModel());
		update = false;
	}
	
	private void spielstdu(Object[] data) throws Exception {
		//empfängt das neue Model
		model.setzeModel((Model) data[1]);
		//Sendet den Spielmodus
		String antwort = graphik.spielstDu().toString();
		netzwerk.print("!SPIELSTDU", antwort);
		
		//Wenn eine Hochzeit gespielt werden soll, wird die angebotene Karte gesendet
		if(antwort.equals("HOCHZEIT")) {
			Karte k = graphik.hochzeitKarte();
			String[] output = new String[] {
				k.gibFarbe().toString(), 
				k.gibWert().toString()
			};
			netzwerk.print("!KARTE", output);
		}
	}
	
	private void modus(Object[] data) throws Exception {
		//Empfangen des Modus des Spiels
		mod = modus.valueOf(data[1].toString());
		netzwerk.print("!KONTRA", String.valueOf(graphik.kontra()));
	}
	
	private void spielt(Object[] data) {
		int spielt = (int) data[1];
		int mitspieler = (int) data[2];
		graphik.spielt(spielt, mitspieler);
	}
	
	private void sieger(Object[] data) {
		//empfängt die Sieger
		int s1 = (int) data[1];
		int s2 = (int) data[2];

		//und gibt sie an die Graphik weiter
		graphik.sieger(s1, s2);
	}
	
	private void id(Object[] data) {
		//ID des Spielers empfangen
		ID = Integer.parseInt(data[1].toString());
		
		netzwerk.setID(ID);
		graphik.setID(ID);
	}
	
	private void hochzeit(Object[] data) throws Exception {
		String antwort = graphik.hochzeit();
		
		if(antwort.equals("JA")) {
			netzwerk.print("!HOCHZEIT", antwort);
			Karte k = graphik.hochzeitKarte();
			String[] output = new String[] {
				k.gibFarbe().toString(), 
				k.gibWert().toString()
			};
			netzwerk.print("!KARTE", output);
		} else {
			netzwerk.print("!HOCHZEIT", antwort);
		}
	}
	
	private void kontra(Object[] data) {
		boolean[] kontra = new boolean[4];
		//data[0] enthält den Steuerbefehl
		for(int i = 1; i < 5; i++) {
			if(data[i].toString().equals("true"))
				kontra[i] = true;
			else 
				kontra[i] = false;
		}
		graphik.kontra(kontra);
	}
	
	private void geklopft(Object[] data) {
		boolean[] geklopft = new boolean[4];
		//data[0] enthält den Steuerbefehl
		for(int i = 1; i < 5; i++) {
			if(data[i].toString().equals("true"))
				geklopft[i - 1] = true;
			else
				geklopft[i - 1] = false;
		}
		graphik.geklopft(geklopft);
	}

	/**
	 * Aktualisiert das Model
	 */
	public synchronized void update(ModelMVC model) throws Exception {
		this.model = model;
		update = true;
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

}
