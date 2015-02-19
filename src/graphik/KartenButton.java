package graphik;

import javax.swing.JButton;

import lib.Karte;

public class KartenButton extends JButton {
	
	private KartenLabel label;
	
	private Karte karte;
	
	public KartenButton(Karte karte) {
		super();
		
		this.setLayout(null);
		
		this.karte = karte;
		
		label = new KartenLabel(this.karte, this.getWidth(), this.getHeight());
		this.add(label);
		this.setSize(label.getWidth(), label.getHeight());
		label.setVisible(true);
	}
	
	/**
	 * Setzt die angezeigte Karte
	 * @param karte
	 */
	public void setzeKarte(Karte karte) {
		this.karte = karte;
		label.setBild(this.karte);
		this.setVisible(true);
	}
	
	/**
	 * Gibt die gehaltene Karte zurück
	 * @return
	 */
	public Karte gibKarte() {
		return karte;
	}
}
