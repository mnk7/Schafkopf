/**
 * Klasse, die den Aktuellen Spielstand beherbergt.
 * Dazu zählen alle Karten auf dem Tisch, die Karten
 * aller Spieler und eine Liste aller Karten.
 */

package lib;

import java.util.ArrayList;

public class Model {
	
	//Alle 24 Karten
	private ArrayList<Karte> kartendeck;
	
	//Die Karten der einzelnen Spieler
	private ArrayList< ArrayList<Karte> > spielerhand;
	
	//Alle Karten auf dem Tisch
	private Karte[] tisch;
	
	
	/**
	 * Initialisiert alle Listen und befüllt das Kartendeck
	 */
	public Model() {
		
		kartendeck = new ArrayList<Karte>();
		
		spielerhand = new ArrayList< ArrayList<Karte> >();
		//Initialisiert die Spielerhaende
		for(int i = 0; i < 4; i++) {
			spielerhand.add(new ArrayList<Karte>());
		}
		
		tisch = new Karte[4];
		
		initKarten();
	}
	
	/**
	 * Erstellt alle Karten
	 */
	private void initKarten() {
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				kartendeck.add(
					new Karte(Karte.farbe.values()[i], 
							  Karte.wert.values()[j]));
			}
		}
	}
	
	/**
	 * Mischt die Karten im Kartendeck
	 */
	public void mischen() {
		
	}
	
	/**
	 * Gibt die ersten drei Karten
	 */
	public void ersteKartenGeben() {
		for(int i = 0; i < 12; i++) {
			spielerhand.get(i % 4).add(kartendeck.get(i));
		}
	}
	
	/**
	 * Gibt fertig aus
	 */
	public void zweiteKartenGeben() {
		for(int i = 12; i < 24; i++) {
			spielerhand.get(i % 4).add(kartendeck.get(i));
		}
	}
	
	public Karte[] gibTisch(){
		return tisch;
	}
	

}
