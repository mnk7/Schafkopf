package tests;

import graphik.MenuGUI;
import client.Client;

public class Clients {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread[] t = new Thread[4];
		for(int i = 0; i < 4; i++) {
			t[i] = new Thread() {
				public void run() {
					try {
						MenuGUI m = new MenuGUI();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			 
			t[i].start();
		}

	}

}
