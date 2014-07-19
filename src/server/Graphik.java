package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class Graphik extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graphik inst = new Graphik();
	}
	
	private JButton start;
	private JButton end;
	private JLabel [] PlayerLabel;
	private Server server;
	
	public Graphik(){
		super();
		PlayerLabel = new JLabel [4];
		
		try {
			initGUI();
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler beim Programmstart");
			e.printStackTrace();
		}	
	}
	
	public void initGUI(){
		this.setSize(330, 240);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SCHOAFKOPF-SERVER");
		this.setResizable(false);
		
		this.setLayout(null);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		for(int i=0;i<4;i++){
			PlayerLabel [i] = new JLabel();
			getContentPane().add(PlayerLabel [i]);
			PlayerLabel [i].setText("");
			PlayerLabel [i].setBounds(10, 50 + i*40, getWidth()-20, 30);
			PlayerLabel [i].setVisible(true);
		}
		
		start = new JButton();
		getContentPane().add(start);
		start.setBounds(10, 10, getWidth()/2 - 15, 30);
		start.setText("Server starten");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				server = new Server();
				Thread t = new Thread(){
					public void run(){
						while(true){
							textSetzen();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};
			t.start();

			}
		});
		start.setVisible(true);
		
		end = new JButton();
		getContentPane().add(end);
		end.setBounds(getWidth()/2 + 5, 10, getWidth()/2 - 15, 30);
		end.setText("Server stoppen");
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				server = null;
			}
		});
		end.setVisible(true);
		
		repaint();
	}
	
	private void textSetzen(){
		ArrayList <Spieler> spieler = new ArrayList <Spieler> ();		
		
		try{
			spieler = server.gibSpieler();
			for(int i=0; i<4; i++){
				String aufschrift;
				aufschrift = spieler.get(i).gibName();
				aufschrift += " - ";
				aufschrift += spieler.get(i).gibIP();
			}
		}
		catch(Exception e) {
			// e.printStackTrace();
		}
		

	}
}

