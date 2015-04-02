package ki;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Wenz extends KI {

	public Wenz(int ID, int handicap) {
		super(ID);
		regeln = new regeln.Wenz();
		this.handicap = handicap;
	}

	public Model spiel(Model model) {
		//Zufällig eine Karte auswählen
		return super.spiel(model);
	}

	protected void untersuche(ArrayList<Karte> karten) {
		// TODO Auto-generated method stub
		
	}

}
