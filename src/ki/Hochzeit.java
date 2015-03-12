package ki;

import java.util.ArrayList;

import regeln.Control;

import lib.Karte;
import lib.Model;

public class Hochzeit extends KI {

	public Hochzeit(int ID) {
		super(ID);
		regeln = new regeln.Hochzeit();
	}

	public boolean kontra(Model model) {
		return false;
	}
	
	public Model spiel(Model model) {
		//Zufällig eine karte auswählen
		return super.spiel(model);
	}

	/**
	 * Untersucht, ob eine Hochzeit angenommen wird und wenn ja, welcher Nicht-Trumpf abgegeben wird
	 * Wird die Hochzeit nicht angenommen, so wird null zurückgegeben
	 * @param model
	 * @return
	 */
	public static Karte hochzeitAnnehmen(Model model, int ID) {
		//Es werden derzeit keine Hochzeiten angenommen
		return null; 
	}

	/**
	 * Gibt den einzigen Trumpf zurück
	 * @param model
	 * @return
	 */
	public static Karte hochzeitVorschlagen(Model model, int ID) {
		ArrayList<Karte> spielerkarten = model.gibSpielerKarten(ID);
		
		regeln.Hochzeit tester = new regeln.Hochzeit();
		
		for(int i = 0; i < spielerkarten.size(); i++) {
			Karte k = spielerkarten.get(i);
			if(tester.istTrumpf(k.gibWert(), k.gibFarbe())) {
				return k;
			}
		}
		
		return null;
	}

}
