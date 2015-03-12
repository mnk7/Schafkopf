package ki;

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
	public KI gibKI(modus m, int ID) {
		KI ki = null;
		
		switch(m) {
		case SAUSPIELeichel:
			ki = new Sauspiel(ID);
			ki.setzeFarbe(lib.Karte.farbe.EICHEL);
			break;
		case SAUSPIELgras:
			ki = new Sauspiel(ID);
			ki.setzeFarbe(lib.Karte.farbe.GRAS);
			break;
		case SAUSPIELherz:
			ki = new Sauspiel(ID);
			ki.setzeFarbe(lib.Karte.farbe.HERZ);
			break;
		case SAUSPIELschellen:
			ki = new Sauspiel(ID);
			ki.setzeFarbe(lib.Karte.farbe.SCHELLEN);
			break;
		case GEIER:
		case GEIERdu:
			ki = new Geier(ID);
			break;
		case WENZ:
		case WENZdu:
			ki = new Wenz(ID);
			break;
		case SOLOeichel:
		case SOLOeichelDU:
			ki = new Solo(ID);
			ki.setzeFarbe(lib.Karte.farbe.EICHEL);
			break;
		case SOLOgras:
		case SOLOgrasDU:
			ki = new Solo(ID);
			ki.setzeFarbe(lib.Karte.farbe.GRAS);
			break;
		case SOLOherz:
		case SOLOherzDU:
			ki = new Solo(ID);
			ki.setzeFarbe(lib.Karte.farbe.HERZ);
			break;
		case SOLOschellen:
		case SOLOschellenDU:
			ki = new Solo(ID);
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
	public boolean klopfen(Model model) {
		return false;
	}
}
