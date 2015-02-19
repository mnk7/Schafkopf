package server;

import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Bot implements Spieler {

	@Override
	public boolean erste3(Model model) {
		return false;
	} 

	@Override
	public void spielen(Model model) {
	}

	@Override
	public modus spielstDu(Model model) {
		return modus.NICHTS;
	}

	@Override
	public boolean modus(lib.Model.modus m) {
		return false;
	} 

	@Override
	public void sieger(int s1, int s2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String gibIP() {
		// TODO Auto-generated method stub
		return "local";
	}

	@Override
	public String gibName() {
		return null;
	}

	@Override
	public void setzeID(int ID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Model gibModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Karte gibKarte() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hochzeit() {
		return false;
	}

	@Override
	public void spieler(int spielt, int mitspieler) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void geklopft(boolean[] geklopft) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kontra(boolean[] kontra) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setzeModel(Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void name() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beenden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void abmelden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String gibAntwort(String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rundeZuende(int kontostand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void konto(int kontostand) {
		// TODO Auto-generated method stub
		
	}

}
