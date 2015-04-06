package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Spielauswahl {
	
	private Karte.farbe farbe;
	
	/**
	 * Gibt an, was die KI spielen will
	 * @param m
	 * @param ID
	 * @return
	 */
	public modus wasSpielen(Model m, int ID) {
		//Aufräumen
		this.farbe = null;
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
		if(spielSI(aufteilung, karten)) {
			return modus.SI;
		}
		
		//Kann ein Solo gespielt werden?
		modus mod = spielSOLO(aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//Kann ein Wenz gespielt werden?
		mod = spielWENZ(aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//Kann ein Geier gespielt werden?
		mod = spielGEIER(aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//ein Sauspiel?
		mod = spielSAUSPIEL(aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//eine Hochzeit?
		mod = spielHOCHZEIT(aufteilung, karten);
		if(mod != null) {
			return mod;
		}
		
		//Vorerst spielen die Bots nicht
		return modus.NICHTS; 
	}
	
	private modus spielHOCHZEIT(int[] aufteilung, boolean[][] karten) {
		return null;
	}

	private modus spielSAUSPIEL(int[] aufteilung, boolean[][] karten) {
		return null;
	}

	/**
	 * Untersucht, ob ein Geier gespielt wird
	 * @param aufteilung
	 * @param karten
	 * @return Geier-Modus oder null
	 */
	private modus spielGEIER(int[] aufteilung, boolean[][] karten) {
		int wert = 0;
		for(int i = 0; i < 4; i++) {
			//Geier addieren
			if(karten[0][i]) {
				wert += (6 + 3 - i);
			}
			//Sau addieren
			if(karten[2][i]) {
				wert += 4;
				//Passender Zehner?
				if(karten[3][i]) {
					wert += 4;
				}
			} else if(karten[3][i]) {
				//einzelne Zehner
				wert += 2;
			}
		}
		//Geier-Du bei den zwei höchsten Geiern (9 + 8) und 2 Sauen (4x2) und 1 passenden Zehner => 29
		if(karten[0][0] && karten[0][1] && aufteilung[4] == 0 && aufteilung[5] == 0 && wert >= 29) {
			return modus.GEIERdu;
		} else {
			//Geier nicht mehr bei 2 Geiern (7 + 6) und 2 Sau (4x2) und 1 einzelnem Zehner (2) => 23
			if(aufteilung[0] > 1 && wert >= 24) {
				return modus.GEIER;
			}
			//Geier nicht mehr bei 1 Geier (9) und 2 Sau (4x2) und 1 passenem Zehner (4) => 21
			if(aufteilung[0] == 1 && wert >= 22) {
				return modus.GEIER;
			}
		}
		return null;
	}

	/**
	 * Untersucht, ob ein Wenz gespielt wird
	 * @param aufteilung
	 * @param karten
	 * @return Wenz-Modus oder null
	 */
	private modus spielWENZ(int[] aufteilung, boolean[][] karten) {
		int wert = 0;
		for(int i = 0; i < 4; i++) {
			//Wenz addieren
			if(karten[1][i]) {
				wert += (6 + 3 - i);
			}
			//Sau addieren
			if(karten[2][i]) {
				wert += 4;
				//Passender Zehner?
				if(karten[3][i]) {
					wert += 4;
				}
			} else if(karten[3][i]) {
				//einzelne Zehner
				wert += 2;
			}
		}
		//Wenz-Du bei den zwei höchsten Wenzen (9 + 8) und 2 Sauen (4x2) und 1 passenden Zehner (4) => 29
		if(karten[1][0] && karten[1][1] && aufteilung[4] == 0 && aufteilung[5] == 0&& wert >= 29) {
			return modus.WENZdu;
		} else {
			//Wenz nicht mehr bei 2 Wenzen (7 + 6) und 2 Sau (4x2) und 1 einzelnem Zehner (2) => 23
			if(aufteilung[1] > 1 && wert >= 24) {
				return modus.WENZ;
			}
			//Wenz nicht mehr bei 1 Wenz (9) und 2 Sau (4x2) und 1 passenem Zehner (4) => 21
			if(aufteilung[1] == 1 && wert >= 22) {
				return modus.WENZ;
			}
		}
		return null;
	}
	
	/**
	 * Untersucht, ob ein Solo gespielt wird. 
	 * @param aufteilung
	 * @return Solo-Modus oder null
	 */
	private modus spielSOLO(int[] aufteilung, boolean[][] karten) {
		int wert = 0;
		//Welche Farbe soll gespielt werden?
		int farbe = -1;
		//Wie viele Karten dieser Farbe hat der Spieler?
		int anzahlFarbe = 0;
		int anzahl = 0;
		//Welche Farbe?
		for(int i = 0; i < 4; i++) {
			//Karten ohne Ober und Unter
			for(int j = 1; j < 6; j++) {
				if(karten[j][i]) {
					anzahl++;
				}
			}
			if(anzahl > anzahlFarbe) {
				farbe = i;
				anzahlFarbe = anzahl;
				anzahl = 0;
			}
		}
		//Wenn der Spieler keine Farbe hat
		if(farbe == -1) {
			//Gras-Solo
			farbe = 1;
		}
		
		for(int i = 0; i < 4; i++) {
			//Ober
			if(karten[0][i]) {
				wert += (12 + 4 - i);
			}
			//Unter
			if(karten[1][i]) {
				wert += (8 + 4 - i);
			}
			//Farbe (As = 2 bis Neun = 5)
			if(karten[1 + i][farbe]) {
				wert += (4 + 4 - i);
			}
			//Sonstige Sau
			if(karten[2][i]) {
				wert += 4;
				//Passende Zehner
				if(karten[3][i]) {
					wert += 4;
				}
			} else if(karten[3][i]) {
				//Sonstige Zehner
				wert += 2;
			}
		}
		
		String mod;
		//Solo-Du: 2 höchste Ober (16 + 15), 2 Wenz (12 + 10), 2 Sau/pass. Zehner (4x2) => 61
		if(karten[0][0] && karten[0][1] && aufteilung[4] == 0 && aufteilung[5] == 0 && wert >= 61) {
			mod = "SOLO" + farbe(farbe) + "DU";
			this.farbe = Karte.farbe.valueOf(farbe(farbe).toUpperCase());
			return modus.valueOf(mod);
		} else {
			//Solo: minimal 2 Ober (14 + 13), 2 Wenz (10 + 9), 1 Sau/pass. Zehner (4) => 50
			//oder: 1 Ober (13), 3 Wenz (12 + 11 + 10), 2 Sau/pass. Zehner (4x2) => 54
			if((aufteilung[0] > 1 && aufteilung[1] > 1 && wert >= 50) ||
					(aufteilung[0] == 1 && aufteilung[1] > 2 && wert >= 53)) {
				mod = "SOLO" + farbe(farbe);
				this.farbe = Karte.farbe.valueOf(farbe(farbe).toUpperCase());
				return modus.valueOf(mod);
			}
		}
		return null;
	}

	/**
	 * Untersucht, ob ein Sie gespielt wird
	 * @param aufteilung
	 * @return
	 */
	private boolean spielSI(int[] aufteilung, boolean[][] karten) {
		if(aufteilung[0] == 4 && aufteilung[1] == 2) {
			if(karten[1][0] && karten[1][1]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gibt den Namen der passenden Farbe
	 * @param f
	 * @return
	 */
	private String farbe(int f) {
		switch(f) {
		case 0:
			return "eichel";
		case 1:
			return "gras";
		case 2:
			return "herz";
		case 3:
			return "schellen";
		}
		return null;
	}
	
	public Karte.farbe gibFarbe() {
		return farbe;
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