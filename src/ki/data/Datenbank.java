package ki.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lib.Karte;

public class Datenbank {
	
	private BufferedReader reader;
	private BufferedWriter printer;
	private File data;
	
	private Spielzug gespielt;
	
	private HashMap<String, Spielzug> spielzuege;
	
	/**
	 * Erstellt eine Datenbank anhand einer .dt-Datei
	 * @param data
	 * @throws Exception
	 */
	public Datenbank(String data) throws Exception {
		Datenbank.class.getClassLoader().getResource(data);
		this.data = new File(data);
		//Datei im Notfall neu erstellen
		if(!this.data.exists()) {
			this.data.createNewFile();
		}
		//Und vollen Zugriff gewähren
		this.data.setWritable(true);
		this.data.setReadable(true);
			
		reader = new BufferedReader(new FileReader(data));
		printer = new BufferedWriter(new FileWriter(data, false));
		
		/*
		 * Initialisiert die Datenbank
		 */
		spielzuege = new HashMap<String, Spielzug>(); 
	}
	
	/**
	 * Sucht einen enthaltenen Datensatz
	 * @return
	 */
	private Spielzug suchen(Karte angespielt,
			int spielerID, 
			int spielender1, int spielender2, 
			ArrayList<Karte> spielerhand, 
			int ausspieler,
			boolean du, 
			ArrayList<Karte> gespielteKarten,
			Karte[] tisch) {
		Spielzug gesucht;
		
		String ps = angespielt.gibString();
		ps += String.valueOf(ausspieler);
		ps += String.valueOf(spielerID);
		ps += String.valueOf(spielender1);
		ps += String.valueOf(spielender2);
		
		for(int i = 0; i < spielerhand.size(); i++) {
			ps += spielerhand.get(i).gibString();
		}
		
		ps += String.valueOf(du);
		
		for(int i = 0; i < tisch.length; i++) {
			ps += tisch[i].gibString();
		}
		
		for(int i = 0; i < gespielteKarten.size(); i++) {
			ps += gespielteKarten.get(i).gibString();
		}
		
		gesucht = spielzuege.get(ps);
		
		return gesucht;
	}
	
	/**
	 * Gibt eine zu spielende Karte an oder NULL
	 * @param angespielt
	 * @param spielerID
	 * @param spielender1
	 * @param spielender2
	 * @param spielerhand
	 * @param du
	 * @param gespielteKarten
	 * @return
	 */
	public String welcheSpielen(Karte angespielt,
			int spielerID, 
			int spielender1, int spielender2, 
			ArrayList<Karte> spielerhand, 
			int ausspieler,
			boolean du, 
			ArrayList<Karte> gespielteKarten,
			Karte[] tisch) {
		
		gespielt = suchen(angespielt, spielerID, spielender1, spielender2, spielerhand,
						  ausspieler, du, gespielteKarten, tisch);
		
		//Wenn der Datensatz noch nicht enthalten ist, wird kein Ergebnis zurückgegeben
		if(gespielt == null) {
			return null;
		} else {
			return gespielt.welcheSpielen();
		}
	}
	
	/**
	 * Gibt den letzten gespielten Zug zurück
	 * @return
	 */
	public Spielzug gibLetztenSpielzug() {
		return gespielt;
	}
	
	/**
	 * Fügt einen neuen Spielzug zur Datenbank hinzu
	 * @param neuerSpielzug
	 */
	public void einfuegen(Spielzug neuerSpielzug) {
		neuerSpielzug.setzePrinter(printer);
		
		spielzuege.put(neuerSpielzug.gibPS(), neuerSpielzug);
	}
	
	/**
	 * Liest Datenbank aus .dt Datei
	 * @throws IOException 
	 */
	public void lesen() throws IOException {
		Spielzug neuerSpielzug;
		String input = "";
		
		while((input = reader.readLine()) != null) {
			neuerSpielzug = new Spielzug(input);
			einfuegen(neuerSpielzug);
		}
		
		reader.close();
	}
	
	/**
	 * Speichert die Datenbank
	 * @throws IOException 
	 */
	public void speichern() throws IOException {
		for(Entry<String, Spielzug> e: spielzuege.entrySet()) {
			e.getValue().speichern();
		}
		
		printer.flush();
		printer.close();
	}
}
