/**
 * Durch diese Interface wird ein Spieler symbolisiert. Es gibt immer 4 Spieler, aber eine beliebige
 * Anzahl an Bots, d.h. KIs und reelen Spielern, die über das Netzwerk mit dem Server verbunden sind.
 */

package server;

import lib.Model;
import lib.Model.modus;

public interface Spieler {
	
	/**
	 * Erhält die ersten 3 Karten und gibt zurück, ob gepklopft wird etc.
	 * @param model
	 * @return
	 */
	public String erste3(Model model);
	
	/**
	 * Führt einen Spielzug aus und gibt das aktualisierte Model zurück
	 * @return
	 */
	public Model spielen(Model model);
	
	/**
	 * Übernimmt ein Model und gibt zurück, ob und was gespielt wird
	 * Bei keinem Spiel wird >> null << zurückgegeben
	 * @param model
	 * @return
	 */
	public Model.modus spielstDu(Model model);
	
	/**
	 * Übergibt den Spielern den Modus und fragt nach Kontra
	 * @param m
	 * @return Kontra
	 */
	public String modus(modus m);

}
