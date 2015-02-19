package regeln;

import lib.Karte;
import lib.Karte.farbe;
import lib.Karte.wert;
import lib.Model;
import lib.Model.modus;

import java.util.ArrayList;

public class Regelwahl {
	
	public Regelwahl() {
		
	}
	
	public Control wahl(modus mod, Model m, int position) {
		Control x = null;
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
		return x;
	}
	
	public boolean istTrumpf(Karte.wert angespielt, Karte.farbe angespielt2) {
		if (angespielt.equals(Karte.wert.OBER) 
				|| angespielt.equals(Karte.wert.UNTER) 
				|| angespielt2.equals( Karte.farbe.HERZ))
			return true;
		
		return false;
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
		
		//:-(
		for(int i = 0; i < spielerhand.size(); i++) {
			Karte k = spielerhand.get(i);
			for(int j = 0; j < noetig.size(); j++) {
				if(k.vergleiche(noetig.get(j))) {
					//Wenn die Karte enthalten ist, wird sie nicht mehr benötigt
					noetig.remove(j);
				}
			}
		}
		
		//Wenn alle nötigen Karten da sind
		if(noetig.size() == 0)
			return true;
		
		return false;
	}
	
	public boolean sauspielMoeglich(Karte.farbe farb, Model m, int position){
		ArrayList<Karte> y;
		y = m.gibSpielerKarten(position);
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).gibFarbe().equals(farb) 
					&& y.get(i).gibWert().equals(Karte.wert.SAU) 
					&& !istTrumpf(y.get(i).gibWert(),y.get(i).gibFarbe())){
				return false;
			}
		}
		return true;
	}
}
