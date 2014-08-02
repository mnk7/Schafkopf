package graphik;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Karte;

public class Spielerhand extends JPanel {
	
	private ArrayList<KartenLabel> karten;
	
	private FlowLayout layout;
	
	public Spielerhand() {
		super();
		
		layout = new FlowLayout();
		this.setLayout(layout);
		karten = new ArrayList<KartenLabel>();
		reset();
	}
	
	/**
	 * Entfernt eine Karte von der Hand
	 */
	public void entferneKarte() {
		//animieren
		
		karten.remove(0);
		repaint();
	}
	
	/**
	 * Setzt die angezeigten Karten
	 * @param model (Karten des Spielers)
	 */
	public void setzeKarten(ArrayList<Karte> model) {
		ArrayList<Karte> m = model;
		for(int i = 0; i < karten.size(); i++) {
			karten.get(i).setBild(m.get(i));
		}
	}
	
	/**
	 * Setzt alles auf Anfang
	 */
	public void reset() {
		//Zuerst alle Karten entfernen
		karten.clear();
		for(int i = 0; i < 6; i++) {
			KartenLabel karte = new KartenLabel(this.getWidth() / 6, this.getHeight());
			//Setzt als Bild die Kartenrückseite
			karte.setBild(null);
			
			this.add(karte);
		}
	}

}
