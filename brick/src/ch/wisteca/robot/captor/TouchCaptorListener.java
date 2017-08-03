package ch.wisteca.robot.captor;

/**
 * Interface permettant d'impl�menter les �venements d'un capteur.
 * @author Wisteca
 */
public interface TouchCaptorListener {
	
	/**
	 * Appel� quand le bouton est pouss�.
	 * @param captor le capteur en question
	 */
	public void onPush(TouchCaptor captor);
	
	/**
	 * Appel� quand le bouton est lib�r�.
	 * @param captor le capteur en questionS
	 */
	public void onUnPush(TouchCaptor captor);
	
}
