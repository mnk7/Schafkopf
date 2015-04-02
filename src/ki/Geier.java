package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Geier extends KI {

	public Geier(int ID, int handicap) {
		super(ID);
		regeln = new regeln.Geier();
		this.handicap = handicap;
	}

	public Model spiel(Model model) {
		//Zufällig ausgewählte Karte
		return super.spiel(model);
	}

	protected void untersuche(ArrayList<Karte> karten) {
		// TODO Auto-generated method stub
		
	}

}
