package ki;

import lib.Model;

public class Geier extends KI {

	public Geier(int ID) {
		super(ID);
		regeln = new regeln.Geier();
	}

	public boolean kontra(Model model) {
		return false;
	}

	public Model spiel(Model model) {
		//Zufällig ausgewählte Karte
		return super.spiel(model);
	}

}
