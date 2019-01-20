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
	 * gibt das empfangene Model
	 */
	public Model gibModel();
	
	/**
	 * Erhält die ersten 3 Karten und gibt zurück, ob gepklopft wird etc.
	 * @param model
	 * @return
	 * @throws Exception  
	 */
	public boolean erste3(Model model) throws Exception;
	
	/**
	 * Führt einen Spielzug aus und gibt das aktualisierte Model zurück
	 * @return
	 * @throws Exception 
	 */
	public void spielen(Model model) throws Exception;
	
	/**
	 * Übernimmt ein Model und das bislang höchste Spiel und gibt zurück, ob und was gespielt wird
	 * Bei keinem Spiel wird >> null << zurückgegeben
	 * @param model
	 * @param modus
	 * @return
	 * @throws Exception 
	 */
	public lib.Model.modus spielstDu(Model model, modus m) throws Exception;
	
	/**
	 * Übergibt den Spielern den Modus und fragt nach Kontra
	 * @param m
	 * @return 
	 * @throws Exception 
	 */
	public boolean modus(modus m) throws Exception;
	
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
	 * @throws Exception 
	 */
	public String gibName() throws Exception;
	
	/**
	 * Fragt nach dem Namen
	 */
	public void name() throws Exception;
	
	/**
	 * Setzt vor jedem Spiel die ID der Spieler
	 * @param ID
	 * @throws Exception 
	 */
	public void setzeID(int ID) throws Exception;

	/**
	 * Gibt eine empfangene Karte
	 * @return
	 * @throws InterruptedException  
	 */
	public Karte gibKarte() throws InterruptedException; 

	/**
	 * Frägt, ob eine Hochzeit angenommen wird
	 * @throws Exception 
	 */
	public boolean hochzeit() throws Exception;

	/**
	 * Gibt die Spielenden an alle weiter
	 * @param spielt
	 * @param mitspieler
	 * @throws Exception
	 */
	public void spieler(int spielt, int mitspieler) throws Exception;

	/**
	 * Sendet, welche Spieler geklopft haben
	 * @param geklopft
	 * @throws Exception 
	 */
	public void geklopft(boolean[] geklopft) throws Exception;

	/**
	 * Sendet, welche Spieler Kontra geben
	 * @param kontra
	 * @throws Exception
	 */
	public void kontra(boolean[] kontra) throws Exception;

	/**
	 * Aktualisiert das Model
	 * @param model
	 */
	public void setzeModel(Model model);

	/**
	 * Beendet den Spieler-Thread Server-seitig
	 */
	public void beenden();

	/**
	 * Sendet ein Beenden-Signal an den Client
	 * und beendet Server-seitig
	 */
	public void abmelden();
 
	/**
	 * schließt die Runde für die Spieler ab und sendet den Kontostand
	 */
	public void rundeZuende(int kontostand, int stock);

	/**
	 * Gibt den Kontostand aus
	 * @param stock 
	 * @param integer
	 */
	public void konto(int kontostand, int stock);

	/**
	 * Aktualisiert das Model des Spielers
	 * @param model
	 */
	public void update(Model model);

	/**
	 * Zeigt an, wer dran ist
	 * @param iD
	 */
	public void weristdran(int iD);

	/**
	 * Verteilt das Model nach jedem Stich
	 * @param model
	 */
	public void stich(Model model);

}
