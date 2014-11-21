package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class Netzwerk {
	 
	protected int spielerID;
	
	//Addresse des Clients
	protected String ip;
	//Port am Client
	protected int port;
	
	//Lesen und Schreiben
	protected PrintWriter out;
	protected BufferedReader in; 
	
	//Speichert ein gesendetes Model
	protected Model model = new Model();
	
	/**
	 * Sendet ein Model, bzw nicht die Karten der anderen Spieler
	 * @param model
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	protected void senden(Model model) throws Exception {
		//Speichert das Model
		this.model = model;
		
		//Die Karten des Spielers
		ArrayList<Karte> spielerhand = model.gibSpielerKarten(spielerID);
		for(int i = 0; i < 6; i++) {
			try {
				Karte karte = spielerhand.get(i);
				send(karte.gibFarbe().toString());
				send(karte.gibWert().toString());
			} catch(Exception e) {
				send("§");
				send("§");
			}
		}
		
		//Der Tisch
		Karte[] tisch = model.gibTisch();
		for(int i = 0; i < 4; i++) {
			try {
				send(tisch[i].gibFarbe().toString());
				send(tisch[i].gibWert().toString());
			} catch(Exception e) {
				send("§");
				send("§");
			}
		}
		
		//Der letzte Stich
		Karte[] letzterStich = model.gibLetztenStich();
		for(int i = 0; i < 4; i++) {
			String output;
			try {
				output = letzterStich[i].gibFarbe().toString();
			} catch(Exception e) {
				output = "§";
			}
			send(output);
			
			try {
				output = letzterStich[i].gibWert().toString();
			} catch(Exception e) {
				output = "§";
			}
			send(output);
		}
	}
	
	/**
	 * Empfängt ein die Änderungen des Spielers und erstellt ein neues Model mit den Änderungen und 
	 * füllt es mit den Daten des zuletzt gesendeten Models
	 * @return aktualisiertes Model
	 * @throws Exception 
	 */
	protected Model empfangen() throws Exception {
		
		Model model;
		
		//Neue Karten werden hier zwischengespeichert
		Karte karte;
		
		//Die Hände der Spieler werden initialisiert
		ArrayList< ArrayList<Karte> > spielerhand = new ArrayList< ArrayList<Karte>>();
		for(int i = 0; i < 4; i++) {
			if(i == spielerID)
				spielerhand.add(new ArrayList<Karte>());
			else
				//Übernimmt die Karten der anderen Spieler aus dem gesendeten Model
				spielerhand.add(this.model.gibSpielerKarten(i));
		}
		Karte[] tisch = new Karte[4];
		Karte[] letzterStich = new Karte[4];
		ArrayList<Integer> punkte = new ArrayList<Integer>();
		
		//Die Karten des Spielers
		for(int i = 0; i < 6; i++) {
			try {
				String farbe = einlesen();
				String wert = einlesen();
				
				//Wurde keine Karte gesendet, keine hinzufügen
				if(!farbe.equals("§")) {
					karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
					spielerhand.get(spielerID).add(karte);
				}
			} catch(Exception e) {
				//gibt es keine neuen Karten, dann beenden
				break;
			}
		}
			
		//Der Tisch
		for(int i = 0; i < 4; i++) {
			try {
				String farbe = einlesen();
				String wert = einlesen();
				
				//Wurde keine Karte gesendet, keine hinzufügen
				if(farbe.equals("§")) throw new Exception();
				
				karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
				
				tisch[i] = karte;
			} catch (Exception e) {
				//Wird nichts empfangen
				tisch[i] = null;
			}
		}
		
		//Der letzte Stich
		for(int i = 0; i < 4; i++) {
			//Hier sind Fehler zum ersten mal unerwünscht
			try {
				String farbe = einlesen();
				String wert = einlesen();
				
				if(farbe.equals("§")) letzterStich[i] = null;
				else {
					karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
	
					letzterStich[i] = karte;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		model = new Model(spielerhand, tisch, letzterStich, punkte);
		
		return model;
	}
	
	/**
	 * Sendet Antworten z.B. ob geklopft wird, oder was gespielt wird
	 * @param modus
	 */
	protected void send(String output) throws Exception{
		System.out.println("[SEND]" + output);
		out.print(output);
		out.flush();
	}
	
	/**
	 * ID des Spielers setzen
	 * @param ID
	 */
	public void setID(int ID) {
		spielerID = ID;
	}
	
	/**
	 * Liest vom Stream ein, bis etwas gesendet wurde
	 * @return input
	 * @throws Exception 
	 */
	protected String einlesen() throws Exception {
		String input = "";
		try {
			do {
				input = in.readLine();
			} while(input.equals(null) || input.equals(""));
		} catch(Exception e) {
			throw e;
		}
		
		System.out.println("[READ]" + input);
		
		return input;
	}
	
	/**
	 * Schreibt einen Datensatz mit Steuerbefehl in den Stream
	 * @param steuer
	 * @param data
	 * @throws Exception
	 */
	public void print(String steuer, String data) throws Exception {
		String[] d = new String[1];
		d[0] = data;
		print(steuer, d);
	}
	
	/**
	 * Schreibt eine Reihe von Datensätzen mit Steuerbefehl
	 * @param steuer
	 * @param data
	 * @throws Exception
	 */
	public void print(String steuer, String[] data) throws Exception {
		send(steuer);
		if(einlesen().equals("!COPY")) {
			for(int i = 0; i < data.length; i++) {
				send(data[i]);
			}
			send("!END");
		}
	}
	
	/**
	 * Sendet ein Model mit Steuerbefehl
	 * @param steuer
	 * @param data
	 * @throws Exception
	 */
	public void printModel(String steuer, Model data) throws Exception {
		send("!MODEL");
		send(steuer);
		if(einlesen().equals("!COPY")) {
			senden(data);
		}
	}
	
	/**
	 * Liest eine Reihe von Datensätzen
	 * @return Steuerbefehl und Datensätze
	 * @throws Exception
	 */
	public Object[] read() throws Exception {
		ArrayList<String> data = new ArrayList<String>();
		data.add(einlesen());
		//Bestätigung senden
		send("!COPY");
		if(data.get(0).equals("!MODEL")) {
			return readModel();
		}
		String input = einlesen();
		while(!input.equals("!END")) {
			data.add(input);
			input = einlesen();
		}
		
		return data.toArray();
	}
	
	/**
	 * Liest ein Model
	 * @return Steuerbefehl und Model
	 * @throws Exception
	 */
	protected Object[] readModel() throws Exception {
		Object[] data = new Object[2];
		data[0] = einlesen();
		//Bestätigen
		send("!COPY");
		data[1] = empfangen();
		return data;
	}
}
