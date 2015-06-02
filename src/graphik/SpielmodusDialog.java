package graphik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import lib.Karte.farbe;
import lib.Model.modus;

public class SpielmodusDialog extends JPanel {
	
	private JButton[] spiele;
	private JCheckBox tout;
	private ButtonGroup farbwahl;
	private JRadioButton[] farben;
	
	private modus rueckgabe;
	
	public SpielmodusDialog() {
		super(true);

		this.setLayout(null);
		this.setOpaque(false);          
		
		spiele = new JButton[7];
		
		for(int i = 0; i < spiele.length; i++) {
			spiele[i] = new JButton();
			this.add(spiele[i]);
			spiele[i].setVisible(true);
			spiele[i].setBounds(0, i*40, 150, 30);
			spiele[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					rueckgabe = auswahl(evt);
				}
			});
		}
		
		spiele[0].setText("Sauspiel");
		spiele[1].setText("Hochzeit");
		spiele[2].setText("Geier");
		spiele[3].setText("Wenz");
		spiele[4].setText("Solo");
		spiele[5].setText("Sie");
		
		spiele[6].setText("Nichts");
		
		tout = new JCheckBox();
		this.add(tout);
		tout.setBounds(200, 0, 100, 30);
		//Tout oder nicht Tout? Das ist hier die Frage!
		tout.setText("Tout ?");
		tout.setForeground(Color.white);
		tout.setOpaque(false);
		tout.setVisible(true);
		
		farbwahl = new ButtonGroup();
		
		farben = new JRadioButton[4];
		for(int i = 0; i < 4; i++) {
			farben[i] = new JRadioButton();
			this.add(farben[i]);
			farbwahl.add(farben[i]);
			farben[i].setBounds(200, 50 * (i + 1), 100, 30);
			farben[i].setForeground(Color.white);
			farben[i].setOpaque(false);
			farben[i].setVisible(true);
		}
		
		farben[0].setText("Eichel");
		farben[1].setText("Gras");
		farben[2].setText("Herz");
		farben[3].setText("Schellen");
	}

	/**
	 * Gibt den ausgewählten Modus zurück
	 * @return Modus
	 */
	public modus modusWahl() {
		//Wartet solange, bis ein Wert ausgewählt wurde
		while(rueckgabe == null) {
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
		return rueckgabe;
	}
	
	private modus auswahl(ActionEvent evt) {
		JButton b = (JButton) evt.getSource();
		String mod = b.getText();
		
		switch(mod) {
		case "Hochzeit":
			return modus.HOCHZEIT;
		case "Sauspiel":
			if(farbwahl.isSelected(farben[0].getModel()))
				return modus.SAUSPIELeichel;
			if(farbwahl.isSelected(farben[1].getModel()))
				return modus.SAUSPIELgras;
			if(farbwahl.isSelected(farben[2].getModel()))
				return modus.SAUSPIELherz;
			if(farbwahl.isSelected(farben[3].getModel()))
				return modus.SAUSPIELschellen;
			JOptionPane.showMessageDialog(this, "Wählen sie eine Farbe");
			return null;
		case "Geier":
			if(tout.isSelected())
				return modus.GEIERdu;
			else
				return modus.GEIER;
		case "Wenz":
			if(tout.isSelected())
				return modus.WENZdu;
			else
				return modus.WENZ;
		case "Solo":
			if(farbwahl.isSelected(farben[0].getModel())) {
				if(tout.isSelected())
					return modus.SOLOeichelDU;
				return modus.SOLOeichel;
			}
			if(farbwahl.isSelected(farben[1].getModel())) {
				if(tout.isSelected())
					return modus.SOLOgrasDU;
				return modus.SOLOgras;
			}
			if(farbwahl.isSelected(farben[2].getModel())) {
				if(tout.isSelected())
					return modus.SOLOherzDU;
				return modus.SOLOherz;
			}
			if(farbwahl.isSelected(farben[3].getModel())) {
				if(tout.isSelected())
					return modus.SOLOschellenDU;
				return modus.SOLOschellen;
			}
			JOptionPane.showMessageDialog(this, "Bitte wählen sie eine Farbe");
			return null;
		case "Sie":
			return modus.SI;
		case "Nichts":
			return modus.NICHTS;
		}
		
		return null;
	}

	/**
	 * Gibt die Farbe eines Sauspiels zurück
	 * @param m
	 * @return
	 */
	public farbe farbe(modus m) {
		if(m.equals(modus.SAUSPIELeichel))
			return farbe.EICHEL;
		if(m.equals(modus.SAUSPIELgras))
			return farbe.GRAS;
		if(m.equals(modus.SAUSPIELherz))
			return farbe.HERZ;
		if(m.equals(modus.SAUSPIELschellen))
			return farbe.SCHELLEN;
		
		return null;
	}
	
	/**
	 * Wandelt einen Modus in gesprochenes Wort um
	 * @param m
	 */
	public String modusZuSprache(modus m) {
		switch(m) {
		case HOCHZEIT:
			return "eine Hochzeit";
		case SAUSPIELeichel:
			return "ein Sauspiel auf die Eichel";
		case SAUSPIELgras:
			return "ein Sauspiel auf die Gras";
		case SAUSPIELherz:
			return "ein Sauspiel auf die Herz";
		case SAUSPIELschellen:
			return "ein Sauspiel auf die Schellen";
		case GEIER:
			return "ein Geier";
		case GEIERdu:
			return "ein Geier-Tout";
		case WENZ:
			return "ein Wenz";
		case WENZdu:
			return "ein Wenz-Tout";
		case SOLOeichel:
			return "ein Eichel-Solo";
		case SOLOeichelDU:
			return "ein Eichel-Solo-Tout";
		case SOLOgras:
			return "ein Gras-Solo";
		case SOLOgrasDU:
			return "ein Gras-Solo-Tout";
		case SOLOherz:
			return "ein Herz-Solo";
		case SOLOherzDU:
			return "ein Herz-Solo-Tout";
		case SOLOschellen:
			return "ein Schellen-Solo";
		case SOLOschellenDU:
			return "ein Schellen-Solo-Tout";
		case SI:
			return "ein Sie";
		}
		
		return "Nichts";
	}
	
	public void reset() {
		rueckgabe = null;
	}
}
