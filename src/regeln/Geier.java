package regeln;
import java.util.ArrayList;

import lib.Karte;
import lib.Model;

public class Geier implements Controll {
	
	private Model m;
	
	public Geier(Model mNeu){
		m = mNeu;
	}
	@Override
	public int sieger(Model model) {
		boolean ober = false;
		Karte[] gespielt = new Karte[4];
		gespielt = m.gibTisch();
		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibWert() == Karte.wert.OBER){
				ober = true;
			}
		}
		if(ober) return keinOber(gespielt);
		return schonOber(gespielt);
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
	public int keinOber(Karte[] gespielt){
		int spieler = 0;
		Karte.farbe farb = gespielt[0].gibFarbe();
		Karte.wert farbwert = gespielt[0].gibWert();

		for(int i = 0; i < 4; i++){
			if(gespielt[i].gibFarbe() == farb){
				spieler = i;
				switch(farbwert){
				case NEUN: farbwert = gespielt[i].gibWert();break;
				case UNTER: {
				if(gespielt[i].gibWert() == Karte.wert.NEUN){}
				else farbwert = gespielt[i].gibWert();
				}break;
				case KONIG:{
					if(gespielt[i].gibWert() == Karte.wert.NEUN || gespielt[i].gibWert() == Karte.wert.UNTER){}
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
	
	@Override
	public boolean erlaubt(Model model) {
		// TODO Auto-generated method stub
		Karte.wert angespielt;
		angespielt = m.gibTisch()[0].gibWert();
		if(angespielt == null){
			return true;
		}
		if(angespielt.equals(Karte.wert.OBER)){
		if(Karte.wert.OBER == getWert()){
			return true;
		}
		if(Karte.wert.OBER != getWert() && keinTrumpf()){
			return true;
		}
		}
		Karte.farbe angespielt2;
		angespielt2 = m.gibTisch()[0].gibFarbe();
		if(getFarbe().equals(angespielt2)){
			return true;
		}
		if(!getFarbe().equals(angespielt2) && keineFarbe(angespielt2)){
			return true;
		}
		return false;
	}
	@Override
	public int mitspieler(Model model) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Karte.wert getWert(){
		Karte.wert x = null;
		for(int i = 0; i < 4; i++){
			if(m.gibTisch()[i] != null){
				x = m.gibTisch()[i].gibWert();
			}
		}
		return x;
	}
	public boolean keinTrumpf(){
		ArrayList<Karte> y;
		int x = 0;
		for(int i = 0; i < 4; i++){
			if(m.gibTisch()[i] != null){
				x = i;
			}
		}
		y = m.gibSpielerKarten(x);
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).gibWert() == Karte.wert.OBER){
				return false;
			}
	}
		return true;
}
	public Karte.farbe getFarbe(){
		Karte.farbe x = null;
		for(int i = 0; i < 4; i++){
			if(m.gibTisch()[i] != null && m.gibTisch()[i].gibWert() != Karte.wert.OBER){
				x = m.gibTisch()[i].gibFarbe();
			}
		}		
		return x;
	}
	public boolean keineFarbe(Karte.farbe z){
		ArrayList<Karte> y;
		int x = 0;
		for(int i = 0; i < 4; i++){
			if(m.gibTisch()[i] != null){
				x = i;
			}
		}
		y = m.gibSpielerKarten(x);
		for(int i = 0; i < y.size(); i++){
			if(y.get(i).gibFarbe() == z && m.gibTisch()[i].gibWert() != Karte.wert.OBER){
				return false;
			}
	}
		return true;
}
}
