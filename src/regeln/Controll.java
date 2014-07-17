package regeln;

import lib.Model;

public interface Controll {
	
	/**
	 * Bestimmt den Sieger eines Spiels
	 * @param model
	 * @return SpielerID des Siegers
	 */
	public int sieger(Model model);
	
	/**
	 * Bestimmt, ob ein Spielzug erlaubt ist und gibt das Ergebnis zurück
	 * @param model
	 * @return erlaubt
	 */
	public boolean erlaubt(Model model);
	
	/**
	 * Bestimmt einen eventuellen Mitspieler
	 * @param model
	 * @return mitspieler oder null
	 */
	public int mitspieler(Model model);

}
