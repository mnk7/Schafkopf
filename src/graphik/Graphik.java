package graphik;

import client.ModelMVC;
import client.View;

public class Graphik implements View extends JFrame
{
	public JButton start;     //Startknopf
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
	
	public Graphik
	{
		start = new JButton();
		spieler1 = new JTextField();
		
	}
	}
	
	
	
	
	@Override
	public void update(ModelMVC model)
	{
		// TODO Auto-generated method stub
		
	}
	
	
	

}
