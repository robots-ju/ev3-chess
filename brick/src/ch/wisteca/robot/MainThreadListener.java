package ch.wisteca.robot;

/**
 * Interface permettant d'ex�cuter une action sans arr�t.
 * @author Wisteca
 */
public interface MainThreadListener {
	
	/**
	 * M�thode appel�e sans arr�t par le thread main.
	 */
	public void onCall();
	
}
