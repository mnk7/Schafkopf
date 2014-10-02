package graphik;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

import lib.Karte;

public class Tisch extends JPanel {
	
	//Speichert die Karten, die auf dem Tisch liegen
	private KartenLabel[] karten;
	
	public Tisch() {
		super();
		
		this.setLayout(null);
		
		karten = new KartenLabel[4];
		//Setzt die Größe der Karten
		for(int i = 0; i < 4; i++) {
			karten[i] = new KartenLabel(null, this.getWidth() / 4, this.getHeight() / 3);
			this.add(karten[i]);
			karten[i].setVisible(true);
		}
		
		kartenPosition();
	}
	
	/**
	 * Setzt die Position der Karten
	 */
	private void kartenPosition() {
		//evtl. gleiche Koordinaten in Variable speichern
		//Links
		karten[0].setLocation(0, this.getHeight() / 4);
		//Oben
		karten[1].setLocation(this.getWidth() *3/8, 0);
		//Rechts
		karten[2].setLocation(this.getWidth() *3/4, this.getHeight() / 4);
		//Unten
		karten[3].setLocation(this.getWidth() *3/8, karten[1].getY() + this.getHeight() / 3 + 80);
	}
	
	/**
	 * Aktualisiert die angezeigten Karten
	 * @param karten
	 */
	public void setzeKarten(Karte[] karten) {
		for(int i = 0; i < 4; i++) {
			this.karten[i].setBild(karten[i]);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Setzt bei Veränderung am Tisch die Größe der Karten neu
		for(int i = 0; i < 4; i++) {
			karten[i].setSize(this.getWidth() / 4, this.getHeight() / 3);
		}
		
		kartenPosition();
	}
}
