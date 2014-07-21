package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import lib.Karte;

public class Netzwerk extends lib.Netzwerk {
	
	private ServerSocket server;
	
	private Socket client;
	
	public Netzwerk(Socket client) throws Exception {
		try {
			//Erstellen eines Clients -> Output
			this.client = client;
			
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch(Exception e) {
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

	public Karte getKarte() {
		Karte karte;
		karte = new Karte(Karte.farbe.valueOf(einlesen()), Karte.wert.valueOf(einlesen()));
		return karte;
	}
}

