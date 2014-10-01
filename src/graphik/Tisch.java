package graphik;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

import lib.Karte;

public class Tisch extends JPanel {
	
	//Speichert die Karten, die auf dem Tisch liegen
	private KartenLabel[] karten;
	
	public Tisch() {
		super(new BorderLayout());
		
		karten = new KartenLabel[4];
		//Setzt die Größe der Karten
		for(int i = 0; i < 4; i++) {
			karten[i] = new KartenLabel(null, this.getWidth() / 4, this.getHeight() / 3);
		}
		//Weist den Karten ihren Platz zu
		this.add(karten[0], BorderLayout.PAGE_END);
		this.add(karten[1], BorderLayout.LINE_START);
		this.add(karten[2], BorderLayout.PAGE_START);
		this.add(karten[3], BorderLayout.LINE_END);
		
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
	}
}
