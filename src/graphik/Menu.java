package graphik;

public interface Menu {
	
	/**
	 * Übergibt dem Client eine neue Graphik
	 * @return View
	 */
	public View gibGraphik();
	
	/**
	 * Beendet ein Spiel
	 */
	public void beenden();

}
