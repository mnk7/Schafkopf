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
	 * Spielt eine Karte
	 * @param model
	 * @return
	 */
	public Model spiel(Model model) {
		//Arbeitet nach dem DAB-Prinzip (Dümmster anzunehmender Bot) und spielt zufällig eine Karte
		ArrayList<Karte> spielerkarten = model.gibSpielerKarten(ID);
		//Speichert die erlaubten Karten
		ArrayList<Integer> erlaubt = new ArrayList<Integer>();
		
		try {
			for(int i = 0; i < spielerkarten.size(); i++) {
				//Legt eine Karte auf den Tisch
					model.setTisch(ID, spielerkarten.get(i));
				
				if(regeln.erlaubt(model, ID)) {
					//Prüft, ob der Zug legal ist und wenn ja, speichert die Karte
					erlaubt.add(i);
				}
				
				model.undo(ID);
			}
			
			if(erlaubt.size() > 2) {
				java.util.Random zufall = new java.util.Random();
				model.setTisch(ID, spielerkarten.get(
						erlaubt.get(
							zufall.nextInt(
								erlaubt.size()))));
			} else {
				model.setTisch(ID, spielerkarten.get(erlaubt.get(0)));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

}
 