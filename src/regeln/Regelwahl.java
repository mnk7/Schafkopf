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
				x = new Geier(); 
			break;
		case WENZdu:  
		case WENZ: 
			x = new Wenz(); 
			break;
		case SOLOeichelDU: 
		case SOLOeichel: 
			x = new Solo(Karte.farbe.EICHEL); 
			break; 
		case SOLOgrasDU: 
		case SOLOgras: 
			x = new Solo(Karte.farbe.GRAS); 
			break;
		case SOLOherzDU: 
		case SOLOherz: 
			x = new Solo(Karte.farbe.HERZ); 
			break;
		case SOLOschellenDU: 
		case SOLOschellen: 
			x = new Solo(Karte.farbe.SCHELLEN); 
			break;
		case SAUSPIELeichel: 
			if(sauspielMoeglich(Karte.farbe.EICHEL, m, position)) 
				x = new Sauspiel(Karte.farbe.EICHEL); 
			break;
		case SAUSPIELgras: 
			if(sauspielMoeglich(Karte.farbe.GRAS, m, position)) 
				x = new Sauspiel(Karte.farbe.GRAS);
			break;
		case SAUSPIELherz: 
			if(sauspielMoeglich(Karte.farbe.HERZ, m, position)) 
				x = new Sauspiel(Karte.farbe.HERZ);
			break;
		case SAUSPIELschellen: 
			if(sauspielMoeglich(Karte.farbe.SCHELLEN, m, position)) 
				x = new Sauspiel(Karte.farbe.SCHELLEN);
			break;
		case HOCHZEIT: 
			x = new Hochzeit();
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
