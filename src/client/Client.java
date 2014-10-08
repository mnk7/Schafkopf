package client;

import lib.Karte;
import lib.Model.modus;

import graphik.Graphik;
import graphik.MenuGUI;

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
	
	
	public Client(String IP, String name, MenuGUI menu) throws Exception{		
		model = new ModelMVC();
		
		this.IP = IP;
		this.name = name;
		ID = -1;
		
		nocheins = true;
		
		if(!connect()) throw new Exception("Verbindung gescheitert");
		
		thread = new Thread() {
			public void run() {
				listen();
			}
		};
		
		thread.start();
		
		graphik = new Graphik();
		
		//Menü wieder sichtbar machen
		menu.setVisible(true);
	}
	
	/**
	 * Empfängt Daten vom Server 
	 */
	private void listen() {
		while(true) {
			try {
				//kleine Pause
				Thread.sleep(100);
				
				String steuerung = netzwerk.einlesen();

				if(steuerung.equals("!NAME")) {
					//Senden des Namens
					netzwerk.send("!NAME");
					netzwerk.send(name);
					break;
				} if(steuerung.equals("!MITSPIELER")) {
					//Namen der Mitspieler empfangen
					String[] namen = new String[4];
					for(int i = 0; i < 4; i++) {
						namen[i] = netzwerk.einlesen();
					}
					//Speichert die Namen der Mitspieler
					graphik.setzeNamen(namen);
					break;
				} if(steuerung.equals("!ERSTE3")) {
					//Model empfangen
					model.setzeModel(netzwerk.empfangen());
					//Klopfen des Spielers abwarten
					netzwerk.send("!ERSTE3");
					netzwerk.send(String.valueOf(graphik.klopfstDu()));
					break;
				} if(steuerung.equals("!SPIEL")) {
					//Model empfangen
					model.setzeModel(netzwerk.empfangen());
					//Signal an Graphik
					netzwerk.send("!SPIEL");
					graphik.spiel();
					break;
				} if(steuerung.equals("!SPIELSTDU")) {
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
				} if(steuerung.equals("!MODUS")) {
					//Empfangen des Modus des Spiels
					mod = modus.valueOf(netzwerk.einlesen());
					break;
				} if(steuerung.equals("!SPIELT")) {
					int spielt = Integer.parseInt(netzwerk.einlesen());
					int mitspieler = Integer.parseInt(netzwerk.einlesen());
					graphik.spielt(spielt, mitspieler);
					break;
				} if(steuerung.equals("!SIEGER")) {
					//empfängt die Sieger
					int s1 = Integer.parseInt(netzwerk.einlesen());
					int s2 = Integer.parseInt(netzwerk.einlesen());

					//und gibt sie an die Graphik weiter
					graphik.sieger(s1, s2);
					break;
				} if(steuerung.equals("!ID")) {
					//ID des Spielers empfangen
					ID = Integer.parseInt(netzwerk.einlesen());
					
					netzwerk.setID(ID);
					graphik.setID(ID);
					break;
				} if(steuerung.equals("!HOCHZEIT")) {
					String answer = graphik.hochzeit();
					
					if(answer.equals("JA")) {
						netzwerk.send(answer);
						netzwerk.send("!KARTE");
						Karte k = graphik.hochzeitKarte();
						netzwerk.send(k.gibFarbe().toString());
						netzwerk.send(k.gibWert().toString());
					}
					break;
				} if(steuerung.equals("!KONTRA")) {
					boolean[] kontra = new boolean[4];
					for(int i = 0; i < 4; i++) {
						if(netzwerk.einlesen().equals("true"))
							kontra[i] = true;
						else 
							kontra[i] = false;
					}
					graphik.kontra(kontra);
					break;
				} if(steuerung.equals("!GEKLOPFT")) {
					boolean[] geklopft = new boolean[4];
					for(int i = 0; i < 4; i++) {
						if(netzwerk.einlesen().equals("true"))
							geklopft[i] = true;
						else
							geklopft[i] = false;
					}
					graphik.geklopft(geklopft);
					break;
				}
			} catch (Exception e) {
				//Wenn ein Fehler auftritt aus der Schleife ausbrechen
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void update(ModelMVC model) throws Exception {
		this.model = model;
		try {
			netzwerk.senden(model.gibModel());
		} catch (Exception e) {
			throw e;
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
			e.printStackTrace();
		}
		
		//falls keine Verbindung hergestellt werden konnte
		return false;
	}

}
