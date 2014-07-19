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
		// TODO Auto-generated method stub
		return 0;
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
