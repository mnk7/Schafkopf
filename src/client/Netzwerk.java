package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Netzwerk extends lib.Netzwerk{
	
	public Netzwerk(int spielerID, String ip) throws IOException{
		super();
		
		//ID des Spielers
		this.spielerID = spielerID;
		
		//Verbindungsinformationen
		this.ip = ip;
		
		//Verbindung mit dem Server
		Socket client = new Socket(ip, port);
			
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
	}
}
