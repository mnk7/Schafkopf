/**
 * Durch diese Interface wird ein Spieler symbolisiert. Es gibt immer 4 Spieler, aber eine beliebige
 * Anzahl an Bots, d.h. KIs und reelen Spielern, die über das Netzwerk mit dem Server verbunden sind.
 */

package server;

import lib.Karte;
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
	 * @throws Exception  
	 */
	public void erste3(Model model) throws Exception;
	
	/**
	 * Führt einen Spielzug aus und gibt das aktualisierte Model zurück
	 * @return
	 * @throws Exception 
	 */
	public void spielen(Model model) throws Exception;
	
	/**
	 * Übernimmt ein Model und gibt zurück, ob und was gespielt wird
	 * Bei keinem Spiel wird >> null << zurückgegeben
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	public void spielstDu(Model model) throws Exception;
	
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
	 * @throws Exception 
	 */
	public String gibName() throws Exception;
	
	/**
	 * Setzt vor jedem Spiel die ID der Spieler
	 * @param ID
	 * @throws Exception 
	 */
	public void setzeID(int ID) throws Exception;

	/**
	 * Gibt eine empfangene Karte
	 * @return
	 */
	public Karte gibKarte();

	/**
	 * Frägt, ob eine Hochzeit angenommen wird
	 * @throws Exception 
	 */
	public void hochzeit() throws Exception;

	public void spieler(int spielt) throws Exception;  

}
