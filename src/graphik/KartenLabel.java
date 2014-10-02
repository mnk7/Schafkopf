package graphik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	private int width = 1;
	private int height = 1;
	
	/**
	 * Zeigt eine Karte an, die Karte wird immer kleinstmöglich angezeigt
	 * @param bild
	 * @param width
	 * @param height
	 */
	public KartenLabel(Karte bild, int width, int height) {
		super();
		
		this.setLayout(null);
		
		//Anzeige der Begrenzungen des Panels
		//[DEBUG]
		//this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Setzt das Bild der Karte
		setBild(bild);
		try {
			//Liest das Bild ein
			KartenLabel.class.getResource(this.bild);
			icon = ImageIO.read(new File(this.bild));
			
		} catch (IOException e) {
			System.out.println("Bild wurde nicht gefunden");
			e.printStackTrace();
		}
		
		//Division durch null vermeiden
		/**float quotient = width / (height + 1);
		if(quotient < 0.6) {
			this.width = width;
			this.height = this.width * 6/10;
		} else {
			this.height = height;
			this.width = this.height * 6/10;
		}
		
		this.setSize(this.width, this.height);
		**/
	}
	
	/**
	 * Legt ein Bild auf die Karte
	 * Wird null übergeben, wird das Bild rueckseite.jpg angezeigt
	 * @param karte
	 */
	public void setBild(Karte karte) {
		if(karte == null) {
			bild =  "karten/rueckseite.jpg";
		} else {
			//Zeigt das zur Karte gehörende Bild
			bild =  "karten/" + 
					karte.gibFarbe().toString().toLowerCase() + 
					karte.gibWert().toString().toLowerCase() + 
					".jpg";
		}
	}
	
	/**
	 * Passt die Größe des Bildes auf eine veränderte Fenstergröße an
	 * @param width
	 * @param height
	 */	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//immer die kleinstmögliche Anzeige wird gewählt
		Dimension windowSize = this.getSize();
		//Divison durch null vermeiden
		/**float quotient = width / (height + 1);
		if(quotient < 0.6) {
			this.width = (int) windowSize.getWidth();
			this.height = this.width * 6/10;
		} else {
			this.height = (int) windowSize.getHeight();
			this.width = this.height * 6/10;
		}
		
		this.width = (int) windowSize.getWidth();
		this.height = (int) windowSize.getHeight();
		
		Image img = icon.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
		
		//this.setIcon(new ImageIcon(bild));
		g.drawImage(img, 0, this.getInsets().top, this);
		**/
		this.setSize(60, 100);
		
		this.setIcon(new ImageIcon(bild));
	}
}
