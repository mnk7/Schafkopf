package graphik;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Spielerhand extends JPanel {
	
	private ArrayList<JLabel> karten;
	
	public Spielerhand() {
		
		JLabel lblName = new JLabel("name");
		add(lblName);
		karten = new ArrayList<JLabel>();
		reset();
	}
	
	/**
	 * Entfernt eine Karte von der Hand
	 */
	public void entferneKarte() {
		
	}
	
	/**
	 * Setzt alles auf Anfang
	 */
	public void reset() {
		//Zuerst alle Karten entfernen
		karten.clear();
		for(int i = 0; i < 6; i++) {
			JLabel karte = new JLabel();
			this.add(karte);
			
		}
	}

}
