package regeln;

import lib.Karte;
import lib.Karte.farbe;
import lib.Karte.wert;
import lib.Model;
import lib.Model.modus;

import java.util.ArrayList;

public class Regelwahl {
	
	public Control wahl(modus mod) {
		switch (mod) {
		case GEIERdu: 
		case GEIER: 
			return new Geier(); 
		case WENZdu:  
		case WENZ: 
			return new Wenz(); 
		case SOLOeichelDU: 
		case SOLOeichel: 
			return new Solo(Karte.farbe.EICHEL); 
		case SOLOgrasDU: 
		case SOLOgras: 
			return new Solo(Karte.farbe.GRAS); 
		case SOLOherzDU: 
		case SOLOherz: 
			return new Solo(Karte.farbe.HERZ); 
		case SOLOschellenDU: 
		case SOLOschellen: 
			return new Solo(Karte.farbe.SCHELLEN); 
		case SAUSPIELeichel: 
			return new Sauspiel(Karte.farbe.EICHEL); 
		case SAUSPIELgras: 
			return new Sauspiel(Karte.farbe.GRAS);
		case SAUSPIELherz: 
			return new Sauspiel(Karte.farbe.HERZ);
		case SAUSPIELschellen: 
			return new Sauspiel(Karte.farbe.SCHELLEN);
		case HOCHZEIT: 
			return new Hochzeit();
		}
		return null;
	}
	/**
	 * Prüft Client-seitig, ob ein Sauspiel, eine Hochzeit oder ein Si gespielt werden können.
	 * Eine Hochzeit wird teilweise Server-seitig geprüft
	 */
	public boolean darfGespieltWerden(modus m, Model model, int ID, Karte.farbe f) {
		if(m.equals(modus.SAUSPIELeichel)
				|| m.equals(modus.SAUSPIELgras)
				|| m.equals(modus.SAUSPIELherz)
				|| m.equals(modus.SAUSPIELschellen)) {
			return sauspielMoeglich(f, model, ID);
		} 
		if(m.equals(modus.SI)) {
			return siMoeglich(model, ID);
		} if(m.equals(modus.HOCHZEIT)) {
			return new Hochzeit().hochzeitMoeglich(model.gibSpielerKarten(ID));
		}
		//Wenz, Geier und Solo dürfen immer gespielt werden
		return true;
	}

	public boolean siMoeglich(Model gibModel, int ID) {
		Model m = gibModel;
		
		ArrayList<Karte> spielerhand = m.gibSpielerKarten(ID);
		
		//Karten, die enthalten sein müssen
		ArrayList<Karte> noetig = new ArrayList<Karte>();
		noetig.add(new Karte(farbe.EICHEL, wert.OBER));
		noetig.add(new Karte(farbe.GRAS, wert.OBER));
		noetig.add(new Karte(farbe.HERZ, wert.OBER));
		noetig.add(new Karte(farbe.SCHELLEN, wert.OBER));
		noetig.add(new Karte(farbe.EICHEL, wert.UNTER));
		noetig.add(new Karte(farbe.GRAS, wert.UNTER));
		
		boolean[] vorhanden = {false, false, false, false, false, false};
		
		
		Karte k;
		for(int i = 0; i < spielerhand.size(); i++) {
			k = spielerhand.get(i);
			for(int j = 0; j < noetig.size(); j++) {
				if(k.vergleiche(noetig.get(j))) {
					//Wenn die Karte enthalten ist, wird sie nicht mehr benötigt
					vorhanden[j] = true;
					break;
				}
			}
		}
		
		//Wenn alle nötigen Karten da sind
		for(int i = 0; i < 6; i++) {
			if(!vorhanden[i]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean sauspielMoeglich(Karte.farbe farbe, Model m, int position){
		ArrayList<Karte> y = m.gibSpielerKarten(position);
		Karte rufsau = new Karte(farbe, Karte.wert.SAU);
		boolean hatFarbe = false;
		boolean hatSau = false;
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).vergleiche(rufsau)) {
				//Der Spieler hat die Rufsau selbst
				hatSau = true;
			}
			if(y.get(i).gibFarbe().equals(farbe)) {
				hatFarbe = true;
			}
		}
		if(!hatSau && hatFarbe) {
			return true;
		} else {
			return false;
		}
	}
}
