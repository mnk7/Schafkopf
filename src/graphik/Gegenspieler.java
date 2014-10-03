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
	
	/**
	 * Erstellt einen Gegenspieler
	 */
	public Gegenspieler() {
		super();
		
		this.setLayout(null);
		 
		kartenzahl = 6;
		
		karten = new Spielerhand();
		karten.setzeKarten(kartenzahl);
		this.add(karten);
		
		//Erstellt eine neue Nachrichtenanzeige mit 4 Ausgaben
		meldungen = new Meldungen(4);
		this.add(meldungen);
		
		//legt eine feste Größe für das Label fest
		this.setSize(karten.getWidth(),
					 karten.getHeight() + meldungen.getHeight() + 10);
		
		meldungen.setLocation(100, 0);
		karten.setLocation(0, meldungen.getHeight() + 10);
	}
	
	/**
	 * Entfernt eine Karte aus der Hand des Gegenspielers
	 */
	public void entferneKarte(int kartenzahl) {
		this.kartenzahl = kartenzahl;
		karten.setzeKarten(this.kartenzahl);
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
		//Erst zurücksetzen
		meldungen.reset();
		meldungen.festeAnzeige(text);
	}
}
