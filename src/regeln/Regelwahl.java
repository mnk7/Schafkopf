package regeln;

import lib.Karte;
import lib.Model;
import lib.Model.modus;
import java.util.ArrayList;

public class Regelwahl {
	
	public Regelwahl() {
		
	}
	
	public Controll wahl(modus mod, Model m, int position) {
		Controll x = null;
		switch (mod) {
		case GEIERdu: case GEIER: x = new Geier(); break;
		case WENZdu:  case WENZ: x = new Wenz(); break;
		case SOLOeichelDU: case SOLOeichel: x = new Solo(Karte.farbe.EICHEL); break; 
		case SOLOgrasDU: case SOLOgras: x = new Solo(Karte.farbe.GRAS); break;
		case SOLOherzDU: case SOLOherz: x = new Solo(Karte.farbe.HERZ); break;
		case SOLOschellenDU: case SOLOschellen: x = new Solo(Karte.farbe.SCHELLEN); break;
		case SAUSPIELeichel: if(sauspielMoeglich(Karte.farbe.EICHEL, m, position)) x = new Sauspiel(Karte.farbe.EICHEL); break;
		case SAUSPIELgras: if(sauspielMoeglich(Karte.farbe.GRAS, m, position)) x = new Sauspiel(Karte.farbe.GRAS);break;
		case SAUSPIELherz: if(sauspielMoeglich(Karte.farbe.HERZ, m, position)) x = new Sauspiel(Karte.farbe.HERZ);break;
		case SAUSPIELschellen: if(sauspielMoeglich(Karte.farbe.SCHELLEN, m, position)) x = new Sauspiel(Karte.farbe.SCHELLEN);break;
		case HOCHZEIT: if(hochzeitMoeglich(m, position)) x = new Hochzeit();
		}
		return x;
	}
<<<<<<< HEAD
	public boolean sauspielMoeglich(Karte.farbe farb, Model m, int position){
		ArrayList<Karte> y;
		y = m.gibSpielerKarten(position);
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).gibFarbe().equals(farb) && y.get(i).gibWert().equals(Karte.wert.SAU) && !istTrumpf(y.get(i).gibWert(),y.get(i).gibFarbe())){
				return true;
			}
		}
		return false;
	}
	
	public boolean istTrumpf(Karte.wert angespielt, Karte.farbe angespielt2) {
		if (angespielt.equals(Karte.wert.OBER) || angespielt.equals(Karte.wert.UNTER) || angespielt2.equals( Karte.farbe.HERZ))return true;
		return false;
	}
	
	public boolean hochzeitMoeglich(Model m, int position){
		return true;
	}
	
=======
	
	public boolean sauspielMoeglich(Karte.farbe f, Model m, int position) {
		return true;
	}
	
	public boolean hochzeitMoeglich(Model m, int position) {
		return true;
	}

>>>>>>> e36f21a39a237ea534d3d0f9866387aedf68f07f
}
