package graphik;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Karte;

public class Gegenspieler extends JPanel {
	
	//Karten des Spielers
	private Spielerhand karten;
	private int kartenzahl;
	//Nachrichten und Meldungen
	private Meldungen meldungen;
	
	private FlowLayout layout;
	
	/**
	 * Erstellt einen Gegenspieler
	 */
	public Gegenspieler() {
		super(new GridLayout(2, 1));
		 
		kartenzahl = 6;
		
		karten = new Spielerhand();
		karten.setSize(this.getWidth(), this.getHeight() / 2);
		//Übergibt 6 leere Karten, damit die Rückseite angezeigt wird
		karten.setzeKarten(new ArrayList<Karte>(kartenzahl));
		
		//Erstellt eine neue Nachrichtenanzeige mit 4 Ausgaben
		meldungen = new Meldungen(4);
		meldungen.setSize(this.getWidth(), this.getHeight() / 2);
		
		this.add(meldungen);
		this.add(karten);
	}
	
	/**
	 * Entfernt eine Karte aus der Hand des Gegenspielers
	 */
	public void entferneKarte() {
		kartenzahl--;
		karten.setzeKarten(new ArrayList<Karte>(kartenzahl));
	}
	
	/**
	 * Zeigt eine Nachricht für den Gegenspieler an
	 * @param text
	 */
	public void nachricht(String text) {
		meldungen.nachricht(text);
	}
	
	/**
	 * Setzt den Namen des Spielers
	 * @param text
	 */
	public void name(String text) {
		meldungen.festeAnzeige(text);
	}
	
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		
		karten.setSize(this.getWidth(), this.getHeight() / 2);
		
		meldungen.setSize(this.getWidth(), this.getHeight() / 2);
	}

}
