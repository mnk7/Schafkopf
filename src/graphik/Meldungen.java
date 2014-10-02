package graphik;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Meldungen extends JPanel{
	
	//Anzeigen
	private JLabel[] meldung;
	
	public Meldungen(int histLaenge) {
		super(new GridLayout(histLaenge, 1));
		
		meldung = new JLabel[histLaenge];
		for(int i = 0; i < meldung.length; i++) {
			meldung[i] = new JLabel();
			this.add(meldung[i]);
			//Absolute Minimalgröße setzen
			meldung[i].setMinimumSize(new Dimension(100, 10));
		}
	}
	
	/**
	 * Erstellt eine nicht veränderliche Anzeige in der ersten Zeile
	 * @param text
	 */
	public void festeAnzeige(String text) {
		meldung[0].setText(text);
	}
	
	/**
	 * Fügt eine eine neue Nachricht hinzu
	 * @param text
	 */
	public void nachricht(String text) {
		for(int i = meldung.length - 1; i > 1; i--) {
			//Rückt alle Anzeigen eins nach unten
			meldung[i] = meldung[i - 1];
		}
		//Zeigt die neue Meldung an
		meldung[1].setText("  --->" + text);
	}

}
