package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import regeln.Hochzeit;

import regeln.Controll;
import regeln.Regelwahl;
import lib.Karte;
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
        private int spielerzahl;
        
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
        
        private final Graphik graphik;
                
        /**
         * Erstellt einen neuen Server
         **/
        public Server(Graphik graphik) {
        	
        	model = new Model();
              
        	spieler = new ArrayList<Spieler>();
        	spielerzahl = 4;
        	
        	geklopft = new String[4];
        	for(int i = 0; i < 4; i++) {
        		geklopft[i] = "";
        	}
        	
        	kontra = new boolean[4];
        	
        	regelwahl = new Regelwahl();
        	
        	nocheins = true;
        	
        	this.graphik = graphik;
        	
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
	        		
	        		if(spieler.size() == spielerzahl && nocheins) {
	        			nocheins = false;
	        			neuesSpiel();
	        		}
	        		
	        		//Drossel
	        		Thread.sleep(100);
        		}
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        
        /**
         * Erstellt ein neues Spiel
         * @throws Exception 
         */
        private void neuesSpiel() throws Exception {
        	
        	//Spiel wurde gestartet
        	while(!nocheins) {
        		//Anzeigen der Spieler
        		graphik.textSetzen(spieler);
        		
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
	        		
	        		modus m = null;
	        		while(m == null) {
	        			try {
	        				m = Model.modus.valueOf(spieler.get(i).gibAntwort());
	        			} catch(Exception e) {
	        				nocheins = true;
	        				continue;
	        			}
	        			Thread.sleep(100);
	        		}
	        		
	        		//speichert, was gespielt wird
	        		mod = model.werSpielt(m, mod);
	        		
	        		//Wenn das Spiel des aktuellen Spielers über ein anderes geht
	        		if(mod.equals(m)) spielt = i;
	        	}
	        	
	        	//will niemand spielen geht es zur nächsten Runde
	        	if(mod.equals(null)) {
	        		nocheins = true;
	        		continue;
	        	}
	        	//Wenn ein Si gespielt wird
	        	if(mod.equals(modus.SI)) {
	        		rundeBeenden();
	        		nocheins = true;
	        		continue;
	        	}
	        	//legt die Regeln fest
	        	regeln = regelwahl.wahl(mod, model, spielt);
	        	if(regeln == null) {
	        		nocheins = false;
	        		break;
	        	}
	        	
	        	//Wenn eine Hochzeit gespielt wird
	        	if(mod.equals(modus.HOCHZEIT)) {
	        		Hochzeit h = (Hochzeit) regeln;
	        		
	        		Karte angebot = spieler.get(spielt).gibKarte();
	        		
	        		if(h.hochzeitMoeglich(model, spielt, angebot)) {
		        		for(int i = 0; i < 4; i++) {
		        			
		        			if(i != spielt) {
		        				spieler.get(i).hochzeit();
		        				
		        				if(spieler.get(i).gibAntwort().equals("JA")) {
		        					//Wenn die Hochzeit angenommen wird
		        					Karte k = spieler.get(i).gibKarte();
		        					
		        					//Wenn die Karte ein Trumpf ist
			        				if(!h.istTrumpf(k.gibWert(), k.gibFarbe())) {
			        					model.hochzeit(spielt, i, angebot, k);
			        					mitspieler = i;
			        				}
		        				}
		        			}
		        		}
	        		}
	        	} else 
	        		//bestimmt einen eventuellen Mitspieler
		        	mitspieler = regeln.mitspieler(model);   
	        	
	        	//Sendet den Modus an alle Spieler und empfängt, ob kontra gegeben wurde
	        	for(int i = 0; i < 4; i++) {
	        		String k;
					try {
						k = spieler.get(i).modus(mod);
						spieler.get(i).spieler(spielt);
					} catch (Exception e) {
						e.printStackTrace(); 
						k = null;
					}
	        		
	        		if(k == null || k == "") kontra[i] = false;
	        		else kontra[i] = true;
	        	}	
	        	
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
        	ArrayList<Integer> punkte = model.gibPunkte();
        	//Die Punkte des Spielers
        	int pSpielt = punkte.get(spielt);
        	//und vielleicht des Mitspielers
        	if(mitspieler != 4)
        		pSpielt += punkte.get(mitspieler);
        	
        	if(pSpielt <= 60) {
        		//Anzeigen, dass er verloren hat
        		spielt += 10;
        		if(mitspieler != 4)
        			mitspieler += 10;
        	}
        	
        	for(int i = 0; i < 4; i++) {
        		try {
        			spieler.get(i).sieger(spielt, mitspieler);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	
        	//Den Spielern Geld abziehen oder hinzufügen
        	for(int i = 0; i < 4; i++) {
        		if(i == spielt || i == mitspieler) {
        			
        		}
        		if(i == spielt + 10 || i == mitspieler + 10) {
        			
        		}
        	}
        }
        
        /**
         * Gibt alle Spieler zurück, damit diese angezeigt werden können
         * @return spieler
         */
        public synchronized ArrayList<Spieler> gibSpieler() {
        	return spieler;
        }
        
        /**
         * Setzt die Spielerzahl
         * @param spielerzahl
         */
        public void setSpielerzahl(int spielerzahl) {
        	this.spielerzahl = spielerzahl;
        }
        
        /**
         * Gibt die Spielerzahl zurück
         * @return
         */
        public int gibSpielerzahl() {
        	return spielerzahl;
        }
        
        /**
         * Beendet den Server
         */
        public void beenden() {
        	try {
        		listener.stop();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
        }

}
