package graphik;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JLabel;

import lib.Karte;

public class Tisch extends JLabel {
	
	//Speichert die Karten, die auf dem Tisch liegen
	private KartenLabel[] karten;
	
	private BorderLayout layout;
	
	public Tisch() {
		super();
		
		layout = new BorderLayout();
		this.setLayout(layout);
		
		karten = new KartenLabel[4];
		//Setzt die Größe der Karten
		for(int i = 0; i < 4; i++) {
			karten[i] = new KartenLabel(null, this.getWidth() / 4, this.getHeight() / 3);
			this.add(karten[i]);
		}
		//Weist den Karten ihren Platz zu
		layout.addLayoutComponent(karten[0], layout.SOUTH);
		layout.addLayoutComponent(karten[1], layout.WEST);
		layout.addLayoutComponent(karten[2], layout.NORTH);
		layout.addLayoutComponent(karten[3], layout.EAST);
		
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
