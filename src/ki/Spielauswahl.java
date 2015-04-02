package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Spielauswahl {
	
	public modus wasSpielen(Model m) {
		//Vorerst spielen die Bots nicht
		return modus.NICHTS; 
	}
	
	/**
	 * Gibt die zum Modus passende KI zurück
	 * Wenn ein Sauspiel oder Solo gespielt wird, muss noch die Farbe gesetzt werden
	 * @param m
	 * @return
	 */
	public KI gibKI(modus m, int ID, int handicap) {
		KI ki = null;
		
		switch(m) {
		case HOCHZEIT:
			ki = new Hochzeit(ID, handicap);
			break;
		case SAUSPIELeichel:
			ki = new Sauspiel(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.EICHEL);
			break;
		case SAUSPIELgras:
			ki = new Sauspiel(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.GRAS);
			break;
		case SAUSPIELherz:
			ki = new Sauspiel(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.HERZ);
			break;
		case SAUSPIELschellen:
			ki = new Sauspiel(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.SCHELLEN);
			break;
		case GEIER:
		case GEIERdu:
			ki = new Geier(ID, handicap);
			break;
		case WENZ:
		case WENZdu:
			ki = new Wenz(ID, handicap);
			break;
		case SOLOeichel:
		case SOLOeichelDU:
			ki = new Solo(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.EICHEL);
			break;
		case SOLOgras:
		case SOLOgrasDU:
			ki = new Solo(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.GRAS);
			break;
		case SOLOherz:
		case SOLOherzDU:
			ki = new Solo(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.HERZ);
			break;
		case SOLOschellen:
		case SOLOschellenDU:
			ki = new Solo(ID, handicap);
			ki.setzeFarbe(lib.Karte.farbe.SCHELLEN);
			break;
		case SI:
			//Wird nicht gespielt
			break;
		}
		return ki;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	public boolean klopfen(Model model, int ID) {
		ArrayList<Karte> karten = model.gibSpielerKarten(ID);
		int ober = 0;
		int unter = 0;
		int as = 0;
		
		for(int i = 0; i < karten.size(); i++) {
			Karte k = karten.get(i);
			if(k.gibWert().equals(Karte.wert.OBER)) {
				ober++;
			} 
			if(k.gibWert().equals(Karte.wert.UNTER)) {
				unter++;
			}
			if(k.gibWert().equals(Karte.wert.SAU)) {
				as++;
			}
		}
		
		//Bei zwei Obern oder Untern klofen
		if(ober >= 2 || unter >= 2) {
			return true;
		} else {
			//Ein Ober oder Unter und 2 Sau oder alles einmal
			if((ober + unter + as) == 3) {
				return true;
			} else {
				return false;
			}
		}
	}
}
