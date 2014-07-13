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
	
	public Mensch(int port) throws Exception {
		
		try {
			//Errichtet neue Verbindung
			netzwerk = new Netzwerk(port);
		} catch(Exception e) {
			//Gibt den Fehler weiter
			throw e;
		}
	}

	public String erste3(Model model) {
		//sendet das Model und erwartet antwort
		try {
			netzwerk.senden(model);
			//horcht nach der Antwort
			return netzwerk.answer();
		} catch(Exception e) {
			return null;
		}
	}

	public Model spielen(Model model) {
		Model m = model;
		
		try {
			//Senden des Models und 
			netzwerk.senden(m);
			//Empfangen des veränderten Models
			m = netzwerk.empfangen();
		} catch(Exception e) {
			error(e);
		}
		return m;
	}

	public modus spielstDu(Model model) {
		//hält die Antwort
		String antwort;
		
		try {
			netzwerk.senden(model);
			antwort = netzwerk.answer();
			//Gibt den Spielmodus zurück, falls gespielt wird
			return modus.valueOf(antwort);
		} catch(Exception e) {
			error(e);
		}
		//ansonsten wird nichts gespielt
		return null;
	}
	
	/**
	 * Behandelt Fehler, die beim Verbindungsaufbau zustandekommen
	 * @param e
	 */
	private void error(Exception e) {
		e.printStackTrace();
	}

}
