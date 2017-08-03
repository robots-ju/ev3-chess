package ch.wisteca.robot;

/**
 * Interface permettant d'exécuter une action sans arrêt.
 * @author Wisteca
 */
public interface MainThreadListener {
	
	/**
	 * Méthode appelée sans arrêt par le thread main.
	 */
	public void onCall();
	
}
