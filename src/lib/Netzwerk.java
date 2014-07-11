package lib;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

public abstract class Netzwerk {
	
	protected int spielerID;
	
	//Addresse des Clients
	protected InetAddress address;
	//Port am Client
	protected int port;
	
	//Lesen und Schreiben
	protected PrintWriter out;
	protected BufferedReader in;
	
	/**
	 * Sendet ein Model
	 * @param model
	 */
	public void senden(Model model) {
		
		//Die Karten des Spielers
		ArrayList<Karte> spielerhand = model.gibSpielerKarten(spielerID);
		for(int i = 0; i < 6; i++) {
			Karte karte = spielerhand.get(i);
			out.write(karte.gibFarbe().ordinal());
			out.write(karte.gibWert().ordinal());
			out.write("-");
		}
		
		//Der Tisch
		Karte[] tisch = model.gibTisch();
		for(int i = 0; i < 4; i++) {
			out.write(tisch[i].gibFarbe().ordinal());
			out.write(tisch[i].gibWert().ordinal());
			out.write("-");
		}
		
		//Der letzte Stich
		Karte[] letzterStich = model.gibLetztenStich();
		for(int i = 0; i < 4; i++) {
			out.write(letzterStich[i].gibFarbe().ordinal());
			out.write(letzterStich[i].gibWert().ordinal());
			out.write("-");
		}
	}
	
	/**
	 * Empfängt ein Model
	 * @return aktualisiertes Model
	 */
	public Model empfangen() {
		Model model;
		
		ArrayList<Karte> kartendeck;
		ArrayList< ArrayList<Karte> > spielerhand;
		Karte[] tisch;
		Karte[] letzterStich;
		ArrayList<Integer> punkte;
		
		model = new Model(kartendeck, spielerhand, tisch, letzterStich, punkte);
		
		return model;
	}
}
