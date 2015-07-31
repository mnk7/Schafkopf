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
	protected Spielzug letzerSpielzug;
	
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
		
		Model m = model;
		//Arbeitet nach dem DAB-Prinzip (Dümmster anzunehmender Bot) und spielt zufällig eine Karte
		ArrayList<Karte> spielerkarten = m.gibSpielerKarten(ID);
		//Speichert alle erlaubten Karten
		ArrayList<Karte> erlaubt = new ArrayList<Karte>();
		
		try {
			for(int i = spielerkarten.size() - 1; i >= 0; i--) {
				//Legt eine Karte auf den Tisch
				m.setTisch(ID, spielerkarten.get(i));
				
				if(regeln.erlaubt(m, ID)) {
					//Prüft, ob der Zug legal ist
					erlaubt.add(m.gibTisch()[ID]);
					//Karte gefunden
				}
				m.undo(ID);
				//Die zurückgelegte Karte wird nach ganz hinten gerückt, der Rest rückt auf
				spielerkarten = m.gibSpielerKarten(ID);
			}
			//Vorerst einfach die erste erlaubte spielen
			m.setTisch(ID, erlaubt.get(0));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return m;
	}
	
	public void stich(Model model) {
		//Empfängt model nach jedem Stich
	}
}
 