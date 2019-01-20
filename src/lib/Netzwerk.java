package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class Netzwerk {
	 
	protected int spielerID = -1;
	
	//Addresse des Clients
	protected String ip;
	//Port am Client
	protected int port = 35555;
	
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
	protected void ModelSenden(Model model) {
		//Speichert das Model
		this.model = model;
		
		//Die Karten des Spielers
		ArrayList<Karte> spielerhand = model.gibSpielerKarten(spielerID);
		for(int i = 0; i < 6; i++) {
			Karte karte = spielerhand.get(i);
			if(karte == null) {
				print("");
				print("");
			} else {
				print(karte.gibFarbe().toString());
				print(karte.gibWert().toString());
			}
		}
		
		//Der Tisch
		Karte[] tisch = model.gibTisch();
		for(int i = 0; i < 4; i++) {
			if(tisch[i] == null) {
				print("");
				print("");
			} else {
				print(tisch[i].gibFarbe().toString());
				print(tisch[i].gibWert().toString());
			}
		}
		
		//Der letzte Stich
		Karte[] letzterStich = model.gibLetztenStich();
		for(int i = 0; i < 4; i++) {
			if(letzterStich[i] == null) {
				print("");
				print("");
			} else {
				print(letzterStich[i].gibFarbe().toString());
				print(letzterStich[i].gibWert().toString());
			}
		}
	}
	
	/**
	 * Empfängt ein die Änderungen des Spielers und erstellt ein neues Model mit den Änderungen und 
	 * füllt es mit den Daten des zuletzt gesendeten Models
	 * @return aktualisiertes Model
	 * @throws Exception 
	 */
	protected Model ModelEmpfangen() {
		
		Model model;
		
		//Neue Karten werden hier zwischengespeichert
		Karte karte;
		
		//Die Hände der Spieler werden initialisiert
		ArrayList< ArrayList<Karte> > spielerhand = new ArrayList< ArrayList<Karte>>();
		for(int i = 0; i < 4; i++) {
			if(i == spielerID) {
				spielerhand.add(new ArrayList<Karte>());
			} else {
				//Übernimmt die Karten der anderen Spieler aus dem gesendeten Model
				spielerhand.add(this.model.gibSpielerKarten(i));
			}
		}
		Karte[] tisch = new Karte[4];
		Karte[] letzterStich = new Karte[4];
		ArrayList<Integer> punkte = this.model.gibPunkte();
		
		//Die Karten des Spielers
		for(int i = 0; i < 6; i++) {
			String farbe = readLine();
			String wert = readLine();
				
			//Wurde keine Karte gesendet, keine hinzufügen
			if(!farbe.equals("")) {
				karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
				spielerhand.get(spielerID).add(karte);
			}
		}
			
		//Der Tisch
		for(int i = 0; i < 4; i++) {
			String farbe = readLine();
			String wert = readLine();
			
			//Wurde keine Karte gesendet, keine hinzufügen
			if(farbe.equals("")) {
				tisch[i] = null;
			} else {
				karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
				tisch[i] = karte;
			}
		}
		
		//Der letzte Stich
		for(int i = 0; i < 4; i++) {
			String farbe = readLine();
			String wert = readLine();
				
			//siehe einlesen
			if(farbe.equals("")) {
				letzterStich[i] = null;
			} else {
				karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
				letzterStich[i] = karte;
			}
		}
		
		model = new Model(spielerhand, tisch, letzterStich, punkte);
		
		return model;
	}
	
	/**
	 * ID des Spielers setzen
	 * @param ID
	 */
	public void setID(int ID) {
		spielerID = ID;
	}
	
	/**
	 * Sendet Antworten z.B. ob geklopft wird, oder was gespielt wird
	 * @param modus
	 */
	protected void print(String output) {
		if(output.equals("") || output == null) {
			//keine Leerzeichen
			output = "§";
		}
		out.println(output);
	}
	
	/**
	 * Schreibt einen Datensatz mit Flag in den Stream
	 * @param flag
	 * @param data
	 * @throws Exception
	 */
	public void print(String flag, String data) {
		print(flag);
		print(data);
		print("!END");
	}
	
	/**
	 * Schreibt eine Reihe von Datensätzen mit Flag
	 * @param flag
	 * @param data
	 * @throws Exception
	 */
	public void print(String flag, String[] data) {
		print(flag);
		for(int i = 0; i < data.length; i++) {
			print(data[i]);
		}
		print("!END");
	}
	
	/**
	 * Sendet ein Model mit Flag
	 * @param flag
	 * @param data
	 * @throws Exception
	 */
	public void printModel(String flag, Model data) {
		print("!MODEL");
		print(flag);
		ModelSenden(data);
	}
	
	/**
	 * Liest eine Reihe von Datensätzen
	 * @return Flag und Datensätze
	 * @throws Exception
	 */
	public Object[] read() {
		ArrayList<String> data = new ArrayList<String>();		
		data.add(readLine());
		
		if(data.get(0).equals("!MODEL")) {
			//Wenn ein Model gesendet wird
			return readModel();
		}
		String input = readLine();
		while(!input.equals("!END")) {
			data.add(input);
			input = readLine();
		}
		
		return data.toArray();
	}
	
	/**
	 * Liest vom Stream ein, bis etwas gesendet wurde
	 * @return input
	 * @throws Exception 
	 */
	protected String readLine() {
		String input = "";
		try {
			input = in.readLine();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//§ kommt nicht in der Ausgabe vor!!
		if(input.equals("§")) {
			input = "";
		}
		
		return input; 
	}
	
	/**
	 * Liest ein Model
	 * @return Flag und Model
	 * @throws Exception
	 */
	protected Object[] readModel() {
		Object[] data = new Object[2];
		data[0] = readLine();
		data[1] = ModelEmpfangen();
		return data;
	}
	
	/**
	 * Schließt die Verbindung
	 */
	public void beenden() {
		try {
			in.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Fehler beim Beenden der Verbindung");
		}
	}
}
