package regeln;

import lib.Karte;
import lib.Model;

public interface Control {
	
	/**
	 * Bestimmt den Sieger eines Spiels
	 * @param model
	 * @return SpielerID des Siegers
	 */
	public int sieger(Model m, int erster);
	
	/**
	 * Bestimmt, ob ein Spielzug erlaubt ist und gibt das Ergebnis zurück
	 * @param model
	 * @param ID
	 * @return erlaubt
	 */
	public boolean erlaubt(Model m, int ID);
	
	/**
	 * Bestimmt einen eventuellen Mitspieler
	 * @param model
	 * @return mitspieler oder null
	 */
	public int mitspieler(Model m);
	
	/**
	 * Bestimmt, ob eine Karte Trumpf ist
	 * @param wert
	 * @param farbe
	 * @return
	 */
	public boolean istTrumpf(Karte.wert wert, Karte.farbe farbe);

	/**
	 * Errechnet die Laufenden der Spieler.
	 * @param spieler
	 * @param mitspieler
	 * @param model
	 * @return
	 */
	public int laufende(int spieler, int mitspieler, Model model);

}
