/**
 * Klasse, die das lib.Model enthält und im Client die Rolle des Models im
 * MVC übernimmt.
 */

package client;

import java.util.ArrayList;

import lib.Model;

public class ModelMVC extends Model{
	
	//Enthält die Beobachter
	private ArrayList<View> beobachter;
	
	
	public ModelMVC() {
		beobachter = new ArrayList<View>();
	}
	
	/**
	 * Sendet ein aktualisiertes Model an alle Beobachter
	 */
	public void update() {
		for(int i = 0; i < beobachter.size(); i++) {
			beobachter.get(i).update(this);
		}
	}
	
	public void addBeobachter(View view) {
		beobachter.add(view);
	}
	
	public void removeBeobachter(View view) {
		beobachter.remove(view);
	}

}
