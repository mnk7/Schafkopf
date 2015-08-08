/**
 * Klasse, die den Aktuellen Spielstand beherbergt.
 * Dazu zählen alle Karten auf dem Tisch, die Karten
 * aller Spieler und eine Liste aller Karten.
 */

package lib;

import java.util.ArrayList;
import java.util.Random;

import regeln.Control;
import regeln.Regelwahl;

public class Model {
	
	//Die verschiedenen Spielmodi
	public enum modus {
		SAUSPIELeichel, SAUSPIELgras, SAUSPIELherz, SAUSPIELschellen,
		HOCHZEIT,
		GEIER,
		WENZ,
		SOLOeichel, SOLOgras, SOLOherz, SOLOschellen,
		GEIERdu,
		WENZdu,
		SOLOeichelDU, SOLOgrasDU, SOLOherzDU, SOLOschellenDU,
		SI,
		NICHTS
	};
	
	private String[] spielernamen;
	
	//Alle 24 Karten
	private ArrayList<Karte> kartendeck;
	
	//Die Karten der einzelnen Spieler
	private ArrayList< ArrayList<Karte> > spielerhand;
	
	//Alle Karten auf dem Tisch
	private Karte[] tisch;
	
	//Speichert den letzten Stich
	private Karte[] letzterStich;
	private int letzterStichGewinner;
	private int letzterStichPunkte;
	
	private ArrayList<Integer> punkte;
	
	private int laufende;
	
	
	/**
	 * Initialisiert alle Listen und befüllt das Kartendeck
	 */
	public Model() {
		
		spielernamen = new String[4];
		
		kartendeck = new ArrayList<Karte>();
		
		spielerhand = new ArrayList< ArrayList<Karte> >();
		//Initialisiert die Spielerhaende
		for(int i = 0; i < 4; i++) {
			spielerhand.add(new ArrayList<Karte>());
		}
		
		tisch = new Karte[4];
		
		letzterStich = new Karte[4];
		letzterStichGewinner = -1;
		letzterStichPunkte = 0;
		
		//Erstellt den Punktezaehler
		punkte = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
			punkte.add(0);
		}
		
		laufende = 0;
		
		initKarten();
	}
	
	/**
	 * Erstellt ein Model aus übergebenen Daten (client)
	 * @param spielerhand
	 * @param tisch
	 * @param letzerStich
	 * @param punkte
	 **/
	public Model(
			ArrayList< ArrayList<Karte> > spielerhand,
			Karte[] tisch,
			Karte[] letzterStich,
			ArrayList<Integer> punkte) {
		
		kartendeck = new ArrayList<Karte>();
		initKarten();
		
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
	 * Mischt das Kartendeck zehn mal
	 */
	public void mischen() {
		mischen(10);
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
	public Karte[] gibTisch() {
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
		int stelleKarte = -1;
		for(int i = 0; i < spielerhand.get(spielerID).size(); i++) {
			if(karte.vergleiche(spielerhand.get(spielerID).get(i))) {
				stelleKarte = i;
			}
		}
		
		if(stelleKarte != -1) {
			tisch[spielerID] = karte;
			
			//Der Spieler haelt die Karte nicht mehr
			spielerhand.get(spielerID).remove(stelleKarte);
			
		} else {
			throw new Exception("Der Spieler besitzt diese Karte nicht!");
		}
		
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
		
		letzterStichGewinner = gewinnerID;
		letzterStichPunkte = punkteStich;
	}
	
	/**
	 * Gibt den letzten Stich zurück
	 * @return letzten Stich
	 */
	public Karte[] gibLetztenStich(){
		return letzterStich;
	}
	/**
	 * Gibt den Gewinner des letzten Stichs zurück
	 * @return ID Gewinner
	 */
	public int gibLetzterStichGewinner() {
		return letzterStichGewinner;
	}
	
	/**
	 * Gibt die Punkte des letzten Stichs zurück
	 * @return Punkte Gewinner
	 */
	public int gibLetzterStichPunkte() {
		return letzterStichPunkte;
	}
	
	/**
	 * Gibt die ID des Ausspielers zurück
	 * @return
	 */
	public int gibAusspieler(int ID) {
		int zahlGespielteKarten = 0;
		for(int i = 0; i < 4; i++) {
			if(tisch[i] != null) {
				zahlGespielteKarten++;
			}
		}
		int spieler0 = ID + 1 - zahlGespielteKarten;
		if(spieler0 < 0) {
			spieler0 += 4;
		}
		if(spieler0 > 3) {
			spieler0 -= 4;
		}
		
		return spieler0;
	}
	
	/**
     * Stellt fest, wer spielt
     * @param m1
     * @param m2
     * @return
     */
    public modus werSpielt(modus m1, modus m2) {
    	//Ist einer der Modi null wird der andere zurückgegeben
    	if(m1 == null) return m2;
    	if(m2 == null) return m1;
    	
    	//Wenn jemand nichts spielt
    	//Achtung!! spielen beide nichts, so kann es sein, dass am Ende 
    	//modus.NICHTS an den Server zurückgegeben wird!
    	if(m1.equals(modus.NICHTS)) return m2;
    	if(m2.equals(modus.NICHTS)) return m1;
    	
    	int nr1 = m1.ordinal();
    	int nr2 = m2.ordinal();
    	
    	//stellt alle Sauspiele und Hochzeiten gleich
    	if(nr1 < 5) nr1 = 0;
    	if(nr2 < 5) nr2 = 0;
    	
    	//stellt alle Solos gleich
    	if(nr1 > 6 && nr1 < 11) nr1 = 7;
    	if(nr2 > 6 && nr2 < 11) nr2 = 7;
    	
    	//stellt alle SoloDu's gleich
    	if(nr1 > 12 && nr1 < 17) nr1 = 13;
    	if(nr2 > 12 && nr2 < 17) nr2 = 13;
    	
    	//Nur wenn der zweite Modus besser ist wird dieser zurückgegeben
    	if(nr1 > nr2) return m1;
    	else return m2;
    }
    
    /**
     * Gibt die Punkte aller Spieler zurück
     * @return
     */
    public ArrayList<Integer> gibPunkte() {
    	return punkte;
    }

    /**
     * Führt eine Hochzeit durch
     * @param spielt
     * @param mitspieler
     * @param angebot
     * @param angenommen
     */
	public void hochzeit(int spielt, int mitspieler, Karte angebot, Karte angenommen) {
		//Position von angebotener und angenommener Karte
		int an = -1;
		int bn = -1;
		
		Karte a = new Karte(angebot.gibFarbe(), angebot.gibWert());
		Karte test = new Karte(null, null);
		for(int i = 0; i < 6; i++) {
			test = new Karte(spielerhand.get(spielt).get(i).gibFarbe(), spielerhand.get(spielt).get(i).gibWert());
			if(test.vergleiche(a)) {
				//Karte von der Hand des Spielers nehmen
				an = i;
				break;
			}
		}
		
		Karte b = new Karte(angenommen.gibFarbe(), angenommen.gibWert());
		Karte gegenTest = new Karte(null, null);
		for(int i = 0; i < 6; i++) {
			gegenTest = new Karte(spielerhand.get(mitspieler).get(i).gibFarbe(), spielerhand.get(mitspieler).get(i).gibWert());
			if(gegenTest.vergleiche(b)) {
				//Karte von der Hand des Spielers nehmen
				bn = i;
				break;
			}
		}
		
		spielerhand.get(spielt).set(an, gegenTest);
		spielerhand.get(mitspieler).set(bn, test);
	}
	
	/**
	 * Setzt den Namen eines Spielers
	 * @param ID
	 * @param name
	 */
	public void setzeName(int ID, String name) {
		spielernamen[ID] = name;
	}
	
	/**
	 * Gibt alle Spielernamen zurück
	 * @return
	 */
	public String[] gibNamen() {
		return spielernamen;
	}
	
	/**
	 * [DEBUG] Gibt die Karten eines Spielers aus
	 * @param spielerID
	 */
	public void ausgeben(int spielerID) {
		try {
			for(int i = 0; i < spielerhand.get(spielerID).size(); i++) {
				System.out.println(spielerhand.get(spielerID).get(i).gibFarbe());
				System.out.println(spielerhand.get(spielerID).get(i).gibWert());
			}
		} catch(Exception e) {
		}
	}

	/**
	 * Setzt die Punkte der einzelnen Spieler
	 * @param punkte
	 */
	public void setPunkte(ArrayList<Integer> punkte) {
		this.punkte = punkte;
	}
	
	/**
	 * Errechnet die Anzahl der Laufenden. Bei weniger als 3 Laufenden wird 0 gesetzt
	 * @param spieler
	 * @param mitspieler
	 * @param modus
	 */
	public void errechneLaufende(int spieler, int mitspieler, modus modus) {
		Control regeln = new Regelwahl().wahl(modus);
		laufende = regeln.laufende(spieler, mitspieler, this);
		if(laufende < 3) {
			laufende = 0;
		}
	}
	
	public int gibLaufende() {
		return laufende;
	}
	
}
