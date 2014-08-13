package graphik;

import java.awt.FlowLayout;
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
	
	public Gegenspieler(String name) {
		super();
		
		//Anordnung der beiden Komponenten
		layout = new FlowLayout();
		this.setLayout(layout);
		
		kartenzahl = 6;
		
		karten = new Spielerhand();
		//Übergibt 6 leere Karten, damit die Rückseite angezeigt wird
		karten.setzeKarten(new ArrayList<Karte>(kartenzahl));
		
		//Erstellt eine neue Nachrichtenanzeige mit 4 Ausgaben
		meldungen = new Meldungen(4);
		//Zeigt den Namen des Spielers an
		meldungen.festeAnzeige(name);
		
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
	
	public void nachricht(String text) {
		meldungen.nachricht(text);
	}

}
