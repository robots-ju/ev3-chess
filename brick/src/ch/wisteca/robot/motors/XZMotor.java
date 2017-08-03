package ch.wisteca.robot.motors;

import ch.wisteca.robot.math.Vector2D;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;

/**
 * Interface qui gère les moteurs de déplacements du chariot (axes x et z).
 * @author Wisteca
 */
public class XZMotor extends Motor {

	private EV3MediumRegulatedMotor myMedium;
	private EV3LargeRegulatedMotor myLarge1, myLarge2;
	
	private EV3TouchSensor myXRightSensor, myXLeftSensor, myZSensor;
	private float[] myXRightSample, myXLeftSample, myZSample;
	
	private Vector2D myCurrentPosition;
	
	/**
	 * Initialise les moteurs et capteurs puis ramène le chariot à sa position initiale.
	 */
	public XZMotor()
	{
		myMedium = new EV3MediumRegulatedMotor(MotorPort.A);
		myLarge1 = new EV3LargeRegulatedMotor(MotorPort.B);
		myLarge2 = new EV3LargeRegulatedMotor(MotorPort.C);
		
		myLarge1.synchronizeWith(new EV3LargeRegulatedMotor[] {myLarge2});
		
		myXRightSensor = new EV3TouchSensor(SensorPort.S1);
		myXRightSample = new float[myXRightSensor.sampleSize()];
		myXLeftSensor = new EV3TouchSensor(SensorPort.S2);
		myXLeftSample = new float[myXRightSensor.sampleSize()];
		myZSensor = new EV3TouchSensor(SensorPort.S3);
		myZSample = new float[myXRightSensor.sampleSize()];
		
		myLarge1.setSpeed(100);
		myLarge2.setSpeed(100);
		myMedium.setSpeed(100);
		
		myLarge1.startSynchronization();
		myLarge1.forward();
		myLarge2.forward();
		myLarge1.endSynchronization();
		myMedium.forward();
		
		while(true)
		{
			myXLeftSensor.fetchSample(myXLeftSample, 0);
			myXRightSensor.fetchSample(myXRightSample, 0);
			myZSensor.fetchSample(myZSample, 0);
			
			if(myXLeftSample[0] == 1 || myXRightSample[0] == 1)
			{
				myLarge1.startSynchronization();
				myLarge1.stop();
				myLarge2.stop();
				myLarge1.endSynchronization();
			}
			
			if(myZSample[0] == 1)
				myMedium.stop();
			
			if(myLarge1.isMoving() == false || myMedium.isMoving() == false)
				break;
		}
		
		myCurrentPosition = new Vector2D(0);
	}
	
	/**
	 * Fait bouger le chariot sur la case demandée.
	 */
	public void goTo(char ligne, int colonne)
	{
		
	}
}
