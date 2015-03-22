package regeln;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Hochzeit implements Control {
	
	public int sieger(Model m, int erster) {
		boolean trumpf = false;
		Karte[] gespielt = m.gibTisch();
		for(int i = 0; i < 4; i++){
			if(istTrumpf(gespielt[i].gibWert(), gespielt [i].gibFarbe())){
				trumpf = true;
			}
		}
		if(!trumpf) {
			return keinTrumpf(gespielt, erster);
		}
		return schonTrumpf(gespielt);
	}
	

	/**
	 * Ermittelt den Sieger eines Stichs, wenn ein Trumpf gespielt wurde
	 * @param gespielt
	 * @return
	 */
	private int schonTrumpf(Karte[] tisch) {
		int sieger = -1;
		
		for(int i = 0; i < 4; i++) {
			//Es muss ein Trumpf vorhanden sein
			if(istTrumpf(tisch[i].gibWert(), tisch[i].gibFarbe())) {
				if(sieger == -1) {
					sieger = i;
				} else {
					int diff = kartenRangliste(tisch[i].gibWert()) - kartenRangliste(tisch[sieger].gibWert());
					//Wenn der Wert der Karte höher als der der alten ist
					if(diff > 0) {
						sieger = i;
					} else {
						//Beide Karten haben den gleichen Wert 
						if(diff == 0) {
							//Wenn die Farbe der Karte höherwertig ist
							//siehe lib.Karte: 1 = EICHEL, 2 = GRAS, 3 = HERZ, 4 = SCHELLEN
							if(tisch[i].gibFarbe().ordinal() < tisch[sieger].gibFarbe().ordinal()) {
								sieger = i;
							}
						}
						//Ansonste sticht die Karte tisch[i] nicht
					}
				}
			}
		}
		
		return sieger;
	}	

	/**
	 * Ermittelt den Sieger, wenn kein Trumpf gespielt wurde
	 * @param tisch
	 * @return
	 */
	private int keinTrumpf(Karte[] gespielt, int erster) {
		//Derjenige, der ausgekartet hat wird zuerst abgerufen
		int spieler = erster;

		for(int i = 1; i < 4; i++){
			
			if(gespielt[(i + erster) % 4].gibFarbe().equals(gespielt[erster].gibFarbe())){
				if(kartenRangliste(gespielt[(i + erster) % 4].gibWert()) 
						> kartenRangliste(gespielt[spieler].gibWert())) {
					spieler = (i + erster) % 4;
				}
			}
		}
		return spieler;
	}
	
	/**
	 * Gibt einen Wert zurück, der dem Stellenwert der Karte entspricht
	 * @param wert
	 * @return
	 */
	private int kartenRangliste(Karte.wert wert) {
		switch(wert) {
		case NEUN:
			return 0;
		case KONIG:
			return 1; 
		case ZEHN:
			return 2;
		case SAU:
			return 3;
		case UNTER:
			return 4;
		case OBER:
			return 5;
		}
		
		return -1;
	}
	
	public boolean erlaubt(Model m, int ID) {
		
		Karte[] tisch = m.gibTisch();
		Karte angespielt;
		
		//Findet die Karte, die zuerst gespielt wurde
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
		angespielt = tisch[spieler0];
		
		//Es wurde nichts angespielt
		if(angespielt == null || ID == spieler0){
			return true;
		}
		//Es wurde Trumpf angespielt
		if(istTrumpf(angespielt.gibWert(), angespielt.gibFarbe())) {
			if(istTrumpf(tisch[ID].gibWert(), tisch[ID].gibFarbe())) {
				return true;
			} else {
				if(keinTrumpf(m, ID)) {
					return true;
				}
			}
			return false;
		}
		//Es wurde eine Farbe angespielt
		if(tisch[ID].gibFarbe().equals(angespielt.gibFarbe()) 
				&& !istTrumpf(tisch[ID].gibWert(), tisch[ID].gibFarbe())) {
			//Es wurde die passende Farbe gespielt
			return true;
		} else { 
			if(keineFarbe(angespielt.gibFarbe(), m, ID)) {
				//Der Spieler hat die Farbe nicht
				return true;
			}
		}
		return false;
	}

	/**
	 * Untersucht, ob der Spieler die angespielte Farbe nicht auf der Hand hat
	 * @param farbe
	 * @param m
	 * @param ID
	 * @return
	 */
	private boolean keineFarbe(Karte.farbe farbe, Model m, int ID) {
		ArrayList<Karte> y = (ArrayList<Karte>) m.gibSpielerKarten(ID).clone();
		y.add(m.gibTisch()[ID]);
		
		for(int i = 0; i < y.size(); i++) {
			Karte karte = y.get(i);
			if(karte.gibFarbe().equals(farbe) && !istTrumpf(karte.gibWert(), karte.gibFarbe())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Untersucht, ob der Spieler einen Trumpf auf der Hand hat
	 * @param m
	 * @param ID
	 * @return
	 */
	private boolean keinTrumpf(Model m, int ID) {
		ArrayList<Karte> y = (ArrayList<Karte>) m.gibSpielerKarten(ID).clone();
		y.add(m.gibTisch()[ID]);
		
		for(int i = 0; i < y.size(); i++) {
			if(istTrumpf(y.get(i).gibWert(), y.get(i).gibFarbe())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Untersucht, ob die Übergebenen Werte für einen Trumpf stehen
	 * @param wert
	 * @param farbe
	 * @return
	 */
	public static boolean istTrumpf(Karte.wert wert, Karte.farbe farbe) {
		if (wert.equals(Karte.wert.OBER) 
				|| wert.equals(Karte.wert.UNTER) 
				|| farbe.equals(Karte.farbe.HERZ)) {
			return true;
		}
		return false; 
	}

	public int mitspieler(Model m) {
		return -1;
	}
	
	public boolean hochzeitMoeglich(ArrayList<Karte> spielerhand) {
		int anzahl_Truempfe = 0;
		for(int i = 0; i < spielerhand.size(); i++) {
			if(istTrumpf(spielerhand.get(i).gibWert(), spielerhand.get(i).gibFarbe())) {
				anzahl_Truempfe++;
			}
		}
		
		if(anzahl_Truempfe == 1) {
			return true;
		}
		
		return false;
	}

	public static boolean hochzeitMoeglich(Model m, int position, Karte angebot){
		if(!istTrumpf(angebot.gibWert(), angebot.gibFarbe())) {
			return false;
		}
		ArrayList<Karte> hand = m.gibSpielerKarten(position);
		for(int i = 0; i < hand.size(); i++){
			if(istTrumpf(hand.get(i).gibWert(), hand.get(i).gibFarbe()) 
					&& !hand.get(i).vergleiche(angebot)) {
				return false;
			} 
		}
		return true;
	}
}
