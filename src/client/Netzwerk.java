package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Netzwerk extends lib.Netzwerk{
	
	public Netzwerk(int spielerID, String ip, int port) {
		
		//ID des Spielers
		this.spielerID = spielerID;
		
		//Verbindungsinformationen
		this.ip = ip;
		this.port = port;
		
		try {
			SocketAddress address = new InetSocketAddress(ip, port);
			//Erstellen eines Clients -> Output
			Socket client = new Socket();
			//Verbinden mit dem Server
			client.connect(address);
			
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
