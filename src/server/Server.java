package server;

import regeln.Controll;
import regeln.Regelwahl;
import lib.Model;
import lib.Model.modus;

public class Server {
	
		//Speichert die verfügbaren Ports des Servers
		private int[] ports = {
				55555,
				55556,
				55557,
				55558
		};
		
		//Speichert den Spielstand
		private Model model;
        
        //hält alle 4 Spieler, ob Bot oder Mensch
        private Spieler[] spieler;
        
        private String[] geklopft;
        
        private boolean[] kontra;
        
        //speichert den Spielmodus
        private modus mod;
        
        Controll regeln;
        Regelwahl regelwahl;
        
        private int spielt;
        
        //fragt ab, ob noch ein Spiel gemacht wird
        private boolean nocheins;
        
        /**
         * Erstellt einen neuen Server
         **/
        public Server() {
        	
        	model = new Model();
              
        	spieler = new Spieler[4];
        	
        	geklopft = new String[4];
        	
        	kontra = new boolean[4];
        	
        	regelwahl = new Regelwahl();
        	
        }
        
        public void neuesSpiel() {
        	connect();
        	
        	while(nocheins) {
	        	model.mischen();
	        	model.ersteKartenGeben();
	        	
	        	for(int i = 0; i < 4; i++) {
	        		//Speichert, ob ein Spieler geklopft hat etc.
	        		geklopft[i] = spieler[i].erste3(model);
	        	}
	        	
	        	model.zweiteKartenGeben();
	        	
	        	for(int i = 0; i < 4; i++) {
	        		//speichert, was der Spieler spielen will
	        		modus m = spieler[i].spielstDu(model);
	        		
	        		//sollte der Spieler nichts spielen ist der nächste dran
	        		if(m == null) continue;
	        		
	        		//speichert, was gespielt wird
	        		mod = model.werSpielt(m, mod);
	        		
	        		//Wenn das Spiel des aktuellen Spielers über ein anderes geht
	        		if(mod.equals(m)) spielt = i;
	        	}
	        	
	        	//will niemand spielen geht es zur nächsten Runde
	        	if(mod == null) continue;
	        	
	        	//Sendet den Modus an alle Spieler und empfängt, ob kontra gegeben wurde
	        	for(int i = 0; i < 4; i++) {
	        		String k = spieler[i].modus(mod);
	        		
	        		if(k == null || k == "") kontra[i] = false;
	        		else kontra[i] = true;
	        	}	    
	        	
	        	regeln = regelwahl.wahl(mod);
	        	
	        	//Spielen
	        	for(int i = 0; i < 6; i++) {
	        		for(int j = 0; j < 4; j++) {
	        			model = spieler[i].spielen(model);
	        			
	        			//Wenn ein Fehler auftritt beenden
	        			if(model == null) {
	        				nocheins = false;
	        				break;
	        			}
	        		}
	        		//Wenn ein Fehler aufgetreten ist
	        		if(!nocheins) break;
	        		
	        		int sieger = regeln.sieger(model);
	        		model.Stich(sieger);
	        	}
	        	
	        	//neu Runde
	        	naechster();
        	}
        }
        
        /**
         * Verbindet sich mit den Spielern
         */
        private void connect() {
        	for(int i = 0; i < 4; i++) {
        		try {
        			spieler[i] = new Mensch(ports[i]);
        		} catch(Exception e) {
        			spieler[i] = new Bot();
        		}
        	}
        }
        
        /**
         * Der nächste spieler ist an der Reihe
         */
        private void naechster() {
        	//Speichert den ersten Spieler
        	Spieler s = spieler[0];
        	
        	//Rückt alle Spieler eins vor
        	for(int i = 2; i != 0; i--) {
        		spieler[i] = spieler[i + 1];
        	}
        	
        	//Setzt den ersten Spieler an die letzte Stelle
        	spieler[3] = s;
        }

}
