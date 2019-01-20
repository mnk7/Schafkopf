package ki;

import java.util.ArrayList;
import java.util.Random;

import lib.Karte;
import lib.Model;

public class Hochzeit extends KI {
	
	private Random zufall;

	public Hochzeit(int ID, int handicap) {
		super(ID);
		regeln = new regeln.Hochzeit();
		//Minimum: 1 Ober (3), 2 Wenz (2), 1 Farbtrumpf (1) = 8
		risiko = 8;
		this.handicap = handicap;
		
		zufall = new Random();
		
		//Variiert die Risikobereitschaft des Bots je nach handicap
		risiko += (zufall.nextInt(handicap) - (int) handicap / 2);
	}
	
	public Model spiel(Model model) {
		//Zufällig eine karte auswählen
		return super.spiel(model);
	}
	
	public void untersuche(ArrayList<Karte> karten) {
		trumpfzahl = 0;
		trumpfsumme = 0;
		for(int i = 0; i < karten.size(); i++) {
			Karte k = karten.get(i); 
			if(regeln.istTrumpf(k.gibWert(), k.gibFarbe())) {
				trumpfzahl++;
				if(k.gibWert().equals(Karte.wert.OBER)) {
					trumpfsumme += 3;
				} else if(k.gibWert().equals(Karte.wert.UNTER)) {
					trumpfsumme += 2;
				} else if(k.gibFarbe().equals(Karte.farbe.HERZ)) {
					trumpfsumme += 1;
				}
			}
		}
	}

	/**
	 * Untersucht, ob eine Hochzeit angenommen wird und wenn ja, welcher Nicht-Trumpf abgegeben wird
	 * Wird die Hochzeit nicht angenommen, so wird null zurückgegeben
	 * @param model
	 * @return
	 */
	public Karte hochzeitAnnehmen(Model model, int ID) {
		//Speichert die Zahl der Trümpfe
		trumpfzahl = 0;
		//Speichert die Qualität der Trümpfe
		trumpfsumme = 0;
		//Nicht-Trumpf
		int nichtTrumpf = -1;
		//Die Karten des Spielers
		ArrayList<Karte> spielerkarten = model.gibSpielerKarten(ID);
		untersuche(spielerkarten);
		
		for(int i = 0; i < spielerkarten.size(); i++) {
			Karte k = spielerkarten.get(i);
			if(!regeln.istTrumpf(k.gibWert(), k.gibFarbe())) {
				//Nicht-Trumpf
				if(nichtTrumpf == -1) {
					nichtTrumpf = i;
				} else {
					//Möglichst eine Karte mit vielen Punkten hergeben
					if(spielerkarten.get(nichtTrumpf).gibPunkte() < spielerkarten.get(i).gibPunkte()) {
						nichtTrumpf = i;
					}
				}
			}
		}
		
		if(trumpfzahl < 6 && trumpfsumme > risiko) {
			return spielerkarten.get(nichtTrumpf);
		} else {
			return null;
		}
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
