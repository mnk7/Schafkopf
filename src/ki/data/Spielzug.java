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
	//Positionen der Spielenden (1 und 3 spielen -> spielende = 13)
	private int 		spielende;
	//Karten des Spielers
	private String[] 	spielerhand;
	//Ist der Modus ein Tout? (Vorerst nicht berücksichtigt)
	private boolean 	du;
	//Die gespielten Trümpfe in sortierte Reihenfolge
	private String 		gespielteKarten;
	//Karten auf dem Tisch 
	private String		tisch;
	//Karten, die früher gespielt wurden und der erreichte Erfolg
	private ArrayList<String> 	getesteteKarten;
	private ArrayList<Integer>  getestetePunktzahl;
	private ArrayList<Integer>	getestetesSpielGewonnen;
	private ArrayList<Integer>  getestetesSpielVerloren;
	
	private String primaerschluessel;
	
	//Speichert zu erinnernde Wert zwischen
	private Karte gespielt;
	private int punkte;
	
	/**
	 * Erstellt einen neuen Datensatz
	 * @param angespielt
	 * @param ausspieler
	 * @param positionSpieler
	 * @param spielende
	 * @param spielerhand
	 * @param du
	 * @param gespielte
	 * @param tisch
	 */
	public Spielzug(Karte 			 angespielt,
					int				 ausspieler,
					int 			 positionSpieler,
					int 			 spielende,
					ArrayList<Karte> spielerhand,
					boolean 		 du,
					ArrayList<Karte> gespielte,
					Karte[]			 tisch) {
		
		this.angespielt    =    angespielt.gibString();
		this.ausspieler    =    ausspieler;
		this.position      =    positionSpieler;
		this.spielende     =    spielende;
		
		this.spielerhand = new String[spielerhand.size()];
		for(int i = 0; i < spielerhand.size(); i++) {
			this.spielerhand[i] = spielerhand.get(i).gibString();
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
			this.gespielteKarten += gespielte.get(i).gibString();
		}
		
		for(int i = 0; i < tisch.length; i++) {
			this.tisch += tisch[i].gibString();
		}
		
		getesteteKarten = new ArrayList<String>();
		getestetePunktzahl = new ArrayList<Integer>();
		getestetesSpielGewonnen = new ArrayList<Integer>();
		getestetesSpielVerloren = new ArrayList<Integer>();
		
		generierePS();
	}
	
	/**
	 * Öffnet einen gespeicherten Datensatz
	 * @param data
	 */
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
		spielende = Integer.parseInt(data.substring(0, pos));
		data = data.substring(pos + 1, data.length());
			
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
		gespielteKarten = data.substring(0, pos);
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
			
			pos = data.indexOf(trenner);
			getestetesSpielVerloren.add(Integer.parseInt(data.substring(0, pos)));
			data = data.substring(pos + 1, data.length());
		}
		
		generierePS();
	}
	
	/**
	 * Wählt aus, welche Karte aufgrund der gesammelten Ergebnise die beste wäre
	 * @return zu spielende Karte
	 */
	public String welcheSpielen() {
		int zu_spielende = 0;
		float punkte = getestetesSpielGewonnen.get(0) / getestetesSpielVerloren.get(0);
		float vergleich;
				
		for(int i = 0; i < getesteteKarten.size(); i++) {
			vergleich = getestetesSpielGewonnen.get(i) / getestetesSpielVerloren.get(i);
			
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
	public void erinnern(Karte gespielt, int punkte) {
		this.gespielt = gespielt;
		this.punkte = punkte;
	}
	
	/**
	 * Speichert, ob das Spiel gewonnen wurde
	 * @param gewonnen
	 */
	public void erinnereGewonnen(boolean gewonnen) {
		String karte = gespielt.gibString();
		boolean schon_getestet = false;
		
		for(int i = 0; i < getesteteKarten.size(); i++) {
			if(getesteteKarten.get(i).equals(karte)) {
				schon_getestet = true;
				//Addiert Gewinne oder Verluste auf die Punktzahl
				getestetePunktzahl.set(i, getestetePunktzahl.get(i) + punkte);
				
				if(gewonnen) {
					getestetesSpielGewonnen.set(i, getestetesSpielGewonnen.get(i) + 1);
				} else {
					getestetesSpielVerloren.set(i, getestetesSpielVerloren.get(i) + 1);
				}
				
				break;
			}
		}
		
		if(!schon_getestet) {
			getesteteKarten.add(karte);
			getestetePunktzahl.add(punkte);
			
			if(gewonnen) {
				getestetesSpielGewonnen.add(1);
			} else {
				getestetesSpielVerloren.add(1);
			}
		}
	}
	
	/**
	 * Setzt den Printer auf eine andere Datei
	 * @param printer
	 */
	public void setzePrinter(BufferedWriter printer) {
		this.printer = printer;
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
	public int gibSpielende() {
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
	public String gibGespielteKarten() {
		return gespielteKarten;
	}
	
	/**
	 * Liefert den Primärschlüssel des Objekts
	 * @return PS
	 */
	public String gibPS() {
		return primaerschluessel;
	}
	
	/**
	 * Generiert den Primärschlüssel des Spielzugs
	 */
	private void generierePS() {
		primaerschluessel = angespielt;
		primaerschluessel += String.valueOf(ausspieler);
		primaerschluessel += String.valueOf(position);
		primaerschluessel += String.valueOf(spielende);
		for(int i = 0; i < spielerhand.length; i++) {
			primaerschluessel += String.valueOf(spielerhand[i]);
		}
		primaerschluessel += String.valueOf(du);
		primaerschluessel += tisch;
		primaerschluessel += gespielteKarten;
	}
	
	/**
	 * Speichert den enthaltenen Datensatz, indem in den übergenenen Stream geschrieben wird 
	 * @throws IOException
	 */
	public void speichern() throws IOException {
		printer.write(angespielt + trenner);
		printer.write(String.valueOf(ausspieler) + trenner);
		printer.write(String.valueOf(position) + trenner);
		printer.write(String.valueOf(spielende) + trenner);
		
		for(int i = 0; i < spielerhand.length; i++) {
			printer.write(spielerhand[i] + trenner);
		}
		
		if(du) {
			printer.write("WAHR" + trenner);
		} else {
			printer.write("FALSCH" + trenner);
		}

		printer.write(gespielteKarten + trenner);
		printer.write(tisch + trenner);
		
		for(int i = 0; i < getesteteKarten.size(); i++) {
			printer.write(getesteteKarten.get(i) + trenner
					+ String.valueOf(getestetePunktzahl.get(i)) + trenner 
					+ String.valueOf(getestetesSpielGewonnen.get(i)) + trenner
					+ String.valueOf(getestetesSpielVerloren.get(i)) + trenner);
		}
		
		printer.write("\n");
	}
}
