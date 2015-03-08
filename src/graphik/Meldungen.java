package graphik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Meldungen extends JPanel{
	
	//Anzeigen
	private JLabel[] meldung;
	
	private int festeAnzeigen = 0;
	
	public Meldungen(int histLaenge) {
		super();
		
		this.setLayout(null);
		this.setOpaque(true);
		this.setBackground(new Color(0,0,0,0));
		this.setSize(200, histLaenge*20);
		
		meldung = new JLabel[histLaenge];
		for(int i = 0; i < meldung.length; i++) {
			meldung[i] = new JLabel();
			this.add(meldung[i]);
			meldung[i].setBounds(0, i*20, 480, 20);
			meldung[i].setBackground(new Color(0,0,0,0));
			meldung[i].setForeground(Color.white);
		}
	}
	
	/**
	 * Erstellt eine nicht veränderliche Anzeige in der ersten Zeile
	 * @param text
	 */
	public void festeAnzeige(String text) {
		//Verdoppelt die letzte Meldung
		nachricht(meldung[festeAnzeigen].getText());
		//Und startet die feste Anzeige
		meldung[festeAnzeigen].setText(text);
		Font schrift = meldung[festeAnzeigen].getFont();
		meldung[festeAnzeigen].setFont(schrift.deriveFont(schrift.BOLD+ schrift.ITALIC));
	}
	
	/**
	 * Fügt eine eine neue Nachricht hinzu
	 * @param text
	 */
	public void nachricht(String text) {
		for(int i = meldung.length - 1; i > festeAnzeigen; i--) {
			//Rückt alle Anzeigen eins nach unten
			meldung[i].setText("");
			meldung[i].setText(meldung[i - 1].getText());
		}
		//Zeigt die neue Meldung an
		meldung[1].setText(text);
	}
	
	/**
	 * Setzt alle Meldungen zurück
	 */
	public void reset() {
		festeAnzeigen = 0;
		for(int i = 0; i < meldung.length; i++) {
			meldung[i].setText("");
		}
	}

}
