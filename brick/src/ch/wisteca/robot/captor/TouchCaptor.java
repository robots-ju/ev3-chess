package ch.wisteca.robot.captor;

import java.util.ArrayList;

import ch.wisteca.robot.MainClass;
import ch.wisteca.robot.MainThreadListener;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTTouchSensor;

/**
 * Représente un capteur NXT. Possibilité d'écouter des événements.
 * @author Wisteca
 */
public class TouchCaptor implements MainThreadListener {
	
	private ArrayList<TouchCaptorListener> myListeners = new ArrayList<>();
	
	private NXTTouchSensor mySensor;
	private float[] mySample;
	private boolean myLastState = true;
	
	/**
	 * Crée un capteur au port donné.
	 * @see NXTTouchSensor#NXTTouchSensor(Port)
	 */
	public TouchCaptor(Port port)
	{
		mySensor = new NXTTouchSensor(port);
		mySample = new float[mySensor.sampleSize()];
		MainClass.addListener(this);
	}
	
	/**
	 * Ajoute un listener au capteur.
	 */
	public void addListener(TouchCaptorListener listener)
	{
		myListeners.add(listener);
	}
	
	/**
	 * @return true si le capteur est enfoncé
	 */
	public boolean isPressed()
	{
		onCall();
		return myLastState;
	}
	
	@Override
	public void onCall()
	{
		mySensor.fetchSample(mySample, 0);
		if(mySample[0] == 0)
		{
			if(myLastState == true)
			{
				for(TouchCaptorListener listener : myListeners)
					listener.onUnPush(this);
			}
			myLastState = false;
		}
		else if(mySample[0] == 1)
		{
			if(myLastState == false)
			{
				for(TouchCaptorListener listener : myListeners)
					listener.onPush(this);
			}
			myLastState = true;
		}
	}
}
