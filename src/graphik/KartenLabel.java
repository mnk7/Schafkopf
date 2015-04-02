package graphik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;

import lib.Karte;

public class KartenLabel extends JLabel {
	
	//Speichert den Pfad zum angezeigten bild
	private String bild;
	//Speichert das Bild
	private BufferedImage icon;
	
	//Speichert Höhe und Weite des Panels
	private int width = 80;
	private int height = 120;
	
	/**
	 * Zeigt eine Karte an, die Karte wird immer kleinstmöglich angezeigt
	 * @param bild
	 * @param width
	 * @param height
	 */
	public KartenLabel(Karte bild, int width, int height) {
		super();
		
		this.setLayout(null);
		this.setSize(this.width, this.height);
		this.setBackground(new Color(0,0,0,0));
		
		setBild(bild);
		
		KartenLabel.class.getResource(this.bild);
		
		//Setzt das Bild der Karte
		setBild(bild);
	}
	
	/**
	 * Legt ein Bild auf die Karte
	 * Wird null übergeben, wird das Bild rueckseite.jpg angezeigt
	 * @param karte
	 */
	public void setBild(Karte karte) {
		if(karte == null) {
			bild =  "graphik/karten/rueckseite.jpg";
		} else {
			//Zeigt das zur Karte gehörende Bild
			bild =  "graphik/karten/" +
					karte.gibFarbe().toString().toLowerCase() + 
					karte.gibWert().toString().toLowerCase() + 
					".jpg";
		}
		
		this.setIcon(new ImageIcon(getClass().getClassLoader().getResource(bild)));
		//this.setIcon(new ImageIcon(bild));
		this.setVisible(true);
	}
	
	/**
	 * Passt die Größe des Bildes auf eine veränderte Fenstergröße an
	 * @param width
	 * @param height
	 */	
	/**public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(2, 2);
		super.paintComponent(g2);
	}**/
}
