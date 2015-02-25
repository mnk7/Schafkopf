package graphik;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Model;

public class LetzterStich extends JPanel {
	
	private KartenLabel[] karten;
	private JLabel[] namen;
	private JButton OK;
	
	private Model model;
	
	public LetzterStich() {
		super();
		
		this.setLayout(null);
		this.setSize(4*80 + 20, 180);
		this.setBackground(new Color(0, 0, 0, 0));
		
		karten = new KartenLabel[4];
		namen = new JLabel[4];
		for(int i = 0; i < 4; i++) {
			karten[i] = new KartenLabel(null, 80, 120);
			this.add(karten[i]);
			karten[i].setLocation(10 + 80*i, 30);
			karten[i].setVisible(true);
			
			namen[i] = new JLabel();
			this.add(namen[i]);
			namen[i].setBounds(10 + 80*i, 10, 80, 20);
			namen[i].setForeground(Color.white);
			namen[i].setVisible(true);
		}
		
		
		OK = new JButton();
		this.add(OK);
		OK.setBounds(10 + 3*80, 160, 80, 20);
		OK.setText("OK");
		OK.setVisible(true);
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				unsichtbar();
			}
		});
		
		model = new Model();
	}
	
	/**
	 * Aktualisiert die Anzeige
	 * @param model
	 */
	public void setzeModel(Model model, String[] spieler) {
		this.model = model;
		
		for(int i = 0; i < 4; i++) {
			namen[i].setText(spieler[i]);
			karten[i].setBild(model.gibLetztenStich()[i]);
		}
	}
	
	private void unsichtbar() {
		this.setVisible(false);
	}

}
