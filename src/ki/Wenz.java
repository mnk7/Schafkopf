package ki;

import lib.Model;

public class Wenz extends KI {

	public Wenz(int ID) {
		super(ID);
		regeln = new regeln.Wenz();
	}

	public boolean kontra(Model model) {
		return false;
	}

	public Model spiel(Model model) {
		//Zufällig eine Karte auswählen
		return super.spiel(model);
	}

}
