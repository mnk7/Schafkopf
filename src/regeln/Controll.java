package regeln;

import lib.Model;

public interface Controll {
	
	/**
	 * Bestimmt den Sieger eines Spiels
	 * @param model
	 * @return
	 */
	public int sieger(Model model);
	
	/**
	 * Bestimmt einen eventuellen Mitspieler
	 * @param model
	 * @return mitspieler oder null
	 */
	public int mitspieler(Model model);

}
