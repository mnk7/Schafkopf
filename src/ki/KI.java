package ki;

import java.util.ArrayList;
import java.util.Random;

import ki.data.Datenbank;
import ki.data.Spielzug;
import regeln.Control;
import lib.Karte;
import lib.Model;

public abstract class KI {
	
	protected int ID;
	protected int spielt;
	protected int mitspieler;
	protected Control regeln;
	protected Datenbank db;
	//Speichert den Spielzug und gibt ihn an die Datenbank zurück
	protected Spielzug letzterSpielzug;
	protected ArrayList<Spielzug> gespielteSpielzuege;
	protected ArrayList<Karte> gespielteTruempfe;
	protected boolean neuerSpielzug;
	//Die ID desjenigen, der in einer Runde ausgespielt hat
	protected int ausspieler;
	
	//Gibt an, welches Risiko der Spieler bereit ist einzugehen
	//Je niedriger der Wert, desto mehr Risiko wird eingegangen, wobei gilt:
	//Ein hoher Trumpf (Wenz beim Wenz, Ober beim Sauspiel) = 3
	//Ein niedriger Trumpf (As beim Wenz, Unter beim Sauspiel) = 2
	//Sonstiges (z.B. Zehner beim Wenz, Farbtrumpf beim Sauspiel) = 1
	protected int risiko;
	
	//Gibt an, welches Handicap ein Bot hat, je größer das Handicap, desto mehr Fehler
	protected int handicap;
	//Speichert die Zahl der Trümpfe
	protected int trumpfzahl;
	//Speichert die Qualität der Trümpfe
	protected int trumpfsumme;
	
	public KI(int ID) {
		this.ID = ID;
		gespielteTruempfe = new ArrayList<Karte>();
		gespielteSpielzuege = new ArrayList<Spielzug>();
		ausspieler = -1;
		neuerSpielzug = true;
		spielt = -1;
		mitspieler = -1;
		risiko = 0;
	}
	
	//gibt der KI ihre Datenbank
	public void setzeDB(Datenbank db) {
		this.db = db;
	}
	
	/**
	 * Setzt die Farbe in Solo und in Sauspiel
	 * @param farbe
	 */
	public void setzeFarbe(Karte.farbe farbe) {
	}

	/**
	 * Gibt an, ob der Spieler kontra gibt
	 * @param model
	 * @return
	 */
	public boolean kontra(Model model) {
		untersuche(model.gibSpielerKarten(ID));
		if(trumpfsumme > risiko) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Ermittelt Trumpfzahl und Trumpfsumme
	 * @param karten
	 */
	protected abstract void untersuche(ArrayList<Karte> karten);
	
	/**
	 * Übernimmt, wer spielt
	 * @param spielt
	 * @param mitspieler
	 */
	public void spieler(int spielt, int mitspieler) {
		this.spielt = spielt;
		this.mitspieler = mitspieler;
	}
	
	/**
	 * Setzt die ID des Bots
	 * @param ID
	 */
	public void setzeID(int ID) {
		this.ID = ID;
	}

	/**
	 * Spielt eine Karte
	 * @param model
	 * @return
	 */
	public Model spiel(Model model) {	
		//Sollte erst aufgerufen werden nachdem man gespielt hat
		ausspieler = model.gibAusspieler(ID) - 1;
		
		if(handicap > 2) {
			return spieleZufaellig(model);
		} else if(handicap == 1) {
			return spieleMitDB(model);
		} else {
			spieleMitKI(model);
		}
		return null;
	}
	
	/**
	 * Spielt zufällig Karten
	 * @param model
	 * @return
	 */
	private Model spieleZufaellig(Model model) {
		//Arbeitet nach dem DAB-Prinzip (Dümmster anzunehmender Bot) und spielt zufällig eine Karte
		ArrayList<Karte> spielerkarten = model.gibSpielerKarten(ID);
		//Speichert alle erlaubten Karten
		ArrayList<Karte> erlaubt = new ArrayList<Karte>();
		
		try {
			for(int i = spielerkarten.size() - 1; i >= 0; i--) {
				//Legt eine Karte auf den Tisch
				model.setTisch(ID, spielerkarten.get(i));
				
				if(regeln.erlaubt(model, ID)) {
					//Prüft, ob der Zug legal ist
					erlaubt.add(model.gibTisch()[ID]);
					//Karte gefunden
				}
				model.undo(ID);
				//Die zurückgelegte Karte wird nach ganz hinten gerückt, der Rest rückt auf
				spielerkarten = model.gibSpielerKarten(ID);
			}
			//Vorerst einfach die erste erlaubte spielen
			model.setTisch(ID, erlaubt.get(0));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * Spielt aufgrund der Datenbank
	 * @param model
	 * @return
	 */
	private Model spieleMitDB(Model model) {
		ArrayList<Karte> hand = (ArrayList<Karte>) model.gibSpielerKarten(ID).clone();
		
		String zu_spielende = db.welcheSpielen(model.gibTisch()[ausspieler],
											ID,
											spielt,
											mitspieler,
											hand,
											ausspieler,
											false, //Tout vorerst nicht berücksichtigt
											gespielteTruempfe,
											model.gibTisch().clone());
		
		if(zu_spielende == null) {
			neuerSpielzug = true;
			//Noch keine Daten vorhanden -> zufällige Karte spielen
			return spieleZufaellig(model);
		} else {
			neuerSpielzug = false;
			//Schon Daten zum Spielzug vorhanden
			for(int i = 0; i < hand.size(); i++) {
				if(hand.get(i).gibString().equals(zu_spielende)) {
					try {
						//Entsprechende Karte spielen
						model.setTisch(ID, hand.get(i));
						break;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			gespielteSpielzuege.add(db.gibLetztenSpielzug());
		}
		
		return model;
	}
	
	/**
	 * Nutzt die KI (in späteren Versionen)
	 * @param model
	 * @return
	 */
	private Model spieleMitKI(Model model) {
		return spieleMitDB(model);
	}
	
	/**
	 * Speichert nach jeder Runde die gespielten Truempfe
	 * @param model
	 */
	public void stich(Model model) {
		ausspieler = model.gibAusspieler(ID);
		
		//Empfängt model nach jedem Stich
		Karte[] tisch = model.gibLetztenStich();
		for(int i = 0; i < tisch.length; i++) {
			if(regeln.istTrumpf(tisch[i].gibWert(), tisch[i].gibFarbe())) {
				gespielteTruempfe.add(tisch[i]);
			}
		}
		
		int punkte = model.gibLetzterStichPunkte();
		int sieger = model.gibLetzterStichGewinner();
		
		if(ID == spielt || ID == mitspieler) {
			if(sieger != spielt && sieger != mitspieler) {
				punkte *= -1;
			}
		} else {
			if(sieger == spielt || sieger == mitspieler) {
				punkte *= -1;
			}
		}
		
		if(neuerSpielzug) {
			ArrayList<Karte> spielerhand = (ArrayList<Karte>) model.gibSpielerKarten(ID).clone();
			spielerhand.add(tisch[ID]);
			
			letzterSpielzug = new Spielzug(tisch[ausspieler],
					ausspieler,
					ID,
					(spielt*10 + mitspieler),
					spielerhand,
					false,
					gespielteTruempfe,
					tisch);
			
			db.einfuegen(letzterSpielzug);
			letzterSpielzug.erinnern(tisch[ID], punkte);
			letzterSpielzug = null;
		} else {
			gespielteSpielzuege.get(gespielteSpielzuege.size() - 1)
					.erinnern(model.gibTisch()[ID], punkte);
		}
	}

	/**
	 * Empfängt die Sieger IDs
	 * @param s1
	 * @param s2
	 */
	public void sieger(int s1, int s2) {
		boolean gewonnen = false;
		
		if(s1 == ID || s2 == ID) {
			gewonnen = true;
		}
		
		for(Spielzug s: gespielteSpielzuege) {
			s.erinnereGewonnen(gewonnen);
		}
	}
}
 