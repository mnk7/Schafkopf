package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Sauspiel extends KI {
	
	private Karte.farbe farbe;

	public Sauspiel(int ID, int handicap) {
		super(ID);
		this.handicap = handicap;
	}
	
	public void setzeFarbe(Karte.farbe farbe) {
		this.farbe = farbe;
		regeln = new regeln.Sauspiel(farbe);
	}

	public Model spiel(Model model) {
		//Zufällig eine Karte auswählen
		return super.spiel(model);
	}

	protected void untersuche(ArrayList<Karte> karten) {
		// TODO Auto-generated method stub
		
	}

}
