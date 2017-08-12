package ch.wisteca.robot.motors;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.Sound;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Interface permettant de gèrer le moteur de l'axe y et la pince.
 * 
 * @author Astreptocoque
 * au début, la pince est en haut et fermée
 */

public class YMotor extends Motor {

	// le nbr de degré pour inverser la manette
	private final static int moveController = 90;

	private RegulatedMotor	motorPince;
	private RegulatedMotor motorDescendPince;
//	private RegulatedMotor motorPump;
	
	/// permet la connection à la seconde brique
	private RemoteRequestEV3[] brique = new RemoteRequestEV3[1];

	public YMotor() {
		// connection à la seconde brique
		System.out.println("Connexion a la 2eme brique...");
		try {
			brique[0] = new RemoteRequestEV3(BrickFinder.find("EV2")[0].getIPAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		motorPince = brique[0].createRegulatedMotor("A", 'L');
		motorDescendPince = brique[0].createRegulatedMotor("B", 'L');
//		motorPump = brique[0].createRegulatedMotor("C", 'L');
		
		motorPince.setSpeed(100);
		motorDescendPince.setSpeed(100);
		//active le moteur de la pompe
//		motorPump.setSpeed(1200);
//		motorPump.forward();
		System.out.println("Pump pump pump");
		Sound.beepSequence();
	}

	/**
	 * Fait descendre la pince puis la serre.
	 */
	public void clamp() {
		// dessend le bras
		// attends que la pince ouverte
		motorPince.rotate(-moveController);
		Delay.msDelay(100);
		motorDescendPince.rotate(-moveController);
		Delay.msDelay(100);
		// ferme la pince
		motorPince.rotate(moveController);
		// remonte le moteur
		Delay.msDelay(200);
		motorDescendPince.rotate(moveController);

	}

	/**
	 * Déserre la pince puis la remonte.
	 */
	public void unclamp() {
		
		// dessend le bras
		motorDescendPince.rotate(-moveController);
		Delay.msDelay(200);
		// ouvre la pince
		motorPince.rotate(-moveController);
		// remonte le bras
		Delay.msDelay(100);
		motorDescendPince.rotate(moveController);
		// serre la pince
		Delay.msDelay(200);
		motorPince.rotate(moveController);
	}
}
