package graphik;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import lib.Karte;

public class Spieler extends JPanel {
	//Buttons mit Bildern der Karten
	private ArrayList<KartenButton> karten;
	
	//speichert die gespielte Karte
	private Karte gespielt;
	
	public Spieler() {
		super();
		
		this.setLayout(null);
		this.setSize(420, 100);
		
		karten = new ArrayList<KartenButton>();		
		for(int i = 0; i < 6; i++) {
			karten.add(new KartenButton(null));
			this.add(karten.get(i));
			karten.get(i).setLocation(70*i, 0);
			karten.get(i).setVisible(true);
			
			karten.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					karteSpielen(evt);
				}
			});
		}
		
		//Karten abschalten
		aktiviert(false);
	}
	
	/**
	 * Aktualisiert die Karten des Spielers
	 * @param model
	 */
	public void update(ArrayList<Karte> model) {
		ArrayList<Karte> spielerkarten = model;
		
		for(int i = 0; i < spielerkarten.size(); i++) {
			//neues Bild zuteilen
			karten.get(i).setzeKarte(spielerkarten.get(i));
		}
		
		//schon gespielte Karten werden abgeschaltet
		for(int i = 5; i >= spielerkarten.size(); i--) {
			karten.get(i).setVisible(false);
			karten.get(i).setEnabled(false);
		}
	}	
	
	private void karteSpielen(ActionEvent evt) {
		//Das Signal kommt auf jeden Fall von einem KartenButton
		KartenButton k = (KartenButton) evt.getSource();
		
		//Speichert die gewählte Karte und lässt keine neue Auswahl zu
		gespielt = k.gibKarte();
		aktiviert(false);
	}
	
	public Karte spiel() {
		gespielt = null;
		aktiviert(true);
		while(gespielt == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return gespielt;
	}
	
	private void aktiviert(boolean an) {
		for(int i = 0; i < karten.size(); i++) {
			karten.get(i).setEnabled(an);
		}
	}	
}
