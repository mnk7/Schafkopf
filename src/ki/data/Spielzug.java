package ki.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import lib.Karte;
import lib.Model.modus;

public class Spielzug {
	//Speicherort
	private BufferedWriter printer;
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
	//Die gespielten Trümpfe in sortierte Reihenfolge
	private String 		gespielteTrümpfe;
	//Karten auf dem Tisch
	private String		tisch;
	//Karten, die früher gespielt wurden und der erreichte Erfolg
	private ArrayList<String> 	getesteteKarten;
	private ArrayList<Integer>  getestetePunktzahl;
	private ArrayList<Integer>	getestetesSpielGewonnen;
	
	
	public Spielzug(BufferedWriter 	 printer,
					Karte 			 angespielt,
					int				 ausspieler,
					int 			 positionSpieler,
					int[] 			 spielende,
					ArrayList<Karte> spielerhand,
					boolean 		 du,
					ArrayList<Karte> gespielte,
					Karte[]			 tisch) {
		this.printer = printer;
		
		this.angespielt    =    identifiziereKarte(angespielt);
		this.ausspieler    =    ausspieler;
		this.position      =    positionSpieler;
		this.spielende     =    spielende;
		
		for(int i = 0; i < spielerhand.size(); i++) {
			this.spielerhand[i] = identifiziereKarte(spielerhand.get(i));
		}
		
		this.du            =    du;
		
		//Sortiere schon gespielte Karten (um die Szenarien vergleichen zu können müssen die gespielten Karten
		Karte k, l;						// immer auf die gleiche Weise angeordnet sein)
		for(int i = 0; i < gespielte.size(); i++) {
			k = gespielte.get(i);
			for(int j = i; j < gespielte.size(); j++) {
				l = gespielte.get(j);
				if((k.gibFarbe().ordinal()*6 + k.gibWert().ordinal())
						< (l.gibFarbe().ordinal()*6 + l.gibWert().ordinal())) {
					//Setzt höherwertige Karte auf i und fügt k wieder hinten an Liste an.
					gespielte.set(i, l);
					gespielte.add(k);
					k = l;
				}
			}
		}
		for(int i = 0; i < gespielte.size(); i++) {
			this.gespielteTrümpfe += identifiziereKarte(gespielte.get(i));
		}
		
		for(int i = 0; i < tisch.length; i++) {
			this.tisch += identifiziereKarte(tisch[i]);
		}
		
		getesteteKarten = new ArrayList<String>();
		getestetePunktzahl = new ArrayList<Integer>();
		getestetesSpielGewonnen = new ArrayList<Integer>();
	}
	
	public Spielzug(String data, BufferedWriter printer) {
		
		this.printer = printer;
		
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
			spielerhand[i] = data.substring(0, pos);
			data = data.substring(pos + 1, data.length());
		}
		
		//-----------------------Tout?
		pos = data.indexOf(trenner);
		if(data.substring(0, pos).equals("WAHR")) {
			du = true;
		} else {
			du = false;
		}
		data = data.substring(pos + 1, data.length());
		
		//-----------------------Gespielte Karten
		pos = data.indexOf(trenner);
		gespielteTrümpfe = data.substring(0, pos);
		data = data.substring(pos + 1, data.length());
		
		//-----------------------Karten auf dem Tisch
		pos = data.indexOf(trenner);
		tisch = data.substring(0, pos);
		data = data.substring(pos + 1, data.length());
		
		//-----------------------Schon getestete Karten
		getesteteKarten = new ArrayList<String>();
		getestetePunktzahl = new ArrayList<Integer>();
		getestetesSpielGewonnen = new ArrayList<Integer>();
		
		while(data.length() > 0) {
			pos = data.indexOf(trenner);
			getesteteKarten.add(data.substring(0, pos));
			data = data.substring(pos + 1, data.length());
			
			pos = data.indexOf(trenner);
			getestetePunktzahl.add(Integer.parseInt(data.substring(0, pos)));
			data = data.substring(pos + 1, data.length());
			
			pos = data.indexOf(trenner);
			getestetesSpielGewonnen.add(Integer.parseInt(data.substring(0, pos)));
			data = data.substring(pos + 1, data.length());
		}
	}
	
	/**
	 * Wählt aus, welche Karte aufgrund der gesammelten Ergebnise die beste wäre
	 * @return zu spielende Karte
	 */
	public String welcheSpielen() {
		int zu_spielende = 0;
		int punkte = getestetesSpielGewonnen.get(0);
		int vergleich;
				
		for(int i = 0; i < getesteteKarten.size(); i++) {
			vergleich = getestetesSpielGewonnen.get(i);
			
			//Vergleich mit welcher Karte mehr Spiele gewonnen wurden
			if(vergleich > punkte) {
				zu_spielende = 0;
				punkte = vergleich;
				
				//sind beide Karten gleichauf, zählt mit welcher Karte mehr Punkte erreicht wurden
			} else if(vergleich == punkte) {
				if(getestetePunktzahl.get(i) > getestetePunktzahl.get(zu_spielende)) {
					zu_spielende = i;
				}
			}
		}
		
		return getesteteKarten.get(zu_spielende);
	}
	 /**
	  * Speichert nachdem ein Spielzug durchgeführt wurde dessen Ergebnis
	  * @param gespielt
	  * @param punkte
	  * @param gewonnen
	  */
	public void erinnern(Karte gespielt, int punkte, boolean gewonnen) {
		String karte = identifiziereKarte(gespielt);
		boolean schon_getestet = false;
		
		//Addiert ein gewonnenes oder subtrahiert ein verlorenes Spiel
		int g = -1;
		if(gewonnen) g = 1;
		
		for(int i = 0; i < getesteteKarten.size(); i++) {
			if(getesteteKarten.get(i).equals(karte)) {
				schon_getestet = true;
				//Addiert Gewinne oder Verluste auf die Punktzahl
				getestetePunktzahl.set(i, getestetePunktzahl.get(i) + punkte);
				getestetesSpielGewonnen.set(i, getestetesSpielGewonnen.get(i) + g);
				
				break;
			}
		}
		
		if(!schon_getestet) {
			getesteteKarten.add(karte);
			getestetePunktzahl.add(punkte);
			getestetesSpielGewonnen.add(g);
		}
	}
	
	private String identifiziereKarte(Karte k) {
		return k.gibFarbe().toString() + k.gibWert().toString();
	}
	
	/**
	 * Gibt an, welche Karte angespielt wurde
	 * @return
	 */
	public String gibAngespielt() {
		return angespielt;
	}
	
	/**
	 * Gibt an, welche ID zum Spieler gehört
	 * @return
	 */
	public int gibSpielerID() {
		return position;
	}
	
	/**
	 * Gibt an, welche ID zum Ausspieler gehört
	 * @return
	 */
	public int gibAusspieler() {
		return ausspieler;
	}
	
	/**
	 * Gibt an, wer spielt
	 * @return
	 */
	public int[] gibSpielende() {
		return spielende;
	}
	
	/**
	 * Gibt an, ob ein Tout gespielt wird
	 * @return
	 */
	public boolean istDu() {
		return du;
	}
	
	/**
	 * Gibt an, welche Trümpfe schon gespielt wurden
	 * @return
	 */
	public String gibgespielteTrümpfe() {
		return gespielteTrümpfe;
	}
	
	/**
	 * Speichert den enthaltenen Datensatz, indem in den übergenenen Stream geschrieben wird 
	 * @throws IOException
	 */
	public void speichern() throws IOException {
		printer.write(angespielt + trenner);
		printer.write(String.valueOf(ausspieler) + trenner);
		printer.write(String.valueOf(position) + trenner);
		printer.write(String.valueOf(spielende[0]) + trenner);
		printer.write(String.valueOf(spielende[1]) + trenner);
		
		for(int i = 0; i < spielerhand.length; i++) {
			printer.write(spielerhand[i] + trenner);
		}
		
		if(du) {
			printer.write("WAHR" + trenner);
		} else {
			printer.write("FALSCH" + trenner);
		}

		printer.write(gespielteTrümpfe + trenner);
		printer.write(tisch + trenner);
		
		for(int i = 0; i < getesteteKarten.size(); i++) {
			printer.write(getesteteKarten.get(i) 
					+ String.valueOf(getestetePunktzahl.get(i)) 
					+ String.valueOf(getestetesSpielGewonnen.get(i)) 
					+ trenner);
		}
		
		printer.write("\n");
	}
}
