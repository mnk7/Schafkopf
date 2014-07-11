package server;

public class Server {
        
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
