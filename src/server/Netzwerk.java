package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Netzwerk extends lib.Netzwerk {
	
	private Socket client;
	
	public Netzwerk(Socket client) throws Exception {
		super();
		
		try {			
			//Erstellen eines Clients -> Output
			this.client = client;
			
			out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
		} catch(Exception e) {
			//Verbindung beenden
			client.close();
			throw e;
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

