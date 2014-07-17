package regeln;

import lib.Karte;
import lib.Model;
import java.util.ArrayList;

public class Wenz implements Controll {

    private Model m;
    private ArrayList<Karte> spielbar;
    public Wenz(Model mNeu){
      m = mNeu;
      spielbar = new ArrayList<Karte> ();
    }
    
	@Override
	public int sieger(Model model) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean erlaubt(Model model) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int mitspieler(Model model) {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
