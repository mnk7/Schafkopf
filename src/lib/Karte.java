/**
 * Eine Klasse, die eine Karte im Spiel repräsentiert.
 * Der Wert der Karte kann nicht geändert werden, da es
 * sich um ein Kartendeck handelt.
 * @author michael
 *
 */

package lib;

public class Karte {
	
	//Das Enum legt für jeden Wert einen Integer-Wert fest, d.h. : EICHEL = 0
	public enum farbe {EICHEL, GRAS, HERZ, SCHELLEN};
	public enum wert {NEUN, ZEHN, KONIG, SAU, UNTER, OBER};
	
	private int[] punkte = {0, 10, 4, 11, 2, 3};
	
	private farbe f;
	
	private wert w;
	
	/**
	 * Jede Karte muss eine Farbe und einen Wert haben
	 * @param f
	 * @param w
	 */
	public Karte(farbe f, wert w) {
		this.f = f;
		this.w = w;
	}
	
	/**
	 * Gibt die Farbe der Karte zurück
	 * @return Farbe
	 */
	public farbe gibFarbe() {
		return f;
	}
	
	/**
	 * Gibt den Wert der Karte zurück
	 * @return Wert
	 */
	public wert gibWert() {
		return w;
	}
	
	/**
	 * Gibt den Punktewert der Karte zurück
	 * @return Punkte
	 */
	public int gibPunkte() {
		return punkte[w.ordinal()];
	}
	
	/**
	 * Vergleicht zwei Karten
	 * @param k
	 * @return gleich?
	 */
	public boolean vergleiche(Karte k) {
		if(k.gibFarbe().equals(f)
			&& k.gibWert().equals(w)) {
			return true;
		}			
		return false;
	}

	/**
	 * Gibt einen String aus Farbe + Wert zurück
	 * @return
	 */
	public String gibString() {
		return f.toString() + w.toString();
	}
}
