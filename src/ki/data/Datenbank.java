package ki.data;

import java.io.File;

public class Datenbank {
	
	File file;
	
	public Datenbank(String data) throws Exception {
		if(data.endsWith(".dt")) {
			file = new File(data);
		} else {
			throw new Exception("Datenbank wurde nicht passende Datei übergeben");
		}
	}
	
	/**
	 * Liest Datenbank aus .dt Datei
	 */
	public void lesen() {
		
	}
	
	/**
	 * Speichert die Datenbank
	 */
	public void speichern() {
		
	}
}
