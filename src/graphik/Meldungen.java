package graphik;

import java.awt.Dimension;
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
		this.setSize(200, histLaenge*20);
		
		meldung = new JLabel[histLaenge];
		for(int i = 0; i < meldung.length; i++) {
			meldung[i] = new JLabel();
			this.add(meldung[i]);
			meldung[i].setBounds(0, i*20, 200, 20);
		}
	}
	
	/**
	 * Erstellt eine nicht veränderliche Anzeige in der ersten Zeile
	 * @param text
	 */
	public void festeAnzeige(String text) {
		meldung[festeAnzeigen].setText(text);
	}
	
	/**
	 * Fügt eine eine neue Nachricht hinzu
	 * @param text
	 */
	public void nachricht(String text) {
		for(int i = meldung.length - 1; i > festeAnzeigen; i--) {
			//Rückt alle Anzeigen eins nach unten
			meldung[i].setText(meldung[i - 1].getText());
		}
		//Zeigt die neue Meldung an
		meldung[1].setText("  --->" + text);
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
