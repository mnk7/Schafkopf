/**
 * Klasse, die das lib.Model enthält und im Client die Rolle des Models im
 * MVC übernimmt.
 */

package client;

import java.util.ArrayList;

import lib.Model;

public class ModelMVC{
	
	//Enthält die Beobachter
	private ArrayList<View> beobachter;
	
	//Ein Model, wie es im Server verwendet wird
	private Model model;
	
	
	public ModelMVC(Model model) {
		beobachter = new ArrayList<View>();
		this.model = model;
	}
	
	public ModelMVC() {
		this(new Model());
	}
	
	/**
	 * Ändert die Spieldaten
	 * @param model
	 * @throws Exception 
	 */
	public void setzeModel(Model model) throws Exception {
		this.model = model;
		update();
	}
	
	/**
	 * Gibt die Spieldaten
	 * @return model
	 */
	public Model gibModel() {
		return model;
	}
	
	/**
	 * Sendet ein aktualisiertes Model an alle Beobachter
	 * @throws Exception 
	 */
	public void update() throws Exception {
		for(int i = 0; i < beobachter.size(); i++) {
			beobachter.get(i).update(this);
		}
	}
	
	/**
	 * Fügt einen Beobachter hinzu
	 * @param view
	 */
	public void addBeobachter(View view) {
		beobachter.add(view);
	}
	
	/**
	 * Entfernt einen Beobachter
	 * @param view
	 */
	public void removeBeobachter(View view) {
		beobachter.remove(view);
	}

}

