package ki;

import lib.Karte;
import lib.Model;

public class Sauspiel extends KI {
	
	private Karte.farbe farbe;

	public Sauspiel(int ID) {
		super(ID);
	}
	
	public void setzeFarbe(Karte.farbe farbe) {
		this.farbe = farbe;
		regeln = new regeln.Sauspiel(farbe);
	}

	public boolean kontra(Model model) {
		return false;
	}

	public Model spiel(Model model) {
		//Zufällig eine Karte auswählen
		return super.spiel(model);
	}

}
