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

public class Mensch extends Thread implements Spieler {
	
	private Netzwerk netzwerk;
	
	//Hält eine Referenz auf den Server, um diesen zu benachrichtigen, wenn
	//die Verbindung abgebrochen wurde
	private Server server;
	
	private String name;
	
	//speichert neueste antworten
	private String antwort;
	private Model model;
	private Karte karte;

	private boolean modelupdate = false;
	
	public Mensch(Socket client, Server server) throws Exception {
		
		antwort = "";
		
		this.server = server;
		
		//Errichtet neue Verbindung
		netzwerk = new Netzwerk(client);
	}
	
	/**
	 * Horcht auf Befehle vom Client
	 */
	public void run() {
		
		while(true) {
			try {
				Object[] data = netzwerk.read();
				
				if(data[0].equals("!NAME")) {
					name = data[1].toString();
					break;
				} if(data[0].equals("!ERSTE3")) {
					antwort = data[1].toString();
					break;
				} if(data[0].equals("!SPIEL")) {
					model = (Model) data[1];
					modelupdate = true;
					break;
				} if(data[0].equals("!SPIELSTDU")) {
					antwort = data[1].toString();
					break;
				} if(data[0].equals("!KONTRA")) {
					antwort = data[1].toString();
					break;
				} if(data[0].equals("!HOCHZEIT")) {
					//Antwort JA + Karte oder NEIN
					antwort = data[1].toString();
					if(antwort.equals("JA")) {
						model = (Model) data[2];
						modelupdate = true;
					}
					break;
				} if(data[0].equals("!KARTE")) {	
					karte = new Karte(
							Karte.farbe.valueOf(data[1].toString()),
							Karte.wert.valueOf(data[2].toString()));
					break;
				}
			} catch (Exception e) {
				//Benachrichtige Server, dass ein Spieler entfernt wurde
				server.entferneSpieler(this);
				break;
			}
		}
	}
	
	public synchronized String gibAntwort() {
		//Solange keine Antwort da ist...
		while(antwort == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		String a = antwort;
		//Eingelesene Antwort löschen
		antwort = null;
		return a;
	}
	
	public synchronized Model gibModel() {
		while(!modelupdate) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		modelupdate = false;
		return model; 
	}
	
	public synchronized void setzeModel(Model m) {
		this.model = m;
	}

	public synchronized void erste3(Model model) throws Exception {
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
	private synchronized void mitspielerNamen() {
		try {
			String[] namen = model.gibNamen();
			netzwerk.print("!MITSPIELER", namen);
		} catch (Exception e) {
			//Keine Fehlermeldung, da unbedeutend
			e.printStackTrace();
		}
	}

	public synchronized void spielen(Model model) throws Exception {
		netzwerk.printModel("!SPIEL", model);
	}

	public synchronized void spielstDu(Model model) throws Exception {
		netzwerk.printModel("!SPIELSTDU", model);
	} 
	
	public synchronized void spieler(int spielt, int mitspieler) throws Exception {
		String[] data = new String[] {
				String.valueOf(spielt),
				String.valueOf(mitspieler)
		};
		netzwerk.print("!SPIEL", data);
	}

	public synchronized void modus(lib.Model.modus m) throws Exception {
		netzwerk.print("!MODUS", m.toString());
		//gibt zurück, ob Kontra gegeben wurde
	}

	public synchronized void sieger(int s1, int s2) throws Exception {
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
		//Gefährlich, aber der Name ist für den weiteren Ablauf wichtig
		while(name == null) {
			try {
				name();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return name;
	}

	public synchronized void name() throws Exception {
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

	public synchronized void hochzeit() throws Exception {
		netzwerk.print("!HOCHZEIT", "");
	}

	public synchronized void geklopft(boolean[] geklopft) throws Exception {
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

	public synchronized void kontra(boolean[] kontra) throws Exception {
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
