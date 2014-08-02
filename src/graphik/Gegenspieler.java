package graphik;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gegenspieler extends JPanel {
	
	//Karten des Spielers
	private Spielerhand karten;
	//Nachrichten und Meldungen
	private Meldungen meldungen;
	
	public Gegenspieler(String name) {
		super();
		
		karten = new Spielerhand();
		
		//Erstellt eine neue Nachrichtenanzeige mit 4 Ausgaben
		meldungen = new Meldungen(4);
		
		

	}

}
