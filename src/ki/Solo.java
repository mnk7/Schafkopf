package ki;

import lib.Karte;
import lib.Model;

public class Solo extends KI {
	
	private Karte.farbe farbe;

	public Solo(int ID) {
		super(ID);
	}

	/**
	 * Muss unbedingt aufgerufen werden
	 * @param farbe
	 */
	public void setzeFarbe(Karte.farbe farbe) {
		this.farbe = farbe;
		regeln = new regeln.Solo(farbe);
	}
	
	public boolean kontra(Model model) {
		return false;
	}

	public Model spiel(Model model) {
		//Zufällig eine Karte auswählen
		return super.spiel(model);
	}

}
