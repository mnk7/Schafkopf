package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Graphik extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Graphik inst = new Graphik();
			}
		});
	}
	
	private JButton start;
	private JButton end;
	private JLabel [] PlayerLabel;
	
	private Server server;
	private Graphik g = this;
	
	private String logo = "./logo.gif";
	
	public Graphik(){
		super();
		
		Graphik.class.getResource(logo);
		//Icon der Anwendung setzen
		ImageIcon icon = new ImageIcon(logo);
		this.setIconImage(icon.getImage());
				
		PlayerLabel = new JLabel [4];
		
		try {
			initGUI();
		} catch(Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Fehler beim Programmstart");
			e.printStackTrace();
		}	
	}
	
	public void initGUI() {
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
		
		for(int i = 0;i < 4;i++){
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
				if(server == null) {
					server = new Server(g);
					server.start();
					
					clear();
					PlayerLabel[0].setText("Server gestartet");
					start.setText("Läuft...");
				}
			}
		});
		start.setVisible(true);
		
		end = new JButton();
		getContentPane().add(end);
		end.setBounds(getWidth()/2 + 5, 10, getWidth()/2 - 15, 30);
		end.setText("Server stoppen");
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(server != null) {
					server.beenden();
					server = null;
					
					clear();
					PlayerLabel[0].setText("Server beendet");
					start.setText("Server starten");
				}
			}
		});
		end.setVisible(true);
		
		repaint();
	}
	
	public synchronized void textSetzen(ArrayList<Spieler> s) { 
		clear();
		ArrayList <Spieler> spieler = s;		
		
		for(int i = 0; i < 4; i++){
			String aufschrift;
			try {
				aufschrift = spieler.get(i).gibName();
				aufschrift += " - ";
				aufschrift += spieler.get(i).gibIP();
			} catch(Exception e) {
				aufschrift = "";
			}
			PlayerLabel[i].setText(aufschrift);
		}
	}
	
	public synchronized void clear() {
		for(int i = 0; i < 4; i++) {
			PlayerLabel[i].setText("");
		}
	}
}

