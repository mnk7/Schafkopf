/**
 * Klasse, die den Aktuellen Spielstand beherbergt.
 * Dazu zählen alle Karten auf dem Tisch, die Karten
 * aller Spieler und eine Liste aller Karten.
 */

package lib;

import java.util.ArrayList;

public class Model {
	
	//Alle Karten
	private ArrayList<Karte> kartendeck;
	
	//Die Karten der einzelnen Spieler
	private ArrayList< ArrayList<Karte> > spielerhande;
	
	//Alle Karten auf dem Tisch
	private Karte[] tisch;
	
	public Model() {
		
	}
	

}
