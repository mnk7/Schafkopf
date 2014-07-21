package tests;

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
						Client c = new Client("127.0.0.1", "M");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
<<<<<<< HEAD
			 
=======
			
>>>>>>> f25c3e51b6e42fb9fccc95e99aefda1effc81689
			t[i].start();
		}

	}

}
