package ch.wisteca.robot.motors;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Interface permettant de gèrer le moteur de l'axe y et la pince.
 * 
 * @author Astreptocoque
 */

public class YMotor extends Motor {

	// le nbr de degré pour inverser la manette
	private final static int moveController = 90;

	private RegulatedMotor	motorPince;
	private RegulatedMotor motorDescendPince;
	private RegulatedMotor motorPump;
	
	/// permet la connection à la seconde brique
	private RemoteRequestEV3[] brique = new RemoteRequestEV3[1];

	public YMotor() {
		// connection à la seconde brique
		
		try {
			brique[0] = new RemoteRequestEV3(BrickFinder.find("EV2")[0].getIPAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		motorPince = brique[0].createRegulatedMotor("A", 'L');
		motorDescendPince = brique[0].createRegulatedMotor("B", 'L');
		motorPump = brique[0].createRegulatedMotor("C", 'M');
		
		motorPince.setSpeed(100);
		motorDescendPince.setSpeed(100);
		//active le moteur de la pompe
		motorPump.setSpeed(1200);
		motorPump.forward();

	}

	/**
	 * Fait descendre la pince puis la serre.
	 */
	public void clamp() {
		// ouvre la pince
		motorPince.rotate(-moveController);
		// dessend le bras
		// attends que la pince ouverte
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
		// remonte le moteur
		Delay.msDelay(100);
		motorDescendPince.rotate(moveController);
		// ferme la pince
		motorPince.rotate(moveController);
	}
}
