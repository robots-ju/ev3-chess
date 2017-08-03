package ch.wisteca.robot.motors;

import ch.wisteca.robot.captor.TouchCaptor;
import ch.wisteca.robot.captor.TouchCaptorListener;
import ch.wisteca.robot.math.Vector2D;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

/**
 * Interface qui gère les moteurs de déplacements du chariot (axes x et z).
 * @author Wisteca
 */
public class XZMotor extends Motor implements TouchCaptorListener {

	private static final float DEGRE_ZONE_VIDE = 666;
	private static final float DEGRE_CASE_X = 331.625f;
	private static final float DEGRE_CASE_Z = 269.875f;
	private static final char[] LETTRES = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
	
	private EV3MediumRegulatedMotor myMedium;
	private EV3LargeRegulatedMotor myLarge1, myLarge2;
	
	private TouchCaptor myXRightSensor, myXLeftSensor, myZSensor;
	
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
		
		myXRightSensor = new TouchCaptor(SensorPort.S1);
		myXLeftSensor = new TouchCaptor(SensorPort.S2);
		myZSensor = new TouchCaptor(SensorPort.S3);
		
		rotateX(true, 300);
		rotateZ(true, 300);
		waitUntilStop(myLarge1, myLarge2, myMedium);
		
		myCurrentPosition = new Vector2D(0);
	}
	
	/**
	 * Fait bouger le chariot sur la case demandée.
	 */
	public void goTo(char ligne, int colonne)
	{
		if(ligne == 'Z' && colonne == 0) // retour 0 ; 0
		{
			rotateX(true, 500);
			rotateZ(true, 500);
			waitUntilStop(myLarge1, myLarge2, myMedium);
			return;
		}
		
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
		Vector2D deplacement = myCurrentPosition.add(toGo);
		
		rotateX(Math.round(deplacement.length() / deplacement.normalize().x * 300), Math.round(deplacement.x));
		rotateZ(Math.round(deplacement.length() / deplacement.normalize().y * 300), Math.round(deplacement.y));
		waitUntilStop(myLarge1, myLarge2, myMedium);
		myCurrentPosition.x = toGo.x;
		myCurrentPosition.y = toGo.y;
	}
	
	private void rotateX(boolean forward, int speed)
	{
		myLarge1.setSpeed(speed);
		myLarge2.setSpeed(speed);
		
		myLarge1.startSynchronization();
		if(speed == 0)
		{
			myLarge1.stop();
			myLarge2.stop();
		}
		else if(forward)
		{
			myLarge1.forward();
			myLarge2.forward();
		}
		else
		{
			myLarge1.backward();
			myLarge2.backward();
		}
		myLarge1.endSynchronization();
	}
	
	private void rotateX(int speed, int degres)
	{
		myLarge1.setSpeed(speed);
		myLarge2.setSpeed(speed);
		
		myLarge1.startSynchronization();
		myLarge1.rotate(degres);
		myLarge2.rotate(degres);
		myLarge1.endSynchronization();
	}
	
	private void rotateZ(boolean forward, int speed)
	{
		myMedium.setSpeed(speed);
		if(speed == 0)
			myMedium.stop();
		else if(forward)
			myMedium.forward();
		else
			myMedium.backward();
	}
	
	private void rotateZ(int speed, int degres)
	{
		myMedium.setSpeed(speed);
		myMedium.rotate(degres);
	}
	
	private void waitUntilStop(BaseRegulatedMotor... motors)
	{
		while(true)
		{
			boolean flag = true;
			for(BaseRegulatedMotor motor : motors)
			{
				if(motor.isMoving())
				{
					flag = false;
					break;
				}
			}
			
			if(flag == true)
				break;
		}
	}

	@Override
	public void onPush(TouchCaptor captor)
	{
		if(captor.equals(myXLeftSensor) || captor.equals(myXRightSensor))
		{
			rotateX(true, 0);
			myCurrentPosition.x = 0;
		}
		else if(captor.equals(myZSensor))
		{
			rotateZ(true, 0);
			myCurrentPosition.y = 0;
		}
	}
	
	@Override
	public void onUnPush(TouchCaptor captor)
	{}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
