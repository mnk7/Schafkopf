/**
 * Repräsentiert serverseitig einen menschlichen Spieler. Übernimmt einen Port und horcht diesen ab.
 * 
 */

package server;

import lib.Model;
import lib.Model.modus;
import server.Netzwerk;

public class Mensch implements Spieler {
	
	private Netzwerk netzwerk;
	
	private String name;
	
	public Mensch(int port) throws Exception {
		
		try {
			//Errichtet neue Verbindung
			netzwerk = new Netzwerk(port);
			//Als erstes wird der Name übertragen
			name = netzwerk.getAnswer();
		} catch(Exception e) {
			//Gibt den Fehler weiter
			throw e;
		}
	}

	public String erste3(Model model) {
		//sendet das Model und erwartet antwort
		try {
			//Sendet erst Steuerbefehl
			netzwerk.send("!ERSTE3");
			
			netzwerk.senden(model);
			//horcht nach der Antwort
			return netzwerk.getAnswer();
		} catch(Exception e) {
			return null;
		}
	}

	public Model spielen(Model model) {
		Model m = model;
		
		try {
			//Steuerbefehl
			netzwerk.send("!SPIEL");
			
			//Senden des Models und 
			netzwerk.senden(m);
			//Empfangen des veränderten Models
			m = netzwerk.empfangen();
		} catch(Exception e) {
			e.printStackTrace();
			m = null;
		}
		return m;
	}

	public modus spielstDu(Model model) {
		//hält die Antwort
		String antwort;
		
		try {
			//Steuerbefehl
			netzwerk.send("!SPIELSTDU");
			
			netzwerk.senden(model);
			antwort = netzwerk.getAnswer();
			
			//Gibt den Spielmodus zurück, falls gespielt wird
			return modus.valueOf(antwort);
		} catch(Exception e) {
			e.printStackTrace();
		}
		//ansonsten wird nichts gespielt
		return null;
	}

	public String modus(lib.Model.modus m) {
		//Steuerbefehl
		netzwerk.send("!MODUS");
		
		//Sendet den Modus
		netzwerk.send(m.toString());
		
		//gibt zurück, ob Kontra gegeben wurde
		try {
			return netzwerk.getAnswer();
		} catch(Exception e) {
			return null;
		}
	}

	public void sieger(int s1, int s2) {
		//Steuerbefehl
		netzwerk.send("!SIEGER");
		
		netzwerk.send(String.valueOf(s1));
		netzwerk.send(String.valueOf(s2));
	}

	public String gibIP() {
		String ip = netzwerk.gibIP();
		return ip;
	}

	public String gibName() {
		return name;
	}

}
