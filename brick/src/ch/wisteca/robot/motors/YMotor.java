package ch.wisteca.robot.motors;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

/**
 * Interface permettant de gèrer le moteur de l'axe y et la pince.
 * 
 * @author Wisteca
 */

public class YMotor extends Motor {

	// le nbr de degré pour inverser la manette
	private final static int moveController = 30;

	private EV3LargeRegulatedMotor yMotor;
	private EV3LargeRegulatedMotor pinceMotor;

	public YMotor() {
		yMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		pinceMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	}

	/**
	 * Fait descendre la pince puis la serre.
	 */
	public void clamp() {
		// ouvre la pince
		pinceMotor.rotate(moveController);
		// dessend le bras
		// attends que la pince soit en bas
		Delay.msDelay(2000);
		yMotor.rotate(moveController);
		Delay.msDelay(5000);
		// ferme la pince
		pinceMotor.rotate(-moveController);
		// remonte le moteur
		Delay.msDelay(2000);
		yMotor.rotate(-moveController);

	}

	/**
	 * Déserre la pince puis la remonte.
	 */
	public void unclamp() {
		
		// dessend le bras
		yMotor.rotate(moveController);
		Delay.msDelay(5000);
		// ouvre la pince
		pinceMotor.rotate(moveController);
		// remonte le moteur
		Delay.msDelay(2000);
		yMotor.rotate(-moveController);
		// ferme la pince
		pinceMotor.rotate(-moveController);
	}
}
