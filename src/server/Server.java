package server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ki.Spielauswahl;
import regeln.Hochzeit;
import regeln.Control;
import regeln.Regelwahl;
import lib.Karte;
import lib.Model;
import lib.Model.modus;

public class Server extends Thread {
	
	    private static int PORT = 35555;
	    
	    private String configdir;
	
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
        //Speichert den Tarif
        private int tarif;
        //Geld der einzelnen Spieler
        private ArrayList<Integer> konto;
        
        private int wartezeit;
        
        //Schwierigkeitsstufe
        private int handicap;
        
        //speichert den Spielmodus
        private modus mod;
        
        private Control regeln;
        private Regelwahl regelwahl;
        
        private Spielauswahl spielauswahl;
        
        private int spielt;
        private int mitspieler;
        
        //fragt ab, ob noch ein Spiel gestartet werden kann
        private boolean nocheins;
        
        private final Graphik graphik;
        
        /**
         * Erstellt einen Server auf dem Standardport
         * @param graphik
         */
        public Server(Graphik graphik, String configdir) {
        	this(graphik, PORT, configdir);
        }
                
        /**
         * Erstellt einen neuen Server
         * @param graphik
         * @param port
         * @throws Exception 
         **/
        public Server(Graphik graphik, int port, String configdir) {
        	super();
        	this.setName("Schafkopf-Server");
        	
        	this.PORT = port;
        	
        	beenden = false;
        	
        	model = new Model();
              
        	spieler = new ArrayList<Spieler>();
        	spielerzahl = 4;
        	
        	wartezeit = 2000;
        	
        	handicap = 3;
        	
        	geklopft = new boolean[4];
        	
        	kontra = new boolean[4];
        	
        	regelwahl = new Regelwahl();
        	
        	stock = 0;
        	tarif = 10;
        	konto = new ArrayList<Integer>();
        	for(int i = 0; i < 4; i++) {
        		konto.add(100*tarif);
        	}
        	
        	nocheins = true;
        	
        	this.graphik = graphik;
        	
        	try {
				server = new ServerSocket(PORT);
				
				spielauswahl = new Spielauswahl(configdir);
        		
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        } 
        
        /**
         * Nimmt die Verbindungen auf
         */
        public void run() {
        	try {
        		while(!beenden) {
        			if(spieler.size() < 4) {
	        			//Akzeptiert die Verbindung
		        		Socket client = server.accept();
		        		
		        		Mensch neuerSpieler = new Mensch(client, this);
		        		spieler.add(neuerSpieler);
		        		
		        		neuerSpieler.name();
		        		
		        		ViewTextSetzen();
		        		
		        		starten();
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
         * Prüft, ob das Spiel gestartet werden kann und wenn ja, startet ein neues Spiel
         * @throws Exception
         */
        private void starten() throws Exception {
        	//Wenn die maximale Anzahl an Spielern erreicht ist und nicht gerade gespielt wird
    		if(spieler.size() == spielerzahl && nocheins) {
    			nocheins = false;
    			//Mit Bots auffüllen
    			for(int i = 4; i > spielerzahl; i--) {
    				spieler.add(new Bot(this, 4 - i, spielauswahl, wartezeit, handicap));
    			}
    			ViewTextSetzen();
    			
    			neuesSpiel();
    		} 
        }
        
        /** 
         * Erstellt ein neues Spiel
         * Unbedingt in einzelne Methoden ausgliedern!
         * @throws Exception 
         */
        private void neuesSpiel() throws Exception {
        	//Konten aktualisieren
        	setzeTarif(tarif);
        	
        	//Spiel wurde gestartet
        	while(!nocheins) {
        		
        		//Am Anfang jeder Runde ein neues Model erzeugen
        		model = new Model();
        		
        		//Übergibt die Spieler dem Model und gibt jedem Spieler eine ID, die seiner Stelle in <spieler> entspricht
        		spielerVorbereiten();
        		kontoAusgeben();

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
	        		kontostand();
	        		naechster();
	        		continue;
	        	}
	        	//legt die Regeln fest
	        	regeln = regelwahl.wahl(mod);
	        	if(regeln == null) {
	        		stock();
	        		kontostand();
	        		naechster();
	        		continue;
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
	        		kontostand();
	        		naechster();
	        		continue;
	        	}
	        	
	        	//Errechnet, wie viele Laufende die Spieler haben
	        	model.errechneLaufende(spielt, mitspieler, mod);
	        	
	        	spiel();

	        	kontostand();
	        	rundeBeenden();
	        	
	        	//neue Runde
	        	naechster();
        	}
        }
        
        /**
         * Zeigt an, wer gerade am Zug ist
         */
        private void weristdran(int ID) {
        	for(int i = 0; i < 4; i++) {
        		spieler.get(i).weristdran(ID);
        	}
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
        	klopfenAngeben();
        }
        	private void erste3() throws Exception {
        		for(int i = 0; i < 4; i++) {
        			weristdran(i);
    	        	//Speichert, ob ein Spieler geklopft hat etc.
    	        	geklopft[i] = spieler.get(i).erste3(model);
            	}
        	}
        	
        	private void klopfenAngeben() throws Exception {
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
        		weristdran(i);
        		try {
        			spielfolge.add(spieler.get(i).spielstDu(model, bestesSpielFinden(spielfolge)));
        		} catch(Exception e) {
        			e.printStackTrace();
        			//Wird ein Fehler zurückgegeben, so wird dieser Spieler nicht berücksichtigt
        			spielfolge.add(modus.NICHTS);
        		}
        	}
        	
        	hoechstesSpiel(spielfolge);
        }
        
        private modus bestesSpielFinden(ArrayList<modus> spielfolge) {
        	if(spielfolge.size() == 0) {
        		return modus.NICHTS;
        	}
        	if(spielfolge.size() == 1) {
        		return spielfolge.get(0);
        	}
        	//ermittelt das höchstwertige Spiel
        	modus modus = spielfolge.get(spielfolge.size() - 1);
        	for(int i = spielfolge.size() - 2; i >= 0; i--) {
        		modus = model.werSpielt(modus, spielfolge.get(i));
        	}
        	return modus;
        }
        
        /**
         * Stellt fest, was gespielt wird
         * @param spielfolge
         * @throws Exception 
         */
        private void hoechstesSpiel(ArrayList<modus> spielfolge) throws Exception {
        	//ermittelt das höchstwertige Spiel
        	spielErmitteln(spielfolge);
        	
        	//Wenn zwei Spieler das Gleiche spielen, spielt der, der zuerst spielt
        	for(int i = spielfolge.size() - 1; i >= 0; i--) {
        		if(spielfolge.get(i).equals(mod)) {
        			spielt = i;
        		}
        	}
        	
        	//Wenn eine Hochzeit nicht erlaubt ist, wird unter den restlichen Spielen ein höchstes ermittelt
        	if(mod.equals(modus.HOCHZEIT) && !hochzeit(spielt)) {
        		spielfolge.remove(spielt);
        		hoechstesSpiel(spielfolge);
        	}
        }
        	
        	private void spielErmitteln(ArrayList<modus> spielfolge) {
        		mod = spielfolge.get(spielfolge.size() - 1);
            	for(int i = spielfolge.size() - 2; i >= 0; i--) {
            		mod = model.werSpielt(mod, spielfolge.get(i));
            	}
        	}
        
        /**
         * Führt eine Hochzeit zwischen zwei Spielern durch
         * @throws Exception 
         */
        private boolean hochzeit(int spielt) throws Exception {
    		Karte angebot = spieler.get(spielt).gibKarte();
    		
    		Hochzeit hochzeit = new Hochzeit();
    		if(hochzeit.hochzeitMoeglich(model, spielt, angebot)) {
        		for(int i = 0; i < 4; i++) {
        			weristdran(i);
        			if(i != spielt && spieler.get(i).hochzeit()) {
        				if(hochzeitAnnehmen(i, angebot, hochzeit)) {
        					return true;
        				}
        			} else {
        				continue;
        			}
        		}
    		} else {
    			return false;
    		}

    		return false;
        }
        
        	private boolean hochzeitAnnehmen(int mitspieler, Karte angebot, Hochzeit hochzeit) throws InterruptedException {
        		//Wenn die Hochzeit angenommen wird
				Karte k = spieler.get(mitspieler).gibKarte();
				if(k == null) {
					return false;
				}
				
				//Wenn die Karte kein Trumpf ist
    			if(!hochzeit.istTrumpf(k.gibWert(), k.gibFarbe())) {
    				model.hochzeit(spielt, mitspieler, angebot, k);
    				this.mitspieler = mitspieler;
    				return true;
    			} else {
    				return false;
    			}
        	}
        
        private void kontra() throws Exception {
        	//Sendet den Modus an alle Spieler und empfängt, ob Kontra gegeben wurde
        	spielerSenden();
        	kontraAbfragen();
        	kontraSenden();
        }
        
        	private void kontraAbfragen() throws Exception {
        		for(int i = 0; i < 4; i++) {
        			weristdran(i);
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
        
        private void spiel() throws Exception {
        	try {
        		
	        	//Spielen
	        	//Speichert, wer zuerst auskartet
	        	int start = 0;
	        	for(int i = 0; i < 6; i++) {
	        		for(int j = 0; j < 4; j++) {
	        			//Es ist immer derjenige zuerst mit auskarten dran, der den letzten Stich gewonnen hat
	        			int spielerID = start + j;
	        			spielerID %= 4;
	        			
	        			spielModelSenden(spielerID);
	        			spielModelEmpfangen(spielerID);
	        		}
	        		
	        		//Allen alle Karten noch einmal zeigen
	        		for(int k = 0; k < 4; k++) {
	        			spieler.get(k).update(model);
	        		}
	        		Thread.sleep(wartezeit);
	        		
	        		//einen Stich zuteilen
	        		int sieger = regeln.sieger(model, start);
	        		start = sieger;
	        		model.Stich(sieger);
	        		stichInfo();
	        	} 
        	
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        
        	private void spielModelSenden(int spielerID) throws Exception {
        		weristdran(spielerID);
        		//Übergibt dem Spieler das aktuelle Model und...
    			spieler.get(spielerID).spielen(model);
    			for(int i = 0; i < 4; i++) {
    				if(i != spielerID) {
    					spieler.get(i).update(model);
    				}
    			}
        	}
        	
        	private void spielModelEmpfangen(int spielerID) {
        		//...empfängt das aktualisierte
    			model = spieler.get(spielerID).gibModel();
        	}
        	
        	private void stichInfo() {
        		for(int i = 0; i < 4; i++) {
        			spieler.get(i).stich(model);
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
        	
        	int k = konto.get(0);
        	konto.remove(0);
        	konto.add(k);
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
        	if(mitspieler != 4) {
        		pSpielt += punkte.get(mitspieler);
        	}
        	
        	//Wenn ein Du gespielt wurde
        	String modString = mod.toString();
        	//Die letzten zwei Buchstaben werden verglichen
        	if(modString.substring(modString.length() - 2, modString.length())
        			.toLowerCase()
        			.equals("du")) {
        		if(pSpielt != 120) {
        			//Wenn der Du verloren wurde
        			spielt += 10;
        		}
        	} else 
        	//Ansonsten wird normal verrechnet
        	if(pSpielt <= 60) {
        		//Anzeigen, dass er verloren hat
        		spielt += 10;
        		if(mitspieler != 4) {
        			mitspieler += 10;
        		}
        	}
        	
        	for(int i = 0; i < 4; i++) {
        		try {
        			spieler.get(i).sieger(spielt, mitspieler);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	
        	//Den Spielern Geld abziehen oder hinzufügen
        	ArrayList<Integer> neuesKonto = new ArrayList<Integer>(4);
        	for(int i = 0; i < 4; i++) {
        		neuesKonto.add(0);
        	}
        	//Wenn die Spielenden verloren haben
        	if(spielt > 9) {
        		neuesKonto.set(spielt - 10, konto.get(spielt - 10) - abrechnung(true, pSpielt));
        		if(mitspieler != 4) {
        			neuesKonto.set(mitspieler - 10, konto.get(mitspieler - 10) - abrechnung(true, pSpielt));
        		}
        	} else {
        		//Wenn die Spielenden gewonnen haben
        		neuesKonto.set(spielt, konto.get(spielt) + abrechnung(true, 120 - pSpielt));
        		if(mitspieler != 4) {
        			neuesKonto.set(mitspieler, konto.get(mitspieler) + abrechnung(true, 120 - pSpielt));
        		}
        		
        		//Wenn ein Sauspiel gewonnen wurde, wird der Stock aufgeteilt
        		if(mod.equals(modus.SAUSPIELeichel) 
                		|| mod.equals(modus.SAUSPIELgras)
                		|| mod.equals(modus.SAUSPIELherz)
                		|| mod.equals(modus.SAUSPIELschellen)) {
        			//Konto aktualiesieren
        			konto.set(spielt, neuesKonto.get(spielt));
        			konto.set(mitspieler, neuesKonto.get(mitspieler));
        			//und Stock addieren
        			neuesKonto.set(spielt,  konto.get(spielt) + stock / 2);
        			neuesKonto.set(mitspieler, konto.get(mitspieler) + stock / 2);
        			stock = 0;
                }
        	}
        	for(int i = 0; i < 4; i++) {
        		if(i != spielt && i != spielt - 10 && i != mitspieler && i != mitspieler - 10) {
        			if(spielt > 9) {
        				//Die Spielenden haben verloren
        				neuesKonto.set(i, konto.get(i) + abrechnung(false, pSpielt));
                		//Wenn ein Sauspiel gewonnen wurde, wird der Stock aufgeteilt
                		if(mod.equals(modus.SAUSPIELeichel) 
                        		|| mod.equals(modus.SAUSPIELgras)
                        		|| mod.equals(modus.SAUSPIELherz)
                        		|| mod.equals(modus.SAUSPIELschellen)) {
                			//Konto aktualiesieren
                			konto.set(i, neuesKonto.get(i));
                			//und Stock addieren
                			neuesKonto.set(i,  konto.get(i) + stock / 2);
                			stock = 0;
                        }
        			} else {
        				//Die Spielenden haben gewonnen
        				neuesKonto.set(i, konto.get(i) - abrechnung(false, 120 - pSpielt));
        			}
        		}
        	}
        	
        	konto = neuesKonto;
        }
        
        /**
         * Errechnet den gewonnenen/verlorenen Betrag
         * @return
         */
        private int abrechnung(boolean spieler, int punkteVerlierer) {
        	int diff = 0;
        	
        	//Abhängig von der Spielart
        	switch(mod) {
        	case SAUSPIELeichel:
        	case SAUSPIELgras:
        	case SAUSPIELherz:
        	case SAUSPIELschellen:
        		diff = tarif;
        		break;
        	case HOCHZEIT:
        		diff = tarif;
        		break;
        	case GEIER:
        		diff = (int) (tarif * 2.5);
        		break;
        	case GEIERdu:
        		diff = tarif * 5;
        		break;
        	case WENZ:
        		diff = (int) (tarif * 2.5);
        		break;
        	case WENZdu:
        		diff = tarif * 5;
        		break;
        	case SOLOeichel:
        	case SOLOgras:
        	case SOLOherz:
        	case SOLOschellen:
        		diff = (int) (tarif * 2.5); 
        		break;
        	case SOLOeichelDU:
        	case SOLOgrasDU:
        	case SOLOherzDU:
        	case SOLOschellenDU:
        		diff = tarif * 5;
        		break;
        	case SI:
        		//Solo-Tout mit 6*tarif für die Laufenden
        		diff = 11 * tarif;
        		break;
        	}
        	
        	//geklopft und Kontra mit einbeziehen
        	for(int i = 0; i < 4; i++) {
        		if(geklopft[i]) {
        			diff *= 2;
        		}
        		if(kontra[i]) {
        			diff *= 2;
        		}
        	}
        	
        	if(punkteVerlierer == 0) {
        		diff *= 3;
        	} else {
        		if(punkteVerlierer < 30) {
        			diff *= 2;
        		}
        	}
        	
        	//Bezieht die Laufenden mit ein
        	diff += model.gibLaufende() * tarif;
        	
        	if(spieler) {
        		if(mitspieler != 4) {
        			return diff;
        		} else {
        			//Wenn der Spieler alleine war muss er alle bezahlen / bekommt er den ganzen Gewinn
        			return diff * 3;
        		}
        	} else {
        		return diff;
        	}
        }
        
        /**
         * Füllt den Stock auf
         */
        private void stock() { 
        	stock += (tarif * 4);
        	for(int i = 0; i < 4; i++) {
        		int k = konto.get(i);
        		konto.set(i, k - tarif);
        	}
        }
        
        private void kontoAusgeben() {
        	for(int i = 0; i < 4; i++) {
        		spieler.get(i).konto(konto.get(i), stock);
        	}
        }
        
        private void kontostand() {
        	for(int i = 0; i < 4; i++) {
        		spieler.get(i).rundeZuende(konto.get(i), stock);
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
         * Entfernt einen Spieler und sorgt dafür, dass das Spiel unterbrochen wird
         * @param i
         */
        public synchronized void entferneSpieler(Spieler s) {
        	spieler.remove(s);
        	
        	//Entfernt alle Bots, um Raum für neue Spieler zu schaffen
        	for(int i = spieler.size() - 1; i >= 0; i--) {
        		if(spieler.get(i) instanceof Bot) {
        			spieler.remove(spieler.get(i));
        		}
        	}
        	spielerzahl = 4;
        	
        	ViewTextSetzen();
        	nocheins = true;
        	interrupt();
        }
        
        /**
         * Setzt die Spielerzahl (menschliche Spieler)
         * @param spielerzahl
         * @throws Exception 
         */
        public void setzeSpielerzahl(int spielerzahl) {
        	this.spielerzahl = spielerzahl;
        	//Versucht ein Spiel zu starten
        	new Thread() {
        		public void run() {
        			try {
						starten();
					} catch (Exception e) {
						e.printStackTrace();
					}
        		}
        	}.start();
        }
        
        /**
         * Ändert die Zeit, mit der das Spiel verzögert wird
         * @param wartezeit
         */
        public void setzeWartezeit(int wartezeit) {
        	this.wartezeit = wartezeit;
        }
        
        /**
         * Gibt die Spielerzahl zurück
         * @return
         */
        public int gibSpielerzahl() {
        	return spielerzahl;
        }
        
        /**
         * Ändert den Tarif des Spiels
         * @param tarif
         */
        public synchronized void setzeTarif(int tarif) {
        	this.tarif = tarif;
        	konto = new ArrayList<Integer>();
        	for(int i = 0; i < 4; i++) {
        		konto.add(100*tarif);
        	}
        }
        
        /**
         * Legt die Schwierigkeitsstufe des Spiels fest
         * @param handicap
         */
        public void setzeHandicap(int handicap) {
        	this.handicap = handicap;
        }
        
        /**
         * Aktualisiert die Anzeige der Spieler
         */
        public synchronized void ViewTextSetzen() {
        	graphik.textSetzen(spieler);
        }
        
        /**
         * Liefert die IP des Servers
         */
        public String gibIP(){        	
        	ArrayList<String> addresses = new ArrayList<String>();
        	
        	try {
	        	Enumeration e = NetworkInterface.getNetworkInterfaces();
	        	while(e.hasMoreElements()) {
	        	    NetworkInterface n = (NetworkInterface) e.nextElement();
	        	    Enumeration ee = n.getInetAddresses();
	        	    while (ee.hasMoreElements()) {
	        	        InetAddress i = (InetAddress) ee.nextElement();
	        	        //Filtert nach IPv4 Addressen
	        	        if(!i.isLoopbackAddress() || !i.isLinkLocalAddress()) {
	        	        	if(i instanceof Inet4Address && i.isSiteLocalAddress()) {
	        	        		addresses.add(i.getHostAddress());
	        	        	}
	        	        }
	        	    }
	        	}
	        	
	        	return addresses.get(addresses.size() - 1);
        	} catch(Exception e) {
        		e.printStackTrace();
        		return server.getLocalSocketAddress().toString();
        	}
        }
        
        /**
         * Beendet den Server
         */
		public void beenden() {
        	try {
        		//Speichert die Datenbanken der KI
    			spielauswahl.beenden();
    			graphik.speichern();
        		
        		ArrayList<Spieler> s = (ArrayList<Spieler>) spieler.clone();
        		
        		beenden = true;
				server.close();
				
				for(int i = 0; i < s.size(); i++) {
					s.get(i).abmelden();
				}
				
				this.interrupt();
				this.finalize();
				
				graphik.beenden();
			} catch (Throwable e) {
				e.printStackTrace();
			}
        }
}
