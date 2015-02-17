package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import regeln.Hochzeit;
import regeln.Control;
import regeln.Regelwahl;
import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Server extends Thread {
	
		private static int PORT = 35555;
	
		//Server, der die Verbindungen verwaltet
		private ServerSocket server;
		private boolean beenden;
		
		//Speichert den Spielstand
		private Model model;
        
        //hält alle 4 Spieler, ob Bot oder Mensch
        private ArrayList<Spieler> spieler;
        private int spielerzahl;
        
        private boolean[] geklopft;
        
        private boolean[] kontra;
        
        //Speichert die Höhe des Stocks
        private int stock;
        
        //speichert den Spielmodus
        private modus mod;
        
        private Control regeln;
        private Regelwahl regelwahl;
        
        private int spielt;
        private int mitspieler;
        
        //fragt ab, ob noch ein Spiel gestartet werden kann
        private boolean nocheins;
        
        private final Graphik graphik;
        
        /**
         * Erstellt einen Server auf dem Standardport
         * @param graphik
         */
        public Server(Graphik graphik) {
        	this(graphik, PORT);
        }
                
        /**
         * Erstellt einen neuen Server
         * @param graphik
         * @param port
         **/
        public Server(Graphik graphik, int port) {
        	super();
        	this.setName("Schafkopf-Server");
        	
        	this.PORT = port;
        	
        	beenden = false;
        	
        	model = new Model();
              
        	spieler = new ArrayList<Spieler>();
        	spielerzahl = 4;
        	
        	geklopft = new boolean[4];
        	
        	kontra = new boolean[4];
        	
        	regelwahl = new Regelwahl();
        	
        	stock = 0;
        	
        	nocheins = true;
        	
        	this.graphik = graphik;
        	
        	try {
				server = new ServerSocket(PORT);
        		
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        } 
        
        /**
         * Nimmt die Verbindungen auf
         */
        public void run() {
        	try {
        		while(!beenden) {
        			//Akzeptiert die Verbindung
	        		Socket client = server.accept();
	        		
	        		Mensch neuerSpieler = new Mensch(client, this);
	        		spieler.add(neuerSpieler);
	        		
	        		neuerSpieler.name();
	        		
	        		ViewTextSetzen();
	        		
	        		//Wenn die maximale Anzahl an Spielern erreicht ist und nicht gerade gespielt wird
	        		if(spieler.size() == spielerzahl && nocheins) {
	        			nocheins = false;
	        			//Mit Bots auffüllen
	        			for(int i = spielerzahl; i < 4; i++) {
	        				spieler.add(new Bot());
	        			}
	        			neuesSpiel();
	        		} 
        		}
        	} catch(Exception e) {
        		e.printStackTrace();
        		//Alle Spieler zurücksetzen
        		for(int i = 0; i < spieler.size(); i++) {
        			spieler.get(i).abmelden();
        		}
        		beenden = true;
        	}
        }
        
        /** 
         * Erstellt ein neues Spiel
         * Unbedingt in einzelne Methoden ausgliedern!
         * @throws Exception 
         */
        private void neuesSpiel() throws Exception {
        	
        	synchronized (model) {
        	synchronized (spieler) {
        	
        	//Spiel wurde gestartet
        	while(!nocheins) {
        		
        		//Am Anfang jeder Runde ein neues Model erzeugen
        		model = new Model();
        		
        		//Übergibt die Spieler dem Model und gibt jedem Spieler eine ID, die seiner Stelle in <spieler> entspricht
        		spielerVorbereiten();

        		//Mischt die Karten und teilt die ersten 3 aus
	        	model.mischen();
	        	model.ersteKartenGeben();
	        	
	        	//Frägt alle Spieler, ob sie klopfen wollen
	        	klopfen();
	        	
	        	//Teilt die Karten ganz aus
	        	model.zweiteKartenGeben();
	        	
	        	//Frägt alle Spieler, was sie Spielen wollen
	        	//Ermittelt danach das Spiel, das gespielt wird und führt gegebenenfalls eine Hochzeit durch
	        	werspielt();
	        	
	        	//will niemand spielen geht es zur nächsten Runde
	        	if(mod.equals(modus.NICHTS) || spielt == -1) {
	        		stock();
	        		break;
	        	}
	        	//legt die Regeln fest
	        	regeln = regelwahl.wahl(mod, model, spielt);
	        	if(regeln == null) {
	        		stock();
	        		break;
	        	}
	        	
	        	//Wenn keine Hochzeit gespielt wird, Mitspieler feststellen
	        	if(!mod.equals(modus.HOCHZEIT)) {
	        		//bestimmt einen eventuellen Mitspieler
		        	mitspieler = regeln.mitspieler(model);   
	        	}
	        	
	        	kontra();
	        	
	        	//Wenn ein Si gespielt wird die Runde gar nicht erst spielen
	        	if(mod.equals(modus.SI)) {
	        		rundeBeenden();
	        		continue;
	        	}
	        	
	        	//Spielen
	        	for(int i = 0; i < 6; i++) {
	        		for(int j = 0; j < 4; j++) {
	        			//Übergibt dem Spieler das aktuelle Model und...
	        			spieler.get(i).spielen(model);
	        			//...empfängt das aktualisierte
	        			model = spieler.get(i).gibModel();
	        			
	        			//Wenn ein Fehler auftritt beenden
	        			if(model == null) {
	        				nocheins = true;
	        				break;
	        			}
	        		}
	        		//Wenn ein Fehler aufgetreten ist
	        		if(nocheins) break;
	        		
	        		//einen Stich zuteilen
	        		int sieger = regeln.sieger(model);
	        		model.Stich(sieger);
	        	}

	        	rundeBeenden();
	        	
	        	//neu Runde
	        	naechster();
        	}
        	}//synchronized spieler
        	}//synchronized model
        }
        
        /**
         * Sendet die Spielernamen an alle und weist jedem Spieler seine ID zu
         * @throws Exception
         */
        private void spielerVorbereiten() throws Exception {
        	
        	//Anzeigen der Spieler
        	for(int i = 0; i < 4; i++) {
        		model.setzeName(i, spieler.get(i).gibName());
        	}
    		
    		//gibt jedem Spieler seine ID
    		for(int i = 0; i < 4; i++) {
    			try {
    				spieler.get(i).setzeModel(model);
					spieler.get(i).setzeID(i);
				} catch (Exception e) {
					e.printStackTrace();
					//Bei Fehler abbrechen
					spieler.get(i).abmelden();
					nocheins = true;
					throw e;
				}
    		}
        }
        
        /**
         * Ermittelt, welche Spieler klopfen
         * @throws Exception
         */
        private void klopfen() throws Exception {
        	erste3();
        	klopfenAbfragen();
        }
        	private void erste3() throws Exception {
        		for(int i = 0; i < 4; i++) {
    	        	//Speichert, ob ein Spieler geklopft hat etc.
    	        	geklopft[i] = spieler.get(i).erste3(model);
            	}
        	}
        	
        	private void klopfenAbfragen() throws Exception {
        		//Spieler benachrichtigen, wer geklopft hat
            	for(int i = 0; i < 4; i++) {
            		spieler.get(i).geklopft(geklopft);
            	}
        	}
        
        /**
         * Ermittelt, welcher Spieler spielt
         * @throws Exception
         */
        private void werspielt() throws Exception {
        	//Speichert, wer was spielen will
        	ArrayList<modus> spielfolge = new ArrayList<modus>();
        	
        	for(int i = 0; i < 4; i++) {
        		try {
        			spielfolge.add(spieler.get(i).spielstDu(model));
        		} catch(Exception e) {
        			e.printStackTrace();
        			//Wird ein Fehler zurückgegeben, so wird dieser Spieler nicht berücksichtigt
        			spielfolge.add(modus.NICHTS);
        		}
        	}
        	
        	hoechstesSpiel(spielfolge);
        }
        
        /**
         * Stellt fest, was gespielt wird
         * @param spielfolge
         * @throws Exception 
         */
        private void hoechstesSpiel(ArrayList<modus> spielfolge) throws Exception {
        	//ermittelt das höchstwertige Spiel
        	mod = spielfolge.get(0);
        	for(int i = 1; i < 4; i++) {
        		mod = model.werSpielt(mod, spielfolge.get(i));
        		
        		if(mod.equals(spielfolge.get(i))) {
        			spielt = i;
        		}
        	}
        	
        	//Wenn eine Hochzeit nicht erlaubt ist, wird unter den restlichen Spielen ein höchstes ermittelt
        	if(mod.equals(modus.HOCHZEIT) && !hochzeit(spielt)) {
        		spielfolge.remove(spielt);
        		spielt = -1;
        		hoechstesSpiel(spielfolge);
        	}
        }
        
        /**
         * Führt eine Hochzeit zwischen zwei Spielern durch
         * @throws Exception 
         */
        private boolean hochzeit(int spielt) throws Exception {
        	Hochzeit h = (Hochzeit) regeln;
    		
    		Karte angebot = spieler.get(spielt).gibKarte();
    		
    		if(h.hochzeitMoeglich(model, spielt, angebot)) {
        		for(int i = 0; i < 4; i++) {
        			
        			if(i != spielt) {
        				
        				if(spieler.get(i).hochzeit()) {
        					//Wenn die Hochzeit angenommen wird
        					Karte k = spieler.get(i).gibKarte();
        					
        					//Wenn die Karte ein Trumpf ist
	        				if(!h.istTrumpf(k.gibWert(), k.gibFarbe())) {
	        					model.hochzeit(spielt, i, angebot, k);
	        					mitspieler = i;
	        					return true;
	        				} else {
	        					return false;
	        				}
        				} else {
        					return false;
        				}
        			} else {
        				continue;
        			}
        		}
    		} else {
    			return false;
    		}
    		
    		//Diese Fall ist nicht möglich, da i != spielt irgendwann zutrifft
    		return false;
        }
        
        private void kontra() throws Exception {
        	//Sendet den Modus an alle Spieler und empfängt, ob Kontra gegeben wurde
        	kontraAbfragen();
        	spielerSenden();
        	kontraSenden();
        }
        
        	private void kontraAbfragen() throws Exception {
        		for(int i = 0; i < 4; i++) {
        			kontra[i] = spieler.get(i).modus(mod);
        		}
        	}
        	
        	private void spielerSenden() throws Exception {
        		for(int i = 0; i < 4; i++) {
    				//Wenn eine Hochzeit gespielt wird, werden beide Spielenden gesendet
    				int mit = 4;
    				if(mod.equals(modus.HOCHZEIT)) {
    					mit = mitspieler;
    				}
    				spieler.get(i).spieler(spielt, mit);
            	}	
        	}
        	
        	private void kontraSenden() throws Exception {
        		for(int i = 0; i < 4; i++) {
            		spieler.get(i).kontra(kontra);
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
        private synchronized void rundeBeenden() {        	
        	int pSpielt;
        	if(mod.equals(modus.SI)) {
        		//Der Spieler hat alle Punkte
        		pSpielt = 120;
        	}
        	
        	ArrayList<Integer> punkte = model.gibPunkte();
        	//Die Punkte des Spielers
        	pSpielt = punkte.get(spielt);
        	//und vielleicht des Mitspielers
        	if(mitspieler != 4)
        		pSpielt += punkte.get(mitspieler);
        	
        	//Wenn ein Du gespielt wurde
        	String modString = mod.toString();
        	//Die letzten zwei Buchstaben werden verglichen
        	if(modString.substring(modString.length() - 3, modString.length() - 1)
        			.toLowerCase()
        			.equals("du")) {
        		if(pSpielt != 120)
        			//Wenn der Du verloren wurde
        			spielt += 10;
        	} else 
        	//Ansonsten wird normal verrechnet
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
         * Füllt den Stock auf
         */
        private void stock() {
        	
        }
        
        /**
         * Gibt alle Spieler zurück, damit diese angezeigt werden können
         * @return spieler
         */
        public synchronized ArrayList<Spieler> gibSpieler() {
        	return spieler;
        }
        
        /**
         * Entfernt einen Spieler und sorgt dafür, dass das Spiel unterbrochen wird
         * @param i
         */
        public synchronized void entferneSpieler(Spieler s) {
        	spieler.remove(s);
        	try {
				javax.swing.JOptionPane.showMessageDialog(graphik, "Spieler " + s.gibName() +" wurde entfernt");
			} catch (Exception e) {
			}
        	
        	ViewTextSetzen();
        	nocheins = true;
        }
        
        /**
         * Setzt die Spielerzahl (menschliche Spieler)
         * @param spielerzahl
         */
        public void setzeSpielerzahl(int spielerzahl) {
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
         * Aktualisiert die Anzeige der Spieler
         */
        public synchronized void ViewTextSetzen() {
        	graphik.textSetzen(spieler);
        }
        
        /**
         * Beendet den Server
         */
        @SuppressWarnings("deprecation")
		public synchronized void beenden() {
        	try {
        		beenden = true;
				server.close();
				//Spieler werden entfernt
				ArrayList<Spieler> s = (ArrayList<Spieler>) spieler.clone();
				for(int i = 0; i < s.size(); i++) {
					s.get(i).abmelden();
				}
				
				this.suspend();
			} catch (IOException e) {
				e.printStackTrace();
				//Programm beenden
				System.exit(0);
			}
        }
}
