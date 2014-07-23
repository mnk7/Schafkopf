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
	
	public Netzwerk(int spielerID, String ip) {
		
		//ID des Spielers
		this.spielerID = spielerID;
		
		//Verbindungsinformationen
		this.ip = ip;
		this.port = 15555;
		
		try {
			//Verbindung mit dem Server
			Socket client = new Socket(ip, port);
			
			out = new BufferedWriter(new PrintWriter(client.getOutputStream(), true));
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
