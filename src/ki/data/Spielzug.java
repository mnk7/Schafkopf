package ki.data;

import java.util.ArrayList;

import lib.Karte;
import lib.Model.modus;

public class Spielzug {
	//Speicherort
	private String file;
	private final char trenner = ':';
	
	/**
	 * Speichert Charakteristika eines bestimmten Spielzugs
	 */
	//Angespielte Karte (auch NULL)
	private String		angespielt;
	//Position des Ausspielers
	private int			ausspieler;
	//Position des Spielers am Tisch
	private int    		position;
	//Positionen der Spielenden
	private int[] 		spielende;
	//Karten des Spielers
	private String[] 	spielerhand;
	//Ist der Modus ein Tout?
	private boolean 	du;
	
	
	public Spielzug(String 			 file,
					modus			 spielmodus,
					Karte 			 angespielt,
					int				 ausspieler,
					int 			 positionSpieler,
					int[] 			 spielende,
					ArrayList<Karte> spielerhand) {

	}
	
	public Spielzug(String data) {
		
		//-----------------------angespielte Karte
		int pos = data.indexOf(trenner);
		angespielt = data.substring(0, pos);
		data = data.substring(pos + 1, data.length());
		
		//-----------------------Position des Ausspielers
		pos = data.indexOf(trenner);
		ausspieler = Integer.parseInt(data.substring(0, pos));
		data = data.substring(pos + 1, data.length());
		
		//-----------------------Position des Spielers
		pos = data.indexOf(trenner);
		position = Integer.parseInt(data.substring(0, pos));
		data = data.substring(pos + 1, data.length());
		
		//-----------------------Positionen der Spielenden
		pos = data.indexOf(trenner);
		spielende = new int[2];
		spielende[0] = Integer.parseInt(data.substring(0, pos));
		data = data.substring(pos + 1, data.length());
			if(data.charAt(0) == trenner) {
				spielende[1] = -1;
				data = data.substring(1, data.length());
			} else {
				pos = data.indexOf(trenner);
				spielende[1] = Integer.parseInt(data.substring(0, pos));
				data = data.substring(pos + 1, data.length());
			}
			
		//-----------------------Karten des Spielers
		spielerhand = new String[6];
		for(int i = 0; i < 6; i++) {
			pos = data.indexOf(trenner);
			
		}
		
		//-----------------------Tout?
		
	}
	
	private String identifiziereKarte(Karte k) {
		return k.gibFarbe().toString() + k.gibWert().toString();
	}
	
	private void speichern() {
		
	}
}
