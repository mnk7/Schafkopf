package graphik;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import lib.Karte;

public class Spieler extends JPanel {
	//Buttons mit Bildern der Karten
	private ArrayList<KartenButton> karten;
	
	//Zeigt an, ob eine Karte für eine Hochzeit ausgesucht wird oder nicht
	private Karte gespielt;
	
	public Spieler(int breite, int hoehe) {
		super(true);
		
		gespielt = null;
		
		this.setLayout(null);
		this.setOpaque(false);
		this.setSize(breite, hoehe);
		
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
		if(model == null) {
			model = new ArrayList<Karte>();
		}
		
		reset();
		
		//Errechnet, um wie viele Pixel die Karten verschoben werden
		int diff = 6 - spielerkarten.size();
		diff *= 70/2;
		
		for(int i = 0; i < spielerkarten.size(); i++) {
			//neues Bild zuteilen
			karten.get(i).setzeKarte(spielerkarten.get(i));
			karten.get(i).setLocation(70 * i + diff, 0);
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
		gespielt = k.gibKarte();
		aktiviert(false);
	}
	
	/**
	 * Gibt eine ausgewählte Karte zurück
	 * @return gespielt
	 */
	public Karte spiel() {
		aktiviert(true);
		while(gespielt == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Karte g = gespielt;
		gespielt = null;
		return g;
	}
	
	private void aktiviert(boolean an) {
		for(int i = 0; i < karten.size(); i++) {
			karten.get(i).setEnabled(an);
		}
	}	
	
	private void reset() {
		for(int i = 0; i < karten.size(); i++) {
			karten.get(i).setVisible(false);
		}
	}
}
