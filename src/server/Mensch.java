/**
 * Repräsentiert serverseitig einen menschlichen Spieler. Übernimmt einen Port und horcht diesen ab.
 * 
 */

package server;

import java.net.Socket;

import lib.Karte;
import lib.Model;
import lib.Model.modus;
import server.Netzwerk;

public class Mensch implements Spieler, Runnable {
	
	private Netzwerk netzwerk;
	
	Thread t;
	
	//Hält eine Referenz auf den Server, um diesen zu benachrichtigen, wenn
	//die Verbindung abgebrochen wurde
	Server server;
	
	private String name;
	
	//speichert neueste antworten
	private String antwort;
	private Model model;
	private Karte karte;
	
	private boolean kontra;
	
	public Mensch(Socket client, Server server) throws Exception {
		
		antwort = "";
		
		this.server = server;
		
		try {
			//Errichtet neue Verbindung
			netzwerk = new Netzwerk(client);
			
			//Lauscher aufsperren
			t = new Thread(this);
			
			t.start();
			
		} catch(Exception e) {
			//Gibt den Fehler weiter
			throw e;
		}
	}
	
	/**
	 * Horcht auf Befehle vom Server
	 */
	public void run() {
		
		while(true) {
			try {
				//Thread.sleep(100);
				
				Object[] data = netzwerk.read();
				
				if(data[0].equals("!NAME")) {
					name = (String) data[1];
					break;
				} if(data[0].equals("!ERSTE3")) {
					antwort = (String) data[1];
					break;
				} if(data[0].equals("!SPIEL")) {
					model = (Model) data[1];
					break;
				} if(data[0].equals("!SPIELSTDU")) {
					antwort = (String) data[1];
					break;
				} if(data[0].equals("!KONTRA")) {
					if(data[1].equals("true")) {
						kontra = true;
					} else {
						kontra = false;
					}	
					break;
				} if(data[0].equals("!HOCHZEIT")) {
					//Antwort JA + Karte oder NEIN
					antwort = (String) data[1];
					if(antwort.equals("JA")) {
						model = (Model) data[2];
					}
					break;
				} if(data[0].equals("!KARTE")) {	
					karte = new Karte(
							Karte.farbe.valueOf((String) data[1]),
							Karte.wert.valueOf((String) data[2]));
					break;
				}
			} catch (Exception e) {
				//Benachrichtige Server, dass ein Spieler entfernt wurde
				server.entferneSpieler(this);
				break;
			}
		}
	}
	
	public String gibAntwort() {
		String a = antwort;
		//Eingelesene Antwort löschen
		antwort = null;
		return a;
	}
	
	public boolean gibKontra() {
		return kontra;
	}
	
	public Model gibModel() {
		return model; 
	}
	
	public void setzeModel(Model m) {
		this.model = m;
	}

	public void erste3(Model model) throws Exception {
		//sendet das Model und erwartet antwort
		try {
			netzwerk.printModel("!ERSTE3", model);			
			//horcht nach der Antwort			
		} catch(Exception e) {
			throw e;
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
		try {
			netzwerk.printModel("!SPIEL", model);
		} catch(Exception e) {
			throw e;
		}
	}

	public void spielstDu(Model model) throws Exception {
		try {
			netzwerk.printModel("!SPIELSTDU", model);
		} catch(Exception e) {
			throw e;
		}
	} 
	
	public void spieler(int spielt, int mitspieler) throws Exception {
		try {
			String[] data = new String[] {
					String.valueOf(spielt),
					String.valueOf(mitspieler)
			};
			netzwerk.print("!SPIEL", data);
		} catch (Exception e) {
			throw e;
		}
	}

	public void modus(lib.Model.modus m) throws Exception {
		netzwerk.print("!MODUS", m.toString());
		//gibt zurück, ob Kontra gegeben wurde
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
		return name;
	}

	public void name() throws Exception {
		netzwerk.print("!NAME", "");
	}

	public synchronized void setzeID(int ID) throws Exception {
		netzwerk.setID(ID);
		netzwerk.print("!ID", String.valueOf(ID));
		
		//Sendet die Namen der Mitspieler
		mitspielerNamen();
	}

	public synchronized Karte gibKarte() throws InterruptedException {	
		Karte k = karte;
		//löscht die eingelesene Karte, um nicht zweimal die gleiche zurückzugeben
		karte = null;
		return k;
	}

	public void hochzeit() throws Exception {
		netzwerk.print("!HOCHZEIT", "");
	}

	public void geklopft(boolean[] geklopft) throws Exception {
		String[] data = new String[4];
		for(int i = 0; i < 4; i++) {
			if(geklopft[i]) {
				data[i] = "true";
			} else {
				data[i] = "false";
			}
		}
		netzwerk.print("!GEKLOPFT", data);
	}

	public void kontra(boolean[] kontra) throws Exception {
		String[] data = new String[4];
		for(int i = 0; i < 4; i++) {
			if(kontra[i]) {
				data[i] = "true";
			} else {
				data[i] = "false";
			}
		}
		netzwerk.print("!KONTRA", data);
	}

}
