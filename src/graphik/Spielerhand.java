package graphik;

import java.util.ArrayList;

import javax.swing.JPanel;

public class Spielerhand extends JPanel {
	
	private ArrayList<KartenLabel> karten;
	
	public Spielerhand() {
		super();
		
		this.setLayout(null);
		this.setOpaque(false);
		
		this.setSize(360, 120);
		
		karten = new ArrayList<KartenLabel>();
		for(int i = 0; i < 6; i++) {
			karten.add(new KartenLabel(null, 80, 120));
			this.add(karten.get(i));
			karten.get(i).setLocation(40 + 40*i, 0);
			karten.get(i).setVisible(true);
			//Kartenhintergrund anzeigen
			karten.get(i).setBild(null);
		}
	}
	
	/**
	 * Setzt die angezeigten Karten -> entfernt eine Karte
	 * @param Anzahl der angezeigten Karten
	 */
	public void setzeKarten(int angezeigt) {
		reset();
		
		//Errechnet, um wie viele Pixel die Karten verschoben werden
		int diff = 6 - angezeigt;
		diff *= 40 / 2;
		
		for(int i = 0; i < 6; i++) {
			karten.get(i).setLocation(40 + diff + 40*i, 0);
		}
		
		for(int i = 6; i > angezeigt; i--) {
			//setzt gespielte Karten unsichtbar
			karten.get(i - 1).setVisible(false);
		}
	}
	
	/**
	 * Setzt alles auf Anfang
	 */
	public void reset() {
		for(int i = 0; i < 6; i++) {
			karten.get(i).setVisible(true);
		}
	}
}
