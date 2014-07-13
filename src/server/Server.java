package server;

import lib.Model;

public class Server {
	
		//Speichert die verfügbaren Ports des Servers
		private int[] ports = {
				55555,
				55556,
				55557,
				55558
		};
		
		//Speichert die Spielerzahl, nach jedem Spiel veränderbar
		private int spielerzahl;
		
		//Speichert den Spielstand
		private Model model;
        
        //hält alle 4 Spieler, ob Bot oder Mensch
        private Spieler[] spieler;
        
        //hält eine Verbindung zu einem Spieler, nötig um Spieler zu erstellen
        private Netzwerk netzwerk;
        
        
        /**
         * Erstellt einen neuen Server
         **/
        public Server() {
              
              spieler = new Spieler[4];
              
              
        }

}
