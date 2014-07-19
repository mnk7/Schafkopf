package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import regeln.Controll;
import regeln.Regelwahl;
import lib.Model;
import lib.Model.modus;

public class Server implements Runnable{
	
		//Server, der die Verbindungen verwaltet
		private ServerSocket server;
		private Thread listener;
		
		//Speichert den Spielstand
		private Model model;
        
        //hält alle 4 Spieler, ob Bot oder Mensch
        private ArrayList<Spieler> spieler;
        
        private String[] geklopft;
        
        private boolean[] kontra;
        
        //speichert den Spielmodus
        private modus mod;
        
        Controll regeln;
        Regelwahl regelwahl;
        
        private int spielt;
        int mitspieler;
        
        //fragt ab, ob noch ein Spiel gemacht wird
        private boolean nocheins;
                
        /**
         * Erstellt einen neuen Server
         **/
        public Server() {
        	
        	model = new Model();
              
        	spieler = new ArrayList<Spieler>();
        	
        	geklopft = new String[4];
        	
        	kontra = new boolean[4];
        	
        	regelwahl = new Regelwahl();
        	
        	try {
        		//Server für jeden Port
				server = new ServerSocket(5555);
				listener = new Thread(this);
				
				listener.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
        
        /**
         * Nimmt die Verbindungen auf
         */
        public void run() {
        	try {
        		while(true) {
	        		Socket client = server.accept();
	        		spieler.add(new Mensch(client));
	        		
	        		if(spieler.size() == 4 && nocheins)
	        			neuesSpiel();
        		}
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        
        /**
         * Erstellt ein neues Spiel, dass läuft bis ein Spieler austritt
         */
        public void neuesSpiel() {
        	
        	System.out.println("JAAA");
        	
        	//Spiel wurde gestartet
        	nocheins = false;
        		
        		//gibt jedem Spieler seine ID
        		for(int i = 0; i < 4; i++) {
        			try {
						spieler.get(i).setzeID(i);
					} catch (Exception e) {
						e.printStackTrace();
						//Bei Fehler abbrechen
						break;
					}
        		}

	        	model.mischen();
	        	model.ersteKartenGeben();
	        	
	        	for(int i = 0; i < 4; i++) {
	        		//Speichert, ob ein Spieler geklopft hat etc.
	        		spieler.get(i).erste3(model);
	        		geklopft[i] = spieler.get(i).gibAntwort();
	        	}
	        	
	        	model.zweiteKartenGeben();
	        	
	        	for(int i = 0; i < 4; i++) {
	        		//speichert, was der Spieler spielen will
	        		spieler.get(i).spielstDu(model);
	        		modus m = Model.modus.valueOf(spieler.get(i).gibAntwort());
	        		
	        		//sollte der Spieler nichts spielen ist der nächste dran
	        		if(m == null) continue;
	        		
	        		//speichert, was gespielt wird
	        		mod = model.werSpielt(m, mod);
	        		
	        		//Wenn das Spiel des aktuellen Spielers über ein anderes geht
	        		if(mod.equals(m)) spielt = i;
	        	}
	        	
	        	//will niemand spielen geht es zur nächsten Runde
	        	if(mod == null) continue;
	        	//Wenn ein Si gespielt wird
	        	if(mod == modus.SI) {
	        		rundeBeenden();
	        		continue;
	        	}
	        	
	        	//Sendet den Modus an alle Spieler und empfängt, ob kontra gegeben wurde
	        	for(int i = 0; i < 4; i++) {
	        		String k;
					try {
						k = spieler.get(i).modus(mod);
					} catch (Exception e) {
						e.printStackTrace();
						k = null;
					}
	        		
	        		if(k == null || k == "") kontra[i] = false;
	        		else kontra[i] = true;
	        	}	    
	        	
	        	//legt die Regeln fest
	        	regeln = regelwahl.wahl(mod);
	        	
	        	//bestimmt einen eventuellen Mitspieler
	        	mitspieler = regeln.mitspieler(model);
	        	
	        	//Spielen
	        	for(int i = 0; i < 6; i++) {
	        		for(int j = 0; j < 4; j++) {
	        			spieler.get(i).spielen(model);
	        			model = spieler.get(i).gibModel();
	        			
	        			//Wenn ein Fehler auftritt beenden
	        			if(model == null) {
	        				nocheins = false;
	        				break;
	        			}
	        		}
	        		//Wenn ein Fehler aufgetreten ist
	        		if(!nocheins) break;
	        		
	        		//einen Stich zuteilen
	        		int sieger = regeln.sieger(model);
	        		model.Stich(sieger);
	        	}
	        	
	        	rundeBeenden();
	        	
	        	//neu Runde
	        	naechster();
	        	
	        	//Es darf ein neues Spiel gestartet werden
	        	nocheins = true;
        }
        
        /**
         * Der nächste spieler ist an der Reihe
         */
        private void naechster() {
        	//Den ersten Spieler auf die letzte Position
        	Spieler s = spieler.get(0);
        	spieler.remove(s);
        	spieler.add(s);
        }
        
        /**
         * Beendet die Runde
         */
        private void rundeBeenden() {
        	for(int i = 0; i < 4; i++) {
        		try {
					spieler.get(i).sieger(spielt, mitspieler);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	
        	//Den Spielern Geld abziehen oder hinzufügen
        }
        
        /**
         * Gibt alle Spieler zurück, damit diese angezeigt werden können
         * @return spieler
         */
        public ArrayList<Spieler> gibSpieler() {
        	return spieler;
        }

}
