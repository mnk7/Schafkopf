package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Spielauswahl {
	
	/**
	 * Gibt an, was die KI spielen will
	 * @param m
	 * @param ID
	 * @return
	 */
	public modus wasSpielen(Model m, int ID) {
		ArrayList<Karte> spielerkarten = m.gibSpielerKarten(ID);
		//Speichert wie die Karten aufgeteilt sind
		//0: Ober, 1: Unter, etc.
		int[] aufteilung = new int[6];
		for(int i = 0; i < 6; i++) {
			//Für Karte.wert siehe lib.Karte
			aufteilung[5 - spielerkarten.get(i).gibWert().ordinal()]++;
		}
		
		//Bildet ab, welche Karten der Spieler hat
		boolean[][] karten = new boolean[6][4];
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 4; j++) {
				karten[i][j] = false;
			}
		}
		for(int i = 0; i < 6; i++) {
			int wert = 5 - spielerkarten.get(i).gibWert().ordinal();
			int farbe = spielerkarten.get(i).gibFarbe().ordinal();
			karten[wert][farbe] = true;
		}
		
		//Zuerst wird untersucht, ob ein SI gespielt werden kann
		if(spielSI(spielerkarten, aufteilung, karten)) {
			return modus.SI;
		}
		
		//Kann ein Solo gespielt werden?
		modus mod = spielSOLO(spielerkarten, aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//Kann ein Wenz gespielt werden?
		mod = spielWENZ(spielerkarten, aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//Kann ein Geier gespielt werden?
		mod = spielGEIER(spielerkarten, aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//ein Sauspiel?
		mod = spielSAUSPIEL(spielerkarten, aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//eine Hochzeit?
		mod = spielHOCHZEIT(spielerkarten, aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//Vorerst spielen die Bots nicht
		return modus.NICHTS; 
	}
	
	private modus spielHOCHZEIT(ArrayList<Karte> spielerkarten, int[] aufteilung, boolean[][] karten) {
		return null;
	}

	private modus spielSAUSPIEL(ArrayList<Karte> spielerkarten, int[] aufteilung, boolean[][] karten) {
		return null;
	}

	/**
	 * Untersucht, ob ein Geier gespielt wird
	 * @param spielerkarten
	 * @param aufteilung
	 * @param karten
	 * @return Geier-Modus oder null
	 */
	private modus spielGEIER(ArrayList<Karte> spielerkarten, int[] aufteilung, boolean[][] karten) {
		//Geier-Du?
		//Mind. 2 Geier und 2 Sau
		if(karten[0][0] && karten[0][1] && aufteilung[2] > 1) {
			//Keine Neuner oder Könige
			if(aufteilung[5] + aufteilung[4] == 0) {
				return modus.GEIERdu;
			}
		}
		//Mind. 2 Ober
		if(aufteilung[0] > 1) {
			//Mind. eine Sau und insgesammt 4 Sau und Zehner
			if((aufteilung[2] + aufteilung[3]) > 3 && aufteilung[2] > 0) {
				return modus.GEIER;
			}
		}
		return null;
	}

	/**
	 * Untersucht, ob ein Wenz gespielt wird
	 * @param spielerkarten
	 * @param aufteilung
	 * @param karten
	 * @return Wenz-Modus oder null
	 */
	private modus spielWENZ(ArrayList<Karte> spielerkarten, int[] aufteilung, boolean[][] karten) {
		//Wenz-Du?
		//Mind. 2 Unter und 2 Sau
		if(karten[1][0] && karten[1][1] && aufteilung[2] > 1) {
			//Keine Neuner oder Könige
			if(aufteilung[5] + aufteilung[4] == 0) {
				return modus.WENZdu;
			}
		}
		//Mind. 2 Unter
		if(aufteilung[1] > 1) {
			//Mind. eine Sau und insgesammt 4 Sau und Zehner
			if((aufteilung[2] + aufteilung[3]) > 3 && aufteilung[2] > 0) {
				return modus.WENZ;
			}
		}
		return null;
	}
	
	/**
	 * Untersucht, ob ein Solo gespielt wird. 
	 * @param spielerkarten
	 * @param aufteilung
	 * @return Solo-Modus oder null
	 */
	private modus spielSOLO(ArrayList<Karte> spielerkarten, int[] aufteilung, boolean[][] karten) {
		//Mindestens 2 Ober und 2 Unter
		if(aufteilung[0] > 1 && aufteilung[1] > 1) {
			//Kann ein Du gespielt werden?
			//Hat der Spieler die höchsten Ober
			if(karten[0][0] && karten[0][1]) {
				//Und einen der höchsten Unter?
				if(karten[1][0] || karten[1][1]) {
					//Zwei Sau
					if(aufteilung[2] == 2) {
						
					}
				}
			}
		}
		return null;
	}

	/**
	 * Untersucht, ob ein Sie gespielt wird
	 * @param spielerkarten
	 * @param aufteilung
	 * @return
	 */
	private boolean spielSI(ArrayList<Karte> spielerkarten, int[] aufteilung, boolean[][] karten) {
		if(aufteilung[0] == 4 && aufteilung[1] == 2) {
			if(karten[1][0] && karten[1][1]) {
				return true;
			}
		}
		return false;
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