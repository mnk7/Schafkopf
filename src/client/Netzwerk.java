package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Netzwerk extends lib.Netzwerk{
	
	public static final int PORT = 15555;
	
	public Netzwerk(int spielerID, String ip) {
		super();
		
		//ID des Spielers
		this.spielerID = spielerID;
		
		//Verbindungsinformationen
		this.ip = ip;
		this.port = PORT;
		
		try {
			//Verbindung mit dem Server
			Socket client = new Socket(ip, port);
			
			out = new PrintWriter(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
