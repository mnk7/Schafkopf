package client;

import lib.Karte;
import lib.Model.modus;

import graphik.Graphik;

public class Client implements View {

	//Enthält die Verbindung zum Server
	private Netzwerk netzwerk;
	
	//Hört auf Befehle des Servers
	private Thread thread;
	
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
	
	//gibt an, ob weitergespielt wird
	private boolean nocheins;
	
	
	public Client(String IP, String name) throws Exception{
		graphik = new Graphik();
		
		model = new ModelMVC();
		
		this.IP = IP;
		this.name = name;
		ID = -1;
		
		nocheins = true;
		
		if(!connect()) throw new Exception("Verbindung gescheitert");
		
		//Senden des Namens
		netzwerk.send(name);
		
		thread = new Thread() {
			public void run() {
				listen();
			}
		};
		
		thread.start();
	}
	
	/**
	 * Empfänt Daten vom Server
	 */
	private void listen() {
		while(true) {
			try {
				String steuerung = netzwerk.einlesen();
				
				switch (steuerung) {
				case "!ERSTE3":
					//Model empfangen
					model.setzeModel(netzwerk.empfangen());
					//Klopfen des Spielers abwarten
					netzwerk.send("!ERSTE3");
					netzwerk.send(String.valueOf(graphik.klopfstDu()));
					break;
				case "!SPIEL":
					//Model empfangen
					model.setzeModel(netzwerk.empfangen());
					//Signal an Graphik
					netzwerk.send("!SPIEL");
					graphik.spiel();
					break;
				case "!SPIELSTDU":
					//empfängt das neue Model
					model.setzeModel(netzwerk.empfangen());
					//Sendet den Spielmodus
					netzwerk.send("!SPIELSTDU");
					String antwort = graphik.spielstDu().toString();
					netzwerk.send(antwort);
					if(antwort.equals("HOCHZEIT")) {
						netzwerk.send("!KARTE");
						Karte k = graphik.hochzeitKarte();
						netzwerk.send(k.gibFarbe().toString());
						netzwerk.send(k.gibWert().toString());
					}
					break;
				case "!MODUS":
					//Empfangen des Modus des Spiels
					mod = modus.valueOf(netzwerk.einlesen());
					break;
				case "!SPIELT":
					int spielt = Integer.parseInt(netzwerk.einlesen());
					graphik.spielt(spielt);
					break;
				case "!SIEGER":
					//empfängt die Sieger
					int s1 = Integer.parseInt(netzwerk.einlesen());
					int s2 = Integer.parseInt(netzwerk.einlesen());

					//und gibt sie an die Graphik weiter
					graphik.sieger(s1, s2);
					break;
				case "!ID":
					//ID des Spielers empfangen
					ID = Integer.parseInt(netzwerk.einlesen());
					netzwerk.setID(ID);
					break;
				case "!HOCHZEIT" :
					String answer = graphik.hochzeit();
					
					if(answer.equals("JA")) {
						netzwerk.send(answer);
						netzwerk.send("!KARTE");
						Karte k = graphik.hochzeitKarte();
						netzwerk.send(k.gibFarbe().toString());
						netzwerk.send(k.gibWert().toString());
					}
					break;
				}
				
				//kleine Pause
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update(ModelMVC model) {
		this.model = model;
		try {
			netzwerk.senden(model.gibModel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Erstellt die Verbindung zum Server
	 */
	public boolean connect() {
		try {
			//Verbindet sich mit dem Server mit falscher Spielernummer
			netzwerk = new Netzwerk(ID, IP);
			return true;
		} catch(Exception e) {
		}
		
		//falls keine Verbindung hergestellt werden konnte
		return false;
	}

}
