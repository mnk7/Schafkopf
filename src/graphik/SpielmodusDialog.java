package graphik;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import lib.Karte.farbe;
import lib.Model.modus;

public class SpielmodusDialog extends JFrame {
	
	private JButton[] spiele;
	private JCheckBox tout;
	private ButtonGroup farbwahl;
	private JRadioButton[] farben;
	
	private modus rueckgabe;
	
	public SpielmodusDialog() {
		super();
		
		this.setVisible(true);
		this.setLayout(null);
		
		spiele = new JButton[7];
		
		for(int i = 0; i < spiele.length; i++) {
			spiele[i] = new JButton();
			getContentPane().add(spiele[i]);
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
		spiele[5].setText("Si");
		
		spiele[6].setText("Nichts");
		
		tout = new JCheckBox();
		getContentPane().add(tout);
		tout.setBounds(200, 0, 100, 30);
		//Tout oder nicht Tout? Das ist hier die Frage!
		tout.setText("Tout ?");
		tout.setVisible(true);
		
		farbwahl = new ButtonGroup();
		
		farben = new JRadioButton[4];
		for(int i = 0; i < 4; i++) {
			farben[i] = new JRadioButton();
			getContentPane().add(farben[i]);
			farbwahl.add(farben[i]);
			farben[i].setVisible(true);
			farben[i].setBounds(200, 50 * (i + 1), 100, 30);
		}
		
		farben[0].setText("Eichel");
		farben[1].setText("Gras");
		farben[2].setText("Herz");
		farben[3].setText("Schellen");
		
		this.setSize(300, 300);
	}

	/**
	 * Gibt den ausgewählten Modus zurück
	 * @return Modus
	 */
	public modus modusWahl() {
		//Wartet solange, bis ein Wert ausgewählt wurde
		while(rueckgabe == null) {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
		case "SI":
			return modus.SI;
		case "Nichts":
			return null;
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

}
