package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Netzwerk extends lib.Netzwerk{
	
	public Netzwerk(InetAddress address, int port) {
		
		//Verbindungsinformationen
		this.address = address;
		this.port = port;
		
		try {
			//Erstellen eines Servers -> Input
			ServerSocket server = new ServerSocket(port);
			//Erstellen eines Clients -> Output
			Socket client = new Socket(address, port);
			
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

