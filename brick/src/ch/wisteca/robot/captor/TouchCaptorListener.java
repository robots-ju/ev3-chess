package ch.wisteca.robot.captor;

/**
 * Interface permettant d'implémenter les évenements d'un capteur.
 * @author Wisteca
 */
public interface TouchCaptorListener {
	
	/**
	 * Appelé quand le bouton est poussé.
	 * @param captor le capteur en question
	 */
	public void onPush(TouchCaptor captor);
	
	/**
	 * Appelé quand le bouton est libéré.
	 * @param captor le capteur en questionS
	 */
	public void onUnPush(TouchCaptor captor);
	
}
