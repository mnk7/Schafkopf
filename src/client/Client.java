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
		
		if(!connect()) throw new Exception("Verbindung gescheitert");
		
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
					//Senden des Namens
					netzwerk.print("!NAME", name);
					break;
				} 
				if(data[0].equals("!MITSPIELER")) {
					//Namen der Mitspieler empfangen
					String[] namen = new String[4];
					for(int i = 0; i < 4; i++) {
						namen[i] = (String) data[i+1];
					}
					//Speichert die Namen der Mitspieler
					graphik.setzeNamen(namen);
					break;
				} 
				if(data[0].equals("!ERSTE3")) {
					//Model empfangen
					model.setzeModel((Model) data[1]);
					//Klopfen des Spielers abwarten
					netzwerk.print("!ERSTE3", String.valueOf(graphik.klopfstDu()));
					break;
				}
				if(data[0].equals("!SPIEL")) {
					//Model empfangen
					model.setzeModel((Model) data[1]);
					//Signal an Graphik
					graphik.spiel();
//Untersuchen!!
					while(!update) {
						Thread.sleep(500);
					}
					netzwerk.printModel("!SPIEL", model.gibModel());
					update = false;
					
					break;
				} 
				if(data[0].equals("!SPIELSTDU")) {
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
					
					break;
				}
				if(data[0].equals("!MODUS")) {
					//Empfangen des Modus des Spiels
					mod = modus.valueOf((String) data[1]);
					netzwerk.print("!KONTRA", String.valueOf(graphik.kontra()));
					break;
				} 
				if(data[0].equals("!SPIELT")) {
					int spielt = (int) data[1];
					int mitspieler = (int) data[2];
					graphik.spielt(spielt, mitspieler);
					break;
				} 
				if(data[0].equals("!SIEGER")) {
					//empfängt die Sieger
					int s1 = (int) data[1];
					int s2 = (int) data[2];

					//und gibt sie an die Graphik weiter
					graphik.sieger(s1, s2);
					break;
				} 
				if(data[0].equals("!ID")) {
					//ID des Spielers empfangen
					ID = (int) data[1];
					
					netzwerk.setID(ID);
					graphik.setID(ID);
					break;
				} 
				if(data[0].equals("!HOCHZEIT")) {
					String answer = graphik.hochzeit();
					
					if(answer.equals("JA")) {
						netzwerk.print("!HOCHZEIT", answer);
						Karte k = graphik.hochzeitKarte();
						String[] output = new String[] {
							k.gibFarbe().toString(), 
							k.gibWert().toString()
						};
						netzwerk.print("!KARTE", output);
					} else {
						netzwerk.print("!HOCHZEIT", answer);
					}
					break;
				} 
				if(data[0].equals("!KONTRA")) {
					boolean[] kontra = new boolean[4];
					for(int i = 0; i < 4; i++) {
						if(data[1].equals("true"))
							kontra[i] = true;
						else 
							kontra[i] = false;
					}
					graphik.kontra(kontra);
					break;
				} 
				if(data[0].equals("!GEKLOPFT")) {
					boolean[] geklopft = new boolean[4];
					for(int i = 0; i < 4; i++) {
						if(data[1].equals("true"))
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

	/**
	 * Aktualisiert das Model
	 */
	public synchronized void update(ModelMVC model) throws Exception {
		this.model = model;
		update = true;
	}

}
