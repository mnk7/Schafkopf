package graphik;

import lib.Karte;
import lib.Model;
import lib.Model.modus;

public interface View {
	
	/**
	 * Aktualisiert das Model
	 * @param model
	 */
	public void setModel(Model model);
	
	/**
	 * Zeigt an, wer am Zug ist
	 */
	public void weristdran(int ID);
	
	/**
	 * Setzt die ID des Spielers, damit die Daten aus dem Model ausgelesen werden können
	 * @param ID
	 */
	public void setID(int ID);
	
	/**
	 * Setzt die Namen der Spieler, ->nachdem<- eine ID vergeben wurde
	 * @param namen
	 */
	public void setzeNamen(String[] namen);
	
	/**
	 * Spielzug des Spielers durchführen
	 * @throws Exception 
	 */
	public void spiel() throws Exception;
	
	/**
	 * Ändert den Modus des Spiels und ändert die Controll
	 * @param mod
	 */
	public void setzeModus(modus mod);
	
	/**
	 * Zeigt das bislang höchste Spiel an
	 * @param mod
	 */
	public void bestesspiel(modus mod);
	
	/**
	 * Fragt den Nutzer, ob er etwas spielen will und gibt den Spielmodus zurück
	 * @param model
	 * @return
	 */
	public modus spielstDu();
	
	/**
	 * Gibt zurück, ob der Spieler klopft
	 * @return
	 */
	public String klopfstDu();
	
	/**
	 * Gibt zurück, ob der Spieler klopft
	 * @return
	 */
	public String kontra();
	
	/**
	 * Zeigt den/die Sieger eines Spiels an
	 * Wenn s2 4 ist, so gibt es keinen zweiten Sieger,
	 * ist s1/s2 zwischen 10 und 13 (11 - 10 = 1 -> der zweite Spieler hat verloren), 
	 * so hat jemand sein Spiel verloren
	 * @param s1
	 * @param s2
	 */
	public void sieger(int s1, int s2);

	/**
	 * Gibt "JA" zurück, wenn der Spieler heiraten will
	 * @return
	 */
	public String hochzeit();

	/**
	 * Gibt die Karte zurück, die der Spieler bei der Hochzeit tauschen will
	 * Wird aufgerufen, wenn jmd. eine Hochzeit anbietet und wenn eine angenommen
	 * wird
	 */
	public void hochzeitKarte();

	/**
	 * Empfängt, wer spielt. Wird keine Hochzeit gespielt ist mitspieler gleich 4
	 * @param spielt
	 * @param mitspieler 
	 */
	public void spielt(int spielt, int mitspieler);

	/**
	 * Zeigt an, wer Kontra gegeben hat
	 * @param kontra
	 */
	public void kontra(boolean[] kontra);

	/**
	 * Zeigt an, wer geklopft hat
	 * @param geklopft
	 */
	public void geklopft(boolean[] geklopft);
	
	/**
	 * Zeigt den Kontostand des Spielers an
	 * @param kontostand
	 * @param stock
	 */
	public void konto(int kontostand, int stock);
	
	/**
	 * Beendet das Spiel
	 */
	public void beenden();
}
