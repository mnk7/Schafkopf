package regeln;

import lib.Karte;
import lib.Model;
import lib.Model.modus;

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
		case SAUSPIELeichel: if(sauspielMoeglich(Karte.farbe.EICHEL, m, position)) x = new Sauspiel(Karte.farbe.EICHEL);
		else {} break;
		case SAUSPIELgras: if(sauspielMoeglich(Karte.farbe.GRAS, m, position)) x = new Sauspiel(Karte.farbe.GRAS);
		else {} break;
		case SAUSPIELherz: if(sauspielMoeglich(Karte.farbe.HERZ, m, position)) x = new Sauspiel(Karte.farbe.HERZ);
		else {} break;
		case SAUSPIELschellen: if(sauspielMoeglich(Karte.farbe.SCHELLEN, m, position)) x = new Sauspiel(Karte.farbe.SCHELLEN);
		else {} break;
		case HOCHZEIT: if(hochzeitMoeglich(m, position)) x = new Hochzeit();
		}
		return x;
	}
	public boolean sauspielMoeglich(Karte.)

}
