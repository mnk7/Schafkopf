package graphik;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import regeln.Controll;
import lib.Karte;
import lib.Model;  
import lib.Model.modus;
import client.ModelMVC;
import client.View;

public class Graphik extends JFrame implements View
{	
	private JTextField spieler1;  //Name Spieler 1
	private JTextField spieler2;  //Name Spieler 2
	private JTextField spieler3;  //Name Spieler 3
	private JTextField spieler4;  //Name Spieler 4
	private JLabel score1;  //Spielstand Spieler1
	private JLabel score2;  //Spielstand Spieler2
	private JLabel score3;  //Spielstand Spieler3
	private JLabel score4;  //Spielstand Spieler4
	private JLabel hintergrund; //Hintergrund (optional)
	private JLabel anzeige;  //zeigt aktuelles Spiel an
	
	//Enthält die Controll -> kontrolliert einen Spielzug
	private Controll controll;
	
	//Das Model
	private ModelMVC model;
	
	public Graphik() {
		super();
		this.setSize(500, 500);
		this.initGui();
		
	}
	
	public static void main(String[] args) {
		Graphik inst = new Graphik();
	}
	
	
	/**
	 * aktualisiert das Model
	 */
	public void update(ModelMVC model)
	{
		this.model = model;		
	}
	
	/**
	 * Spielzug des Spielers durchführen
	 */
	public void spiel() {
		
	}
	
	/**
	 * Ändert den Modus des Spiels und ändert die Controll
	 * @param mod
	 */
	public void setzeModus(modus mod) {
		
	}
	
	/**
	 * Fragt den Nutzer, ob er etwas spielen will und gibt den Spielmodus zurück
	 * @param model
	 * @return
	 */
	public modus spielstDu() {
		return null;
	}
	
	/**
	 * Gibt zurück, ob der Spieler klopft
	 * @return
	 */
	public boolean klopfstDu() {
		return false;
	}
	
	/**
	 * Zeigt den/die Sieger eines Spiels an
	 * Wenn s2 4 ist, so gibt es keinen zweiten Sieger,
	 * ist s1/s2 zwischen 10 und 13 (11 - 10 = 1 -> der zweite Spieler hat verloren), 
	 * so hat jemand sein Spiel verloren
	 * @param s1
	 * @param s2
	 */
	public void sieger(int s1, int s2) {
		
	}

	/**
	 * Gibt "JA" zurück, wenn der Spieler heiraten will
	 * @return
	 */
	public String hochzeit() {
		return null;
	}

	/**
	 * Gibt die Karte zurück, die der Spieler bei der Hochzeit tauschen will
	 * Wird aufgerufen, wenn jmd. eine Hochzeit anbietet und wenn eine angenommen
	 * wird
	 * @return
	 */
	public Karte hochzeitKarte() {
		return null;
	}

	/**
	 * Empfängt, wer spielt 
	 * @param spielt
	 */
	public void spielt(int spielt) {
		
	}
	
	public void initGui()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SCHOAFKOPF-APP");
		this.setResizable(false);
		spieler1 = new JTextField();
		spieler1.setBounds(200, 200, 100, 50);
		spieler2 = new JTextField();
		spieler2.setBounds(600, 600, 100, 50);
		spieler3 = new JTextField();
		spieler3.setBounds(200, 800, 100, 50);
		spieler4 = new JTextField();		
		spieler4.setBounds(0, 600, 100, 50);
		
	}
	

}

