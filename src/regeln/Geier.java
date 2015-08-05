package regeln;
import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Geier implements Control {
	
	public int sieger(Model m, int erster) {
		boolean unter = false;
		Karte[] gespielt = m.gibTisch();
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.OBER){
				unter = true;
			}
		}
		if(!unter) {
			return keinTrumpf(gespielt, erster);
		}
		return schonTrumpf(gespielt);
	}
	
	private int schonTrumpf(Karte[] gespielt){
		int spieler = -1;
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.OBER){
				
				if(spieler == -1) {
					spieler = i;
				} else {
					//siehe lib.Karte: 1 = EICHEL, 2 = GRAS, 3 = HERZ, 4 = SCHELLEN
					if(gespielt[i].gibFarbe().ordinal() < gespielt[spieler].gibFarbe().ordinal()) {
						spieler = i;
					}
				}
			}
		}
		return spieler;
	}
	
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
		case UNTER:
			return 1;
		case KONIG:
			return 2; 
		case ZEHN:
			return 3;
		case SAU:
			return 4;
		case OBER:
			return 5;
		}
		
		return -1;
	}
	
	public boolean erlaubt(Model m, int ID) {
		Karte[] tisch = m.gibTisch();
		Karte angespielt;
		int spieler0 = m.gibAusspieler(ID);
		
		//Findet die Karte, die zuerst gespielt wurde
		angespielt = tisch[spieler0];
		
		//Es wurde nichts angespielt
		if(angespielt == null || ID == spieler0){
			return true;
		}
		if(m.gibSpielerKarten(ID).size() == 0) {
			return true;
		}
		//Es wurde ein Unter angespielt
		if(angespielt.gibWert().equals(Karte.wert.OBER)){
			if(tisch[ID].gibWert().equals(Karte.wert.OBER)){
				return true;
			} else if(keinTrumpf(m, ID)){
				return true;
			}
			return false;
		}
		//Es wurde eine Farbe angespielt
		if(tisch[ID].gibFarbe().equals(angespielt.gibFarbe()) 
				&& !tisch[ID].gibWert().equals(Karte.wert.OBER)){
			//Es wurde die passende Farbe gespielt
			return true;
		} else if(keineFarbe(angespielt.gibFarbe(), m, ID)){
			//Der Spieler hat die Farbe nicht
			return true;
		}
		return false;
	}

	public int mitspieler(Model m) {
		return 4;
	}
	
	/**
	 * Untersucht, ob der Spieler einen Trumpf auf der Hand hat
	 * @param m
	 * @param ID
	 * @return
	 */
	private boolean keinTrumpf(Model m, int ID){
		ArrayList<Karte> y = (ArrayList<Karte>) m.gibSpielerKarten(ID).clone();
		y.add(m.gibTisch()[ID]);
		
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).gibWert().equals(Karte.wert.OBER)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Untersucht, ob der Spieler die angespielte Farbe nicht auf der Hand hat
	 * @param farbe
	 * @param m
	 * @param ID
	 * @return
	 */
	private boolean keineFarbe(Karte.farbe farbe, Model m, int ID){
		ArrayList<Karte> y = (ArrayList<Karte>) m.gibSpielerKarten(ID).clone();
		y.add(m.gibTisch()[ID]);
		
		for(int i = 0; i < y.size(); i++){
			Karte karte = y.get(i);
			if(karte.gibFarbe().equals(farbe) && !karte.gibWert().equals(Karte.wert.OBER)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean istTrumpf(Karte.wert wert, Karte.farbe farbe) {
		if(wert.equals(Karte.wert.OBER)) {
			return true;
		}
		return false;
	}
	
	public int laufende(int spieler, int mitspieler, Model model) {
		ArrayList<Karte> spielerkarten = model.gibSpielerKarten(spieler);
		
		//Für jeden enthaltenen Trumpf gibt es ein Feld
		boolean[] enthalten = new boolean[4];
		for(int i = 0; i < 4; i++) {
			enthalten[i] = false;
		}
		
		for(int i = 0; i < spielerkarten.size(); i++) {
			Karte k = spielerkarten.get(i);
			int stelle;
			
			if(k.gibWert().equals(Karte.wert.OBER)) {
				stelle = k.gibFarbe().ordinal();
				enthalten[stelle] = true;
			}
		}
		
		int laufende = 0;
		for(int i = 0; i < 4; i++) {
			if(enthalten[i]) {
				laufende++;
			} else {
				break;
			}
		}
		
		return laufende;
	}
}
