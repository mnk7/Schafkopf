package schafkopf;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class SpielUI extends StackPane implements View {
	
	private int ID;
	
	private Spielerhand spielerhand;

	public SpielUI(int kartenzahl, Node... children) {
		super(children);
		
		// Setze vorerst keine ID
		ID = -1;
		
		spielerhand = new Spielerhand(kartenzahl);
		spielerhand.setMinSize(400, 400);
		this.getChildren().add(spielerhand);
	}

	@Override
	public void setModel(Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void weristdran(int ID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setID(int ID) {
		this.ID = ID;
	}

	@Override
	public void setzeNamen(String[] namen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spiel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setzeModus(modus mod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bestesspiel(modus mod) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public modus spielstDu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String klopfstDu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String kontra() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sieger(int s1, int s2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String hochzeit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void hochzeitKarte() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spielt(int spielt, int mitspieler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kontra(boolean[] kontra) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void geklopft(boolean[] geklopft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void konto(int kontostand, int stock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beenden() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Aktuali
	 * @param karten
	 */
	private void setzeTisch(Karte[] karten) {
		//Passt die Karten auf dem Tisch an die Spieler an
		Karte[] gespielt = new Karte[4];
		gespielt[3] = karten[ID]; 
		for(int i = 0; i < 3; i++) {
			gespielt[i] = karten[(ID + 1 + i) % 4];
		}
				
		spielerhand.setzeTisch(gespielt);
	}

}
