/**
 * Klasse, die den Aktuellen Spielstand beherbergt.
 * Dazu zählen alle Karten auf dem Tisch, die Karten
 * aller Spieler und eine Liste aller Karten.
 */

package lib;

import java.util.ArrayList;
import java.util.Random;

public class Model {
	
	//Alle 24 Karten
	private ArrayList<Karte> kartendeck;
	
	//Die Karten der einzelnen Spieler
	private ArrayList< ArrayList<Karte> > spielerhand;
	
	//Alle Karten auf dem Tisch
	private Karte[] tisch;
	
	//Speichert den letzten Stich
	private Karte[] letzterStich;
	
	private ArrayList<Integer> punkte;
	
	
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
		
		letzterStich = new Karte[4];
		
		//Erstellt den Punktezaehler
		punkte = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
			punkte.set(i, 0);
		}
		
		initKarten();
	}
	
	/**
	 * Erstellt ein Model aus übergebenen Daten (client)
	 * @param kartendeck
	 * @param spielerhand
	 * @param tisch
	 * @param letzerStich
	 * @param punkte
	 **/
	public Model(ArrayList<Karte> kartendeck, 
			ArrayList< ArrayList<Karte> > spielerhand,
			Karte[] tisch,
			Karte[] letzterStich,
			ArrayList<Integer> punkte) {
		
		this.kartendeck = kartendeck;
		
		this.spielerhand = spielerhand;
		
		this.tisch = tisch;
		
		this.letzterStich = letzterStich;
		
		this.punkte = punkte;
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
	 * @param tiefe: Gibt an, wie oft durch das Kartendeck gegangen werden soll
	 */
	public void mischen(int tiefe) {
		Random zufall = new Random();
		Karte karte;
		int z;
		
		for(int i = 0; i < tiefe; i++) {
			for(int j = 0; j < 24; j++) {
				//Karte zwischenspeichern
				karte = kartendeck.get(j);
				//Zufällige andere Karte auswählen
				z = zufall.nextInt(24);
				//Beide tauschen
				kartendeck.set(j, kartendeck.get(z));
				kartendeck.set(z, karte);
			}
		}
	}
	
	/**
	 * Mischt das Kartendeck fünf mal
	 */
	public void mischen() {
		mischen(5);
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
	
	/**
	 * Gibt alle Karten auf dem Tisch aus
	 * @return Karten-Feld
	 */
	public Karte[] gibTisch(){
		return tisch;
	}
	
	/**
	 * Gibt dem Spieler seine Karten
	 * @param spielerID
	 * @return ArrayList der Karten
	 */
	public ArrayList<Karte> gibSpielerKarten(int spielerID) {
		return spielerhand.get(spielerID);
	}
	
	/**
	 * Ablegen einer Karte auf dem Tisch
	 * @param spielerID
	 * @param karte
	 * @return Die aktuelle Hand des Spielers
	 * @throws Exception
	 */
	public ArrayList<Karte> setTisch(int spielerID, Karte karte) throws Exception {
		//Eine Vorsichtsmaßnahme um falsche Züge zu verhindern
		if(spielerhand.get(spielerID).contains(karte)) {
			tisch[spielerID] = karte;
			
			//Der Spieler haelt die Karte nicht mehr
			spielerhand.remove(karte);
			
		} else
			throw new Exception("Der Spieler besitzt diese Karte nicht!");
		
		return spielerhand.get(spielerID);
	}
	
	/**
	 * Karte vom Tisch zurücknehmen
	 * @param spielerID
	 * @return Die aktuelle Hand des Spielers
	 **/
	public ArrayList<Karte> undo(int spielerID) {
		//Gibt dem Spieler seine Karte wieder zurück
		//Wichtig für Prüfung der Legalität eines Spielzugs
		spielerhand.get(spielerID).add(tisch[spielerID]);
		tisch[spielerID] = null;
		
		return spielerhand.get(spielerID);
	}
	
	/**
	 * Der Stich wird dem Gewinner zugeschrieben und zwischengespeichert
	 * @param gewinnerID
	 */
	public void Stich(int gewinnerID) {
		int punkteStich = 0;
		
		for(int i = 0; i < 4; i++) {
			punkteStich += tisch[i].gibPunkte();
			//Der Stich wird zwischengespeichert
			letzterStich[i] = tisch[i];
			//Die Karten werden vom Tisch genommen
			tisch[i] = null;
		}
		
		//Der Gewinner bekommt die Punkte
		punkte.set(gewinnerID, punkte.get(gewinnerID) + punkteStich);
	}
	
	/**
	 * Gibt den letzten Stich zurück
	 * @return letzten Stich
	 */
	public Karte[] gibLetztenStich(){
		return letzterStich;
	}
	
}
