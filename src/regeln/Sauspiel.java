package regeln;

import java.util.ArrayList;

import lib.Karte;
import lib.Model;


public class Sauspiel implements Control {
	Karte.farbe rufsau;
	Karte.farbe farbe = Karte.farbe.HERZ;
	public Sauspiel(Karte.farbe saufarbe){
		rufsau = saufarbe;
	}

	public int sieger(Model m) {
		boolean trumpf = false;
		Karte[] gespielt = new Karte[4];
		gespielt = m.gibTisch();
		for(int i = 0; i < 4; i++){
			if(istTrumpf(gespielt[i].gibWert(),gespielt [i].gibFarbe())){
				trumpf = true;
			}
		}
		if(trumpf) return koaTrumpf(gespielt);
		return schonTrumpf(gespielt);
	}
	
	public int schonTrumpf(Karte[] gespielt){
		boolean ober = false;
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.OBER){
				ober = true;
			}
		}
		if(ober) return schonOber(gespielt);
		
		boolean unter = false;
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.UNTER){
				unter = true;
			}
		}
		if(unter) return schonUnter(gespielt);
		int spieler = 0;
		Karte.wert farbwert = gespielt[0].gibWert();

		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibFarbe() == farbe){
				spieler = i;
				switch(farbwert){
				case NEUN: farbwert = gespielt[i].gibWert();break;
				case KONIG:{
					if(gespielt[i].gibWert() == Karte.wert.NEUN){}
					else farbwert = gespielt[i].gibWert();
					}break;
				case ZEHN:{
					if(gespielt[i].gibWert() == Karte.wert.SAU)farbwert = gespielt[i].gibWert();
					else {}
					}break;		
				case SAU:break;
					}
				}
			}
		return spieler;

		
	}
	
	public int schonUnter(Karte[] gespielt){
		int spieler = 0;
		Karte.farbe unterFarbe = null;
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.UNTER){
				spieler = i;
				switch(unterFarbe){
				case HERZ: {
				if(gespielt[i].gibFarbe() == Karte.farbe.SCHELLEN){}
				else unterFarbe = gespielt[i].gibFarbe();
				}break;
				case GRAS:{
					if(gespielt[i].gibFarbe() == Karte.farbe.EICHEL)unterFarbe = gespielt[i].gibFarbe();
					else {}
					}break;
				case EICHEL:break;
				default: unterFarbe = gespielt[i].gibFarbe();
				}
				}
			}
		return spieler;
	}
	
	public int schonOber(Karte[] gespielt){
		int spieler = 0;
		Karte.farbe oberFarbe = null;
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.OBER){
				spieler = i;
				switch(oberFarbe){
				case HERZ: {
				if(gespielt[i].gibFarbe() == Karte.farbe.SCHELLEN){}
				else oberFarbe = gespielt[i].gibFarbe();
				}break;
				case GRAS:{
					if(gespielt[i].gibFarbe() == Karte.farbe.EICHEL)oberFarbe = gespielt[i].gibFarbe();
					else {}
					}break;
				case EICHEL:break;
				default: oberFarbe = gespielt[i].gibFarbe();
				}
				}
			}
		return spieler;
	}	
	
	public int koaTrumpf(Karte[] gespielt){
		int spieler = 0;
		Karte.farbe farb = gespielt[0].gibFarbe();
		Karte.wert farbwert = gespielt[0].gibWert();

		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibFarbe() == farb){
				spieler = i;
				switch(farbwert){
				case NEUN: farbwert = gespielt[i].gibWert();break;
				case KONIG:{
					if(gespielt[i].gibWert() == Karte.wert.NEUN){}
					else farbwert = gespielt[i].gibWert();
					}break;
				case ZEHN:{
					if(gespielt[i].gibWert() == Karte.wert.SAU)farbwert = gespielt[i].gibWert();
					else {}
					}break;		
				case SAU:break;
					}
				}
			}
		return spieler;
	}
	
	public boolean istTrumpf(Karte.wert angespielt, Karte.farbe angespielt2) {
		if (angespielt.equals(Karte.wert.OBER) || angespielt.equals(Karte.wert.UNTER) || angespielt2.equals(farbe))return true;
		return false;
	}

	

	public boolean erlaubt(Model m) {
		Karte.wert angespielt;
		//Es wurde noch nichts angespielt
		if(m.gibTisch()[0] == null) {
			return true;
		} //Andernfalls...
		angespielt = m.gibTisch()[0].gibWert();
		Karte.farbe angespielt2;
		angespielt2 = m.gibTisch()[0].gibFarbe();
		if (angespielt == null) {			
			return true;
		}
		if (istTrumpf(angespielt, angespielt2)) {
			if (istTrumpf(getWert(m), getFarbe(m))) {
				return true;
			}
			if (keinTrumpf(m) && !sau(m)) {
				return true;
			}
			return false;
		}
		if(getFarbe(m).equals(angespielt2) && sau(m)){
			return true;
		}
		if(!getFarbe(m).equals(angespielt2) && keineFarbe(angespielt2, m) && !sau(m)){
			return true;
		}
		return false;
	}
	
	public boolean keineFarbe(Karte.farbe z, Model m){
		ArrayList<Karte> y;
		int x = 0;
		for(int i = 0; i < 4; i++){
			if(m.gibTisch()[i] != null){
				x = i;
			}
		}
		y = m.gibSpielerKarten(x);
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).gibFarbe() == z && !istTrumpf(y.get(i).gibWert(),y.get(i).gibFarbe())){
				return false;
			}
	}
		return true;
}
	
	public Karte.wert getWert(Model m) {
		Karte.wert x = null;
		for (int i = 0; i < 4; i++) {
			if (m.gibTisch()[i] != null) {
				x = m.gibTisch()[i].gibWert();
			}
		}
		return x;
	}

	public Karte.farbe getFarbe(Model m) {
		Karte.farbe x = null;
		for (int i = 0; i < 4; i++) {
			if (m.gibTisch()[i] != null) {
				x = m.gibTisch()[i].gibFarbe();
			}
		}
		return x;
	}

	public boolean keinTrumpf(Model m) {
		ArrayList<Karte> y;
		int x = 0;
		for (int i = 0; i < 4; i++) {
			if (m.gibTisch()[i] != null) {
				x = i;
			}
		}
		y = m.gibSpielerKarten(x);
		for (int i = 0; i < y.size(); i++) {
			if (istTrumpf(y.get(i).gibWert(), y.get(i).gibFarbe())) {
				return false;
			}
		}
		return true;
	}

	public boolean sau(Model m){
		int spieler = -1;
		for (int i = 0; i < 4; i++) {
			if (m.gibTisch()[i] != null) {
				spieler = i;
			}
		}
		ArrayList<Karte> hand;
		hand = m.gibSpielerKarten(spieler);
		for(int i = 0; i < hand.size(); i++){
			if (hand.get(i).gibWert().equals(Karte.wert.SAU) && hand.get(i).gibFarbe().equals(rufsau)){
				return true;
			} 
		}
		return false;
	}

		

	public int mitspieler(Model m) {
		ArrayList<Karte> hand;
		for(int i = 0; i < 4; i++){
			hand = m.gibSpielerKarten(i);
			for(int j = 0; j < hand.size(); j++){
				if(hand.get(j).gibWert().equals(Karte.wert.SAU) && hand.get(j).gibFarbe().equals(rufsau)){
					return i;
				}
			}
		}
		return -1;
	}

}
