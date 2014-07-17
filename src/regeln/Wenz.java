package regeln;
import.java.util.ArrayList;

public class Wenz implements Controll {

    private Model m;
    private ArrayList<Karte> spielbar;
    public Wenz(Model mNeu){
      m = mNeu;
      spielbar = new ArrayList<Karte> ();
    }
    
}
