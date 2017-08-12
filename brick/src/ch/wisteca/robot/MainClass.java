package ch.wisteca.robot;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.wisteca.robot.connection.Connection;
import lejos.hardware.Button;

/**
 * Classe principale, gère le thread main.
 * @author Wisteca
 */
public class MainClass  {

	private static ConcurrentLinkedQueue<MainThreadListener> myListeners = new ConcurrentLinkedQueue<>();
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		new ThreadListenerCall();
		Connection.create();
		Connection.getInstance().addListener(new MotorController());
		System.out.println("Retour en 0;0 termine");
		Connection.getInstance().connect();
	}
	
	/**
	 * Ajoute un listener au thread main.
	 */
	public static void addListener(MainThreadListener listener)
	{
		myListeners.add(listener);
	}
	
	private static class ThreadListenerCall implements Runnable {

		public ThreadListenerCall()
		{
			new Thread(this, "ThreadListenerCall").start();
		}
		
		@Override
		public void run()
		{
			while(true)
			{
				for(MainThreadListener listener : myListeners)
					listener.onCall();
				
				if(Button.ENTER.isDown())
					System.exit(0);
			}
		}
	}
}
