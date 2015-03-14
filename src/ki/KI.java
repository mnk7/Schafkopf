package ki;

import java.util.ArrayList;

import regeln.Control;
import lib.Karte;
import lib.Model;

public abstract class KI {
	
	protected int ID;
	protected int spielt;
	protected int mitspieler;
	protected Control regeln;
	
	public KI(int ID) {
		this.ID = ID;
		spielt = -1;
		mitspieler = -1;
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
	public abstract boolean kontra(Model model);

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
		
		try {
			for(int i = 0; i < spielerkarten.size(); i++) {
				//Legt eine Karte auf den Tisch
				m.setTisch(ID, spielerkarten.get(i));
				
				if(regeln.erlaubt(m, ID)) {
					//Prüft, ob der Zug legal ist
					break;
					//Karte gefunden
				} else {
					m.undo(ID);
					//Die zurückgelegte Karte wird nach ganz hinten gerückt, der Rest rückt auf
					spielerkarten = m.gibSpielerKarten(ID);
					i--;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return m;
	}

}
 