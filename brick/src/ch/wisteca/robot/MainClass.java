package ch.wisteca.robot;

import java.util.concurrent.ConcurrentLinkedQueue;

import ch.wisteca.robot.connection.Connection;

/**
 * Classe principale, gère le thread main.
 * @author Wisteca
 */
public class MainClass  {

	private static ConcurrentLinkedQueue<MainThreadListener> myListeners = new ConcurrentLinkedQueue<>();
	
	public static void main(String[] args)
	{
		Connection.connect();
		Connection.getInstance().addListener(new MotorController());
		
		while(true)
		{
			for(MainThreadListener listener : myListeners)
				listener.onCall();
		}
	}
	
	/**
	 * Ajoute un listener au thread main.
	 */
	public static void addListener(MainThreadListener listener)
	{
		myListeners.add(listener);
	}
}
