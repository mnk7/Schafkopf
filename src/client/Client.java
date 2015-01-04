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
	 
	//Erstellt ein Model
	private ModelMVC model;
	
	//Modus des Spiels speichern
	private modus mod;
	
	//Name des Spielers
	private String name;
	//ID des Spielers
	private int ID;
	
	private boolean update;
	
	
	public Client(String IP, String name, MenuGUI menu) throws Exception{		
		model = new ModelMVC();
		model.addBeobachter(this);
		
		this.IP = IP;
		this.name = name;
		ID = -1;
		
		update = false;
		
		netzwerk = new Netzwerk(ID, IP);
		
		listener = new Thread() {
			public void run() {
				listen();
			}
		};
		
		listener.start();
		
		graphik = new Graphik(model);
		model.addBeobachter(graphik);
		
		//Menü wieder sichtbar machen
		menu.setVisible(true);
	}
	
	/**
	 * Empfängt Daten vom Server 
	 */
	private synchronized void listen() {
		while(true) {
			try {				
				Object[] data = netzwerk.read();

				if(data[0].equals("!NAME")) {
					name(data);
					continue;
				} 
				if(data[0].equals("!MITSPIELER")) {
					mitspieler(data);
					continue;
				} 
				if(data[0].equals("!ERSTE3")) {
					erste3(data);
					continue;
				}
				if(data[0].equals("!SPIEL")) {
					spiel(data);
					continue;
				} 
				if(data[0].equals("!SPIELSTDU")) {
					spielstdu(data);
					continue;
				}
				if(data[0].equals("!MODUS")) {
					modus(data);
					continue;
				} 
				if(data[0].equals("!SPIELT")) {
					spielt(data);
					continue;
				} 
				if(data[0].equals("!SIEGER")) {
					sieger(data);
					continue;
				} 
				if(data[0].equals("!ID")) {
					id(data);
					continue;
				} 
				if(data[0].equals("!HOCHZEIT")) {
					hochzeit(data);
					continue;
				} 
				if(data[0].equals("!KONTRA")) {
					kontra(data);
					continue;
				} 
				if(data[0].equals("!GEKLOPFT")) {
					geklopft(data);
					continue;
				}
			} catch (Exception e) {
				//Wenn ein Fehler auftritt aus der Schleife ausbrechen
				e.printStackTrace();
				break;
			}
		}
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
		model.setzeModel((Model) data[1]);
		//Klopfen des Spielers abwarten
		netzwerk.print("!ERSTE3", graphik.klopfstDu());
	}
	
	private synchronized void spiel(Object[] data) throws Exception {
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
	
	private synchronized void spielstdu(Object[] data) throws Exception {
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
	
	private synchronized void modus(Object[] data) throws Exception {
		//Empfangen des Modus des Spiels
		mod = modus.valueOf(data[1].toString());
		netzwerk.print("!KONTRA", String.valueOf(graphik.kontra()));
	}
	
	private synchronized void spielt(Object[] data) {
		int spielt = (int) data[1];
		int mitspieler = (int) data[2];
		graphik.spielt(spielt, mitspieler);
	}
	
	private synchronized void sieger(Object[] data) {
		//empfängt die Sieger
		int s1 = (int) data[1];
		int s2 = (int) data[2];

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
	
	private synchronized void kontra(Object[] data) {
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
	
	private synchronized void geklopft(Object[] data) {
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

}
