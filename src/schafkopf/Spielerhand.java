package schafkopf;

import java.util.ArrayList;
import lib.Karte;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Spielerhand extends Pane {
	
	// Speichert bei Drag and Drop die Position des ersten Mausklicks
	private double mouseX = 0;
	private double mouseY = 0;
	
	/*
	 * kartenButton und karten halten die Spielerkarten in der selben Reihenfolge vor,
	 * welche dem auf dem Server und im Model entspricht. Da der Nutzer allerdings die
	 * Karten umsortieren kann, ist die Belegungstabelle kartenBelegung nötig.
	 * Der Index in kartenBelegung entspricht der angezeigten Position, der Wert im
	 * Feld entspricht dem entsprechenden Button.
	 * Hat der Spieler also drei Karten, die vom Server als 1, 2, 3 bezeichnet werden
	 * und der Benutzer tauscht die ersten zwei Karten, so sieht die Belegungstabelle
	 * folgendermaßen aus:     [2, 1, 3]
	 */
	private ArrayList<Button> kartenButton;
	private ArrayList<Integer> kartenBelegung;
	private ArrayList<Karte> karten;
	
	/*
	 * Die Karten auf dem Tisch. Die Spieler-Reihenfolge geht im Uhrzeigersinn, wobei
	 * der lokale Spieler die letzte Karte, also Index 3 erhält.
	 */
	private Label[] tisch;
	private Karte[] kartenAufTisch;
	
	private Duration animationsDauer = Duration.millis(150);

	public Spielerhand(int kartenzahl, Node... children) {
		super(children);
		
		karten = new ArrayList<Karte>();
		kartenAufTisch = new Karte[] {null, null, null, null};
		
		this.setBackground(new Background(new BackgroundFill(Color.TAN, null, null)));
		
		// Reagiere auf Resize-Events
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			ordneKarten();
		};
		this.widthProperty().addListener(stageSizeListener);
		this.heightProperty().addListener(stageSizeListener);
		
		//----------------------- Die Karten des Spielers
		
		kartenButton = new ArrayList<Button>();
		kartenBelegung = new ArrayList<Integer>(kartenzahl);
		for(int i = 0; i < kartenzahl; i++) {
			Button btn = new Button();
			btn.setPadding(Insets.EMPTY);
			btn.setMinSize(80, 120);
			btn.setVisible(true);
			//btn.setDisable(true);
			
			btn.setOnMousePressed(e -> {
				mouseX = e.getX();
				mouseY = e.getY();
				btn.toFront();
			});
			btn.setOnMouseDragged(e -> {
				handleDrag(btn, e);
	        });
			btn.setOnMouseReleased(e -> {
				handleMouseRelease(btn, e);
			});
			kartenButton.add(btn);
			setzeKarte(btn, null);
			this.getChildren().add(btn);
			
			kartenBelegung.add(i);
		}
		
		// ---------------------- Der Ablagestapel auf dem Tisch
		
		tisch = new Label[4];
		for(int i = 0; i < tisch.length; i++) {
			tisch[i] = new Label();
			tisch[i].setPadding(Insets.EMPTY);
			tisch[i].setMinSize(80, 120);
			//tisch[i].setDisable(true);
			this.getChildren().add(tisch[i]);
		}
		setzeTisch(new Karte[] {null, null, null, null});
		
		// Ordne Karten an
		ordneKarten();
	}
	
	public void setzeKarten(ArrayList<Karte> karten) {
		this.karten = karten;
		for(int i = 0; i < karten.size(); i++) {
			setzeKarte(kartenButton.get(i), karten.get(i));
		}
		
		for(int i = karten.size(); i < kartenButton.size(); i++) {
			// Entferne den Button aus der Belegungstabelle
			kartenBelegung.remove(kartenBelegung.indexOf(i));
		}
	}
	
	public void setzeTisch(Karte[] karten) {
		for(int i = 0; i < tisch.length; i++) {
			if(karten[i] != null) {
				//Zeigt das zur Karte gehörende Bild
				String url =  "/assets/" +
						karten[i].gibFarbe().toString().toLowerCase() + 
						karten[i].gibWert().toString().toLowerCase() + 
						".jpg";
				
				tisch[i].setGraphic(new ImageView(
		        		new Image(getClass().getResourceAsStream(url),
		        				tisch[i].getWidth(),
		        				tisch[i].getHeight(),
		        				true, true)));
				tisch[i].setVisible(true);
				
				// lässt neue Karten hereischweben, außer es ist die eigene (id = 3)
				if(this.kartenAufTisch[i] == null && i != 3) {
					// Schiebt die Karte aus dem Fenster
					tisch[i].setTranslateX((1 + (i - 1) % 2) * this.getWidth() / 2);
					tisch[i].setTranslateY((0.25 - (2 - i) % 2) * this.getHeight() / 2);
					tisch[i].setRotate(((2 - i) % 2 * 90));
					tisch[i].toFront();
					
					// lasse Karte wieder hereinschweben
					legeKarteAufTisch(i).play();
				}
			} else {
				tisch[i].setVisible(false);
			}
		}
		
		this.kartenAufTisch = karten;
	}
	
	public void reset() {
		// setze alles auf Anfang
		karten.clear();
		kartenBelegung.clear();
		for(int i = 0; i < kartenButton.size(); i++) {
			kartenBelegung.add(i);
			kartenButton.get(i).setVisible(true);
			kartenButton.get(i).setDisable(true);
			setzeKarte(kartenButton.get(i), null);
		}
		for(int i = 0; i < tisch.length; i++) {
			tisch[i].setDisable(true);
			tisch[i].setVisible(false);
		}
		setzeTisch(new Karte[] {null, null, null, null});
		
		ordneKarten();
	}
		
	private void setzeKarte(Button btn, Karte karte) {
		String url = "/assets/rueckseite.jpg";
		if(karte != null) {
			//Zeigt das zur Karte gehörende Bild
			url =  "/assets/" +
					karte.gibFarbe().toString().toLowerCase() + 
					karte.gibWert().toString().toLowerCase() + 
					".jpg";
		}
		
		btn.setGraphic(new ImageView(
        		new Image(getClass().getResourceAsStream(url),
        				btn.getWidth(),
        				btn.getHeight(),
        				true, true)));
	}
	
	private void handleDrag(Button btn, MouseEvent e) {
		btn.setTranslateX(btn.getTranslateX() - mouseX + e.getX());
		btn.setTranslateY(btn.getTranslateY() - mouseY + e.getY());
		/*
		 * alpha = arctan(x / y)
		 */
		double x = btn.getTranslateX() 
					- 0.5 * this.getWidth() + 0.15 * btn.getWidth();
		double y = btn.getTranslateY() 
					- this.getHeight() - (3 - 0.1 * kartenButton.size() / 2) * btn.getHeight();
		btn.setRotate(java.lang.Math.atan(-x / y) * 50); 
		
		// Ermögliche das sortieren der Karten durch verschieben
		if(kartenButton.size() > 1) {
			// Berechne den Index um die Position der Karten rechts und links zu überprüfen
			int index = kartenBelegung.indexOf(kartenButton.indexOf(btn));
			
			if(index == 0) {
				// Karte wurde nach links gezogen
				if(btn.getTranslateX()
								> kartenButton.get(kartenBelegung.get(index + 1)).getTranslateX()) {
					verschiebeKarte(index + 1, index).play();
				}
			} else if(index == kartenBelegung.size() - 1) {
				// Karte wurde nach rechts gezogen
				if(btn.getTranslateX()
								< kartenButton.get(kartenBelegung.get(index - 1)).getTranslateX()) {
					verschiebeKarte(index - 1, index).play();
				}
			} else {
				if(btn.getTranslateX() 
								> kartenButton.get(kartenBelegung.get(index + 1)).getTranslateX()) {
					verschiebeKarte(index + 1, index).play();
				} else if(btn.getTranslateX() 
								< kartenButton.get(kartenBelegung.get(index - 1)).getTranslateX()) {
					verschiebeKarte(index - 1, index).play();
				}
			}
		}
	}
	
	private void handleMouseRelease(Button btn, MouseEvent e) {
		// Beachte, ob Karte auf dem Tisch gelandet ist:
		
		// Schiebe die Karte auf ihren Platz zurück
		platziereKarte(kartenBelegung.indexOf(kartenButton.indexOf(btn))).play();
		// Stelle Fächerung wieder her
		for(int i = 0; i < kartenBelegung.size(); i++) {
			kartenButton.get(kartenBelegung.get(i)).toFront();
		}
	}
	
	/**
	 * Platziert eine Karte auf ihrem Platz auf der Spielerhand.
	 * Der Index bezieht sich auf die Belegungstabelle.
	 * @param index
	 * @return
	 */
	private ParallelTransition platziereKarte(int index) {
		return verschiebeKarte(index, index);
	}
	
	/**
	 * Verschiebt eine Karte in der Spielerhand. Die Indices beziehen sich auf die
	 * Belegungstabelle.
	 * @param von
	 * @param nach
	 */
	private ParallelTransition verschiebeKarte(int von, int nach) {
		// aktualisiere die Belegungstabelle
		if(von != nach) {
			int buffer = kartenBelegung.get(von);
			kartenBelegung.set(von, kartenBelegung.get(nach));
			kartenBelegung.set(nach, buffer);
		}
		
		// Belegung bereits geändert!
		Button btn = kartenButton.get(kartenBelegung.get(nach));
		
		ParallelTransition alleTransitionen = new ParallelTransition();	
		TranslateTransition transition_trans = new TranslateTransition(animationsDauer, btn);
		RotateTransition transition_rot = new RotateTransition(animationsDauer, btn);
		
		/*
		 * Ordne Karten auf einem Bogen mit Radius 10*btn.height am unteren Ende des Panes an.
		 * r^2 = x^2 + y^2 -> y = sqrt(r^2 - x^2)
		 */
		double x = (nach - kartenBelegung.size() / 2.0) * 0.6 * btn.getWidth();
		double y = java.lang.Math.sqrt(
				java.lang.Math.pow(4 * btn.getHeight(), 2) 
				- java.lang.Math.pow(x, 2) );

		transition_trans.setToX(0.5 * this.getWidth() - 0.15 * btn.getWidth() + x);
		
		// Verschiebe die Höhe des Bogens entsprechend der maximalen Kartenzahl
		transition_trans.setToY(this.getHeight() + (3 - 0.1 * kartenButton.size() / 2) * btn.getHeight() - y);
		
		/*
		 * alpha = arctan(x / y)
		 */
		//btn.setRotate(java.lang.Math.atan(x / y) * 50);
		transition_rot.setToAngle(java.lang.Math.atan(x / y) * 50);
		
		alleTransitionen.getChildren().addAll(transition_trans, transition_rot);
		alleTransitionen.setCycleCount(1);
		return alleTransitionen;
	}
	
	/**
	 * Bewegt ein Label zu seinem Platz auf dem Ablagestapel
	 * id gibt die Nummer des Spielers, wobei der lokale Spieler die 3 erhält.
	 * @param id
	 * @return
	 */
	private ParallelTransition legeKarteAufTisch(int id) {
		ParallelTransition alleTransitionen = new ParallelTransition();
		TranslateTransition transition_trans = new TranslateTransition(animationsDauer, tisch[id]);
		RotateTransition transition_rot = new RotateTransition(animationsDauer, tisch[id]);
		
		// -1 0 1 0
		transition_trans.setToX((this.getWidth() - tisch[id].getWidth()) / 2 
				+ (id - 1) % 2 * tisch[id].getWidth() / 2);
		// 0 1 0 -1
		transition_trans.setToY(this.getHeight() / 4 
				- (2 - id) % 2 * tisch[id].getHeight() / 4);
		transition_rot.setToAngle(-85 + 95 * id);
		
		alleTransitionen.getChildren().addAll(transition_trans, transition_rot);
		
		return alleTransitionen;
	}
	
	/**
	 * Ordnet alle Karten in der Spielerhand an.
	 */
	private void ordneKarten() {	
		// Die Karten sollen sich gleichzeitig zu ihren Plätzen bewegen
		ParallelTransition alleTransitionen = new ParallelTransition();
		
		for(int i = 0; i < kartenBelegung.size(); i++) {		
			alleTransitionen.getChildren().add(platziereKarte(i));
		}
		
		/*
		 *  Die abgelegten Karten:
		 *  Die Oberseite des Stapels soll etwa 1/4 der Fensterhöhe vom oberen
		 *  Rand entfernt sein.
		 */
		for(int i = 0; i < tisch.length; i++) {
			alleTransitionen.getChildren().addAll(legeKarteAufTisch(i));
		}
		
		alleTransitionen.setCycleCount(1);
		alleTransitionen.play();
	}

}
