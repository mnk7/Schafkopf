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
	
	private String name;
	
	//speichert neueste antworten
	private String antwort;
	private Model model;
	private Karte karte;
	
	public Mensch(Socket client) throws Exception {
		
		antwort = "";
		
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
				String input = netzwerk.einlesen();
				
				switch(input) {
				case "!NAME" :
					name = netzwerk.einlesen();
					break;
				case "!ERSTE3" :
					antwort = netzwerk.einlesen();
					break;
				case "!SPIEL" :
					model = netzwerk.empfangen();
					break;
				case "!SPIELSTDU" :
					antwort = netzwerk.einlesen();
					break;
				case "!HOCHZEIT" :
					antwort = netzwerk.einlesen();
				case "!KARTE" :	
					karte = netzwerk.getKarte();
					break;
				default :
					antwort = input;
				}
				
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String gibAntwort() {
		return antwort;
	}
	
	public Model gibModel() {
		return model;
	}

	public void erste3(Model model) throws Exception {
		//sendet das Model und erwartet antwort
		try {
			//Sendet erst Steuerbefehl
			netzwerk.send("!ERSTE3");
			
			netzwerk.senden(model);
			//horcht nach der Antwort
		} catch(Exception e) {
			throw e;
		}
	}

	public void spielen(Model model) throws Exception {
		Model m = model;
		
		try {
			//Steuerbefehl
			netzwerk.send("!SPIEL");
			
			//Senden des Models und 
			netzwerk.senden(m);
		} catch(Exception e) {
			throw e;
		}
	}

	public void spielstDu(Model model) throws Exception {
		try {
			//Steuerbefehl
			netzwerk.send("!SPIELSTDU");
			
			netzwerk.senden(model);

		} catch(Exception e) {
			throw e;
		}
	} 
	
	public void spieler(int spielt) throws Exception {
		try {
			netzwerk.send("!SPIELT");
		} catch (Exception e) {
			throw e;
		}
	}

	public String modus(lib.Model.modus m) throws Exception{
		//Steuerbefehl
		netzwerk.send("!MODUS");
		
		//Sendet den Modus
		netzwerk.send(m.toString());
		
		//gibt zurück, ob Kontra gegeben wurde
		try {
			return netzwerk.einlesen();
		} catch(Exception e) {
			return null;
		}
	}

	public void sieger(int s1, int s2) throws Exception {
		//Steuerbefehl
		netzwerk.send("!SIEGER");
		
		netzwerk.send(String.valueOf(s1));
		netzwerk.send(String.valueOf(s2));
	}
 
	public String gibIP() {
		String ip = netzwerk.gibIP();
		return ip;
	}

	public synchronized String gibName() throws Exception {
		try {
			netzwerk.send("!NAME");
			Thread.sleep(100);
		} catch (Exception e) {
			throw e;
		}
		return name;
	}

	public synchronized void setzeID(int ID) throws Exception {
		//Steuerbefehl
		netzwerk.send("!ID");		
		netzwerk.send(String.valueOf(ID));
	}

	public synchronized Karte gibKarte() {
		return karte;
	}

	public void hochzeit() throws Exception {
		netzwerk.send("!HOCHZEIT");
	}

}
