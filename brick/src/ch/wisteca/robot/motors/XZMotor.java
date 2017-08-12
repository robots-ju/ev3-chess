package ch.wisteca.robot.motors;

import ch.wisteca.robot.captor.TouchCaptor;
import ch.wisteca.robot.captor.TouchCaptorListener;
import ch.wisteca.robot.math.Vec2;
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

	private static final float DEGRE_ZONE_VIDE = 780f;
	private static final float DEGRE_CASE_X = 380f;
	private static final float DEGRE_CASE_Z = 310f;
	
	private EV3MediumRegulatedMotor myMedium;
	private EV3LargeRegulatedMotor myLarge1, myLarge2;
	
	private TouchCaptor myXRightSensor, myXLeftSensor, myZSensor;
	
	private Vec2 myCurrentPosition;
	
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
		
		myXRightSensor.addListener(this);
		myXLeftSensor.addListener(this);
		myZSensor.addListener(this);
		
		myCurrentPosition = new Vec2(0);
		goTo(0, 0);
	}
	
	/**
	 * Fait bouger le chariot sur la case demandée.
	 */
	public void goTo(int ligne, int colonne)
	{
		if(ligne == 0 && colonne == 0) // retour 0 ; 0
		{
			if(myCurrentPosition.x > 0 && myCurrentPosition.y > 0)
			{
				rotateX(800, (int) myCurrentPosition.x - 200);
				rotateZ(800, (int) myCurrentPosition.y - 200);
				waitUntilStop(myLarge1, myLarge2, myMedium);
			}
			
			rotateX(true, 200);
			rotateZ(true, 200);
			waitUntilStop(myLarge1, myLarge2, myMedium);
			
			return;
		}
		
		ligne -= 1;
		colonne -= 1;
		
		float x = DEGRE_ZONE_VIDE + (ligne * DEGRE_CASE_X);
		float z = colonne * DEGRE_CASE_Z;
		
		Vec2 toGo = new Vec2(x, z);
		Vec2 deplacement = toGo.sub(myCurrentPosition);
		
		rotateX(Math.round(deplacement.normalize().x * 600), -Math.round(deplacement.x));
		rotateZ(Math.round(deplacement.normalize().y * 600), -Math.round(deplacement.y));
		waitUntilStop(myLarge1, myLarge2, myMedium);
		myCurrentPosition.x = toGo.x;
		myCurrentPosition.y = toGo.y;
		
		System.out.println("x: " + myCurrentPosition.x + " y: " + myCurrentPosition.y);
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
			myXLeftSensor.onCall();
			myXRightSensor.onCall();
			myZSensor.onCall();
			
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
