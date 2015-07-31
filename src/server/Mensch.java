/**
 * Repräsentiert serverseitig einen menschlichen Spieler. Übernimmt einen Port und horcht diesen ab.
 * 
 */

package server;

import java.net.Socket;
import java.util.HashMap;

import lib.Karte;
import lib.Model;
import lib.Model.modus;
import server.Netzwerk;

public class Mensch implements Spieler {
	
	private Netzwerk netzwerk;
	
	//Hält eine Referenz auf den Server, um diesen zu benachrichtigen, wenn
	//die Verbindung abgebrochen wurde
	private Server server;
	
	private String name;
	
	private final int wartezeit = 800;
	
	//speichert neueste antworten
	private HashMap<String, Object> antwort;
	private Model model;
	private Karte karte;

	private boolean modelupdate = false;
	
	private boolean beenden;
	
	private Thread spieler;
	
	public Mensch(Socket client, Server server) throws Exception {
		
		beenden = false;
		
		antwort = new HashMap<String, Object>();
		
		this.server = server;
		
		//Errichtet neue Verbindung
		netzwerk = new Netzwerk(client);
		
		spieler = new Thread() {
			  		  public void run() {
						  listen();
					  }
				  };
		spieler.start();
	}
	
	/**
	 * Horcht auf Befehle vom Client
	 */
	public void listen() {
		
		while(!beenden) {
			try {
				Object[] data = netzwerk.read();
				
				switch(data[0].toString()) {
				case "!NAME":
					name = data[1].toString();
					continue;
				case "!SPIELSTDU":
					antwort.put(data[0].toString(), data[1]);
					continue;
				case "!KONTRA":
					antwort.put(data[0].toString(), data[1]);
					continue;
				case "!ERSTE3":
					antwort.put(data[0].toString(), data[1]);
					continue;
				case "!SPIEL":
					model = (Model) data[1];
					modelupdate = true;
					continue;
				case "!HOCHZEIT":
					//Antwort: JA + Karte oder NEIN
					antwort.put(data[0].toString(), data[1]);
					continue;
				case "!KARTE":	
					karte = new Karte(
							Karte.farbe.valueOf(data[1].toString()),
							Karte.wert.valueOf(data[2].toString()));
					continue;
				case "!BEENDEN":
					beenden();
					continue;
				default: 
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				beenden = true;
				//Benachrichtige Server, dass ein Spieler entfernt wurde
				abmelden();
			}
		}
	}
	
	public synchronized String gibAntwort(String flag) {
		String a = "";
		//Solange keine Antwort da ist...
		do {
			//aktives Warten
			try {
				//Wenn null zurückgegeben wird kann ein Fehler auftreten
				a = antwort.get(flag).toString();
				if(a == null) {
					a = "";
				}
				
				Thread.sleep(wartezeit);
			} catch (Exception e) {
				a = "";
				continue;
			}
		} while(a.equals(""));
		
		antwort.remove(flag);
		return a;
	}
	
	public Model gibModel() {
		while(!modelupdate) {
			try {
				Thread.sleep(wartezeit);
			} catch (InterruptedException e) {
			}
		}
		modelupdate = false;
		return model; 
	}

	
	public Karte gibKarte() throws InterruptedException {	
		while(karte == null) {
			Thread.sleep(wartezeit);
		}
		Karte k = karte;
		//löscht die eingelesene Karte, um nicht zweimal die gleiche zurückzugeben
		karte = null;
		return k;
	}
	
	public synchronized void setzeModel(Model m) {
		this.model = m;
	}

	public boolean erste3(Model model) throws Exception {
		//sendet das Model und erwartet antwort
		setzeModel(model);
		netzwerk.printModel("!ERSTE3", model);			
		//horcht nach der Antwort	
		if(gibAntwort("!ERSTE3").equals("JA")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sendet die Namen der Mitspieler
	 */
	private void mitspielerNamen() {
		try {
			String[] namen = model.gibNamen();
			netzwerk.print("!MITSPIELER", namen);
		} catch (Exception e) {
			//Keine Fehlermeldung, da unbedeutend
			e.printStackTrace();
		}
	}

	public void spielen(Model model) throws Exception {
		netzwerk.printModel("!SPIEL", model);
	}

	public modus spielstDu(Model model, modus m) throws Exception {
		netzwerk.print("!BESTESSPIEL", m.toString());
		netzwerk.printModel("!SPIELSTDU", model);
		return modus.valueOf(gibAntwort("!SPIELSTDU"));
	} 
	
	public void spieler(int spielt, int mitspieler) throws Exception {
		String[] data = new String[] {
				String.valueOf(spielt),
				String.valueOf(mitspieler)
		};
		netzwerk.print("!SPIELT", data);
	}

	public boolean modus(lib.Model.modus m) throws Exception {
		netzwerk.print("!MODUS", m.toString());
		
		//gibt zurück, ob Kontra gegeben wurde
		if(gibAntwort("!KONTRA").equals("JA")) {
			return true;
		} else {
			return false;
		}
	}

	public void sieger(int s1, int s2) throws Exception {
		String[] data = new String[] {
			String.valueOf(s1),
			String.valueOf(s2)
		};
		netzwerk.print("!SIEGER", data);
	}
 
	public String gibIP() {
		String ip = netzwerk.gibIP();
		return ip;
	}
	
	public String gibName() {
		while(name == null) {
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
		}
		return name;
	}

	public void name() throws Exception {
		netzwerk.print("!NAME", "");
	}

	public void setzeID(int ID) throws Exception {
		netzwerk.setID(ID);
		netzwerk.print("!ID", String.valueOf(ID));
		
		//Sendet die Namen der Mitspieler
		mitspielerNamen();
	}

	public boolean hochzeit() throws Exception {
		netzwerk.print("!HOCHZEIT", "");
		if(gibAntwort("!HOCHZEIT").equals("JA")) {
			return true;
		} else {
			return false;
		}
	}

	public void geklopft(boolean[] geklopft) throws Exception {
		String[] data = new String[4];
		for(int i = 0; i < 4; i++) {
			if(geklopft[i]) {
				data[i] = "JA";
			} else {
				data[i] = "NEIN";
			}
		}
		netzwerk.print("!GEKLOPFT", data);
	}

	public void kontra(boolean[] kontra) throws Exception {
		String[] data = new String[4];
		for(int i = 0; i < 4; i++) {
			if(kontra[i]) {
				data[i] = "JA";
			} else {
				data[i] = "NEIN";
			}
		}
		netzwerk.print("!KONTRA", data);
	}
	
	public void update(Model model) {
		try {
			netzwerk.printModel("!UPDATE", model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void weristdran(int ID) {
		try {
			netzwerk.print("!DRAN", String.valueOf(ID));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void konto(int kontostand, int stock) {
		String[] data = new String[] {
				String.valueOf(kontostand),
				String.valueOf(stock)
		};
		try {
			netzwerk.print("!KONTO", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stich(Model model) {
		//Model wird nach jedem Stich an Bots weitergegeben
	}
	 
	public void rundeZuende(int kontostand, int stock) {
		String[] data = new String[] {
				String.valueOf(kontostand),
				String.valueOf(stock)
		};
		try {
			netzwerk.print("!ENDE", data);
			antwort.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void beenden() {
		beenden = true;
		netzwerk.beenden();
		spieler.interrupt();
		server.entferneSpieler(this);
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
