package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Solo extends KI {
	
	private Karte.farbe farbe;

	public Solo(int ID, int handicap) {
		super(ID);
		this.handicap = handicap;
	}

	/**
	 * Muss unbedingt aufgerufen werden
	 * @param farbe
	 */
	public void setzeFarbe(Karte.farbe farbe) {
		this.farbe = farbe;
		regeln = new regeln.Solo(farbe);
	}

	public Model spiel(Model model) {
		//Zufällig eine Karte auswählen
		return super.spiel(model);
	}

	protected void untersuche(ArrayList<Karte> karten) {
		// TODO Auto-generated method stub
		
	}

}
