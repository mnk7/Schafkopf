package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Netzwerk extends lib.Netzwerk {
	
	private ServerSocket server;
	
	private Socket client;
	
	public Netzwerk(int port) throws Exception {
		
		//Verbindungsinformationen
		this.port = port;
		
		try {
			//Erstellen eines Servers -> Input
			server = new ServerSocket(port);
			//Erstellen eines Clients -> Output
			client = server.accept();
			
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
	System.out.println("verbunden");
			
		} catch (IOException e) {
			throw new Exception("Verbindung mit Client fehlgeschlagen");
		}
	}
	
	/**
	 * Gibt die IP des Clients zurück
	 * @return IP
	 */
	public String gibIP() {
		return client.getInetAddress().getHostAddress();
	}
}

