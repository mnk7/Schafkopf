/**
 * Durch diese Interface wird ein Spieler symbolisiert. Es gibt immer 4 Spieler, aber eine beliebige
 * Anzahl an Bots, d.h. KIs und reelen Spielern, die über das Netzwerk mit dem Server verbunden sind.
 */

package server;

import lib.Model;
import lib.Model.modus;

public interface Spieler{
	
	/**
	 * gibt die Antwort
	 */
	public String gibAntwort();
	
	/**
	 * gibt das empfangene Model
	 */
	public Model gibModel();
	
	/**
	 * Erhält die ersten 3 Karten und gibt zurück, ob gepklopft wird etc.
	 * @param model
	 * @return
	 */
	public void erste3(Model model);
	
	/**
	 * Führt einen Spielzug aus und gibt das aktualisierte Model zurück
	 * @return
	 */
	public void spielen(Model model);
	
	/**
	 * Übernimmt ein Model und gibt zurück, ob und was gespielt wird
	 * Bei keinem Spiel wird >> null << zurückgegeben
	 * @param model
	 * @return
	 */
	public void spielstDu(Model model);
	
	/**
	 * Übergibt den Spielern den Modus und fragt nach Kontra
	 * @param m
	 * @return Kontra
	 * @throws Exception 
	 */
	public String modus(modus m) throws Exception;
	
	/**
	 * Sendet die Sieger einer Runde
	 * @throws Exception 
	 */
	public void sieger(int s1, int s2) throws Exception;
	
	/**
	 * Gibt die IP des Clients zurück
	 * @return IP
	 */
	public String gibIP();
	
	/**
	 * Gibt den Namen des Spielers zurück
	 * @return
	 */
	public String gibName();
	
	/**
	 * Setzt vor jedem Spiel die ID der Spieler
	 * @param ID
	 * @throws Exception 
	 */
	public void setzeID(int ID) throws Exception;

}
