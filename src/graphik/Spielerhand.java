package graphik;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Spielerhand extends JPanel {
	
	private ArrayList<KartenLabel> karten;
	
	public Spielerhand() {
		
		JLabel lblName = new JLabel("name");
		add(lblName);
		karten = new ArrayList<KartenLabel>();
		reset();
	}
	
	/**
	 * Entfernt eine Karte von der Hand
	 */
	public void entferneKarte() {
		//animieren
		
		karten.remove(0);
	}
	
	/**
	 * Setzt alles auf Anfang
	 */
	public void reset() {
		//Zuerst alle Karten entfernen
		karten.clear();
		for(int i = 0; i < 6; i++) {
			KartenLabel karte = new KartenLabel(100, 50);
			//Setzt als Bild die Kartenrückseite
			karte.setBild(null);
			
			this.add(karte);
		}
	}

}
