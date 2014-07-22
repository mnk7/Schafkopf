package regeln;

import lib.Karte;
import lib.Model;


public class Hochzeit implements Controll {

	@Override
	public int sieger(Model model) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean erlaubt(Model model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int mitspieler(Model model) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Gibt zurück, ob die angegebene Karte ein Trumpf ist
	 * @param k
	 * @return
	 */
	public boolean istTrumpf(Karte k) {
		return true;
	}

	public boolean hochzeitMoeglich(Model model, int spielt, Karte angebot) {
		return false;
	}

}
