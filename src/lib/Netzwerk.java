package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
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
	protected Model model;
	
	/**
	 * Sendet ein Model, bzw nicht die Karten der anderen Spieler
	 * @param model
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public void senden(Model model) throws Exception {
		//Speichert das Model
		this.model = model;
		
		//Die Karten des Spielers
		ArrayList<Karte> spielerhand = model.gibSpielerKarten(spielerID);
		for(int i = 0; i < 6; i++) {
			try {
			Karte karte = spielerhand.get(i);
			out.write(karte.gibFarbe().toString());
			out.write(karte.gibWert().toString());
			} catch(Exception e) {
				out.write("");
				out.write("");
			}
		}
		
		//Der Tisch
		Karte[] tisch = model.gibTisch();
		for(int i = 0; i < 4; i++) {
			try {
			out.write(tisch[i].gibFarbe().toString());
			out.write(tisch[i].gibWert().toString());
			} catch(Exception e) {
				out.write("");
				out.write("");
			}
		}
		
		//Der letzte Stich
		Karte[] letzterStich = model.gibLetztenStich();
		for(int i = 0; i < 4; i++) {
			String output;
			try {
				output = letzterStich[i].gibFarbe().toString();
			} catch(Exception e) {
				output = "";
			}
			out.write(output);
			
			try {
				output = letzterStich[i].gibWert().toString();
			} catch(Exception e) {
				output = "";
			}
			out.write(output);
		}
	}
	
	/**
	 * Empfängt ein die Änderungen des Spielers und erstellt ein neues Model mit den Änderungen und 
	 * füllt es mit den Daten des zuletzt gesendeten Models
	 * @return aktualisiertes Model
	 * @throws Exception 
	 */
	public Model empfangen() throws Exception {
		
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
				String farbe = in.readLine();
				String wert = in.readLine();
				
				//Wurde keine Karte gesendet, keine hinzufügen
				if(farbe.equals("")) throw new Exception();
				
				karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
				
				spielerhand.get(spielerID).add(karte);
			} catch(Exception e) {
				//gibt es keine neuen Karten, dann beenden
				break;
			}
		}
			
		//Der Tisch
		for(int i = 0; i < 4; i++) {
			try {
				String farbe = in.readLine();
				String wert = in.readLine();
				
				//Wurde keine Karte gesendet, keine hinzufügen
				if(farbe.equals("")) throw new Exception();
				
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
				String farbe = in.readLine();
				String wert = in.readLine();
				
				if(farbe.equals("")) letzterStich[i] = null;
				else {
					karte = new Karte(Karte.farbe.valueOf(farbe), Karte.wert.valueOf(wert));
	
					letzterStich[i] = karte;
				}
			} catch(Exception e) {
				throw new Exception("!!!Fehler beim Empfangen von Daten!!");
			}
		}

		
		model = new Model(spielerhand, tisch, letzterStich, punkte);
		
		return model;
	}
	
	/**
	 * frägt nach Antworten des Clients
	 * @return
	 * @throws IOException
	 */
	public String getAnswer() throws Exception {
		String output = in.readLine();
		return output;
	}
	
	/**
	 * Sendet Antworten z.B. ob geklopft wird, oder was gespielt wird
	 * @param modus
	 */
	public void send(String modus) throws Exception{
		out.write(modus);
	}
	
	/**
	 * ID des Spielers setzen
	 * @param ID
	 */
	public void setID(int ID) {
		spielerID = ID;
	}
}
