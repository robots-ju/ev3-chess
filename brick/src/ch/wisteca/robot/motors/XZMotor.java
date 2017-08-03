package ch.wisteca.robot.motors;

import ch.wisteca.robot.math.Vector2D;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTTouchSensor;

/**
 * Interface qui gère les moteurs de déplacements du chariot (axes x et z).
 * @author Wisteca
 */
public class XZMotor extends Motor {

	private static final float DEGRE_ZONE_VIDE = 666;
	private static final float DEGRE_CASE_X = 331.625f;
	private static final float DEGRE_CASE_Z = 269.875f;
	private static final char[] LETTRES = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
	
	private EV3MediumRegulatedMotor myMedium;
	private EV3LargeRegulatedMotor myLarge1, myLarge2;
	
	private NXTTouchSensor myXRightSensor, myXLeftSensor, myZSensor;
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
		
		myXRightSensor = new NXTTouchSensor(SensorPort.S1);
		myXRightSample = new float[myXRightSensor.sampleSize()];
		myXLeftSensor = new NXTTouchSensor(SensorPort.S2);
		myXLeftSample = new float[myXRightSensor.sampleSize()];
		myZSensor = new NXTTouchSensor(SensorPort.S3);
		myZSample = new float[myXRightSensor.sampleSize()];
		
		myLarge1.setSpeed(300);
		myLarge2.setSpeed(300);
		myMedium.setSpeed(300);
		
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
			
			if(myLarge1.isMoving() == false && myMedium.isMoving() == false)
				break;
		}
		
		myCurrentPosition = new Vector2D(0);
	}
	
	/**
	 * Fait bouger le chariot sur la case demandée.
	 */
	public void goTo(char ligne, int colonne)
	{
		int caseLigne = 0;
		for(int i = 0 ; i < LETTRES.length ; i++)
		{
			if(LETTRES[i] == ligne)
			{
				caseLigne = i;
				break;
			}
		}
		
		float x = DEGRE_ZONE_VIDE + (caseLigne * DEGRE_CASE_X);
		float z = colonne * DEGRE_CASE_Z;
		Vector2D toGo = new Vector2D(x, z);
		Vector2D deplacement = toGo.add(myCurrentPosition);
		
		myLarge1.startSynchronization();
		int depX = Math.round(deplacement.x);
		myLarge1.rotate(depX);
		myLarge2.rotate(depX);
		myLarge1.endSynchronization();
		myMedium.rotate(Math.round(deplacement.y));
		
		while(true)
		{
			if(myLarge1.isMoving() == false && myMedium.isMoving() == false)
				break;
		}
	}
}
