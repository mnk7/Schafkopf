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
				Thread.sleep(100);
				
				String input = netzwerk.einlesen();
				
				if(input.equals("!NAME")) {
					name = netzwerk.einlesen();
					break;
				} if(input.equals("!ERSTE3")) {
					antwort = netzwerk.einlesen();
					break;
				} if(input.equals("!SPIEL")) {
					model = netzwerk.empfangen();
					break;
				} if(input.equals("!SPIELSTDU")) {
					antwort = netzwerk.einlesen();
					break;
				} if(input.equals("!HOCHZEIT")) {
					antwort = netzwerk.einlesen();
				} if(input.equals("!KARTE")) {	
					karte = netzwerk.getKarte();
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
	
	public Model gibModel() {
		return model; 
	}
	
	public void setzeModel(Model m) {
		this.model = m;
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
	
	/**
	 * Sendet die Namen der Mitspieler
	 */
	private void mitspielerNamen() {
		try {
			netzwerk.send("!MITSPIELER");
			Thread.sleep(100);
			
			String[] namen = model.gibNamen();
			for(int i = 0; i < namen.length; i++) {
				netzwerk.send(namen[i]);
			}
		} catch (Exception e) {
			//Keine Fehlermeldung, da unbedeutend
			e.printStackTrace();
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
	
	public void spieler(int spielt, int mitspieler) throws Exception {
		try {
			netzwerk.send("!SPIELT");
			netzwerk.send(String.valueOf(spielt));
			netzwerk.send(String.valueOf(mitspieler));
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
		netzwerk.send("!NAME");
		Thread.sleep(100);
		return name;
	}

	public synchronized void setzeID(int ID) throws Exception {
		netzwerk.setID(ID);
		//Steuerbefehl
		netzwerk.send("!ID");		
		netzwerk.send(String.valueOf(ID));
		
		//Sendet die Namen der Mitspieler
		mitspielerNamen();
	}

	public synchronized Karte gibKarte() throws InterruptedException {
		//Auf Eingabe warten
		Thread.sleep(100);
		
		Karte k = karte;
		//löscht die eingelesene Karte, um nicht zweimal die gleiche zurückzugeben
		karte = null;
		return k;
	}

	public void hochzeit() throws Exception {
		netzwerk.send("!HOCHZEIT");
	}

	public void geklopft(boolean[] geklopft) throws Exception {
		netzwerk.send("!GEKLOPFT");
		for(int i = 0; i < 4; i++) {
			if(geklopft[i])
				netzwerk.send("true");
			netzwerk.send("false");
		}
	}

	public void kontra(boolean[] kontra) throws Exception {
		netzwerk.send("!KONTRA");
		for(int i = 0; i < 4; i++) {
			if(kontra[i])
				netzwerk.send("true");
			netzwerk.send("false");
		}
	}

}
