package ch.wisteca.robot;

import ch.wisteca.robot.connection.PacketListener;

/**
 * Singleton qui interpr�te les commandes envoy�es au serveur pour faire bouger les moteurs.
 * @author Wisteca
 */
public class MotorController implements PacketListener {

	private static MotorController myInstance;
	
	public static MotorController getInstance()
	{
		return myInstance;
	}
	
	public MotorController()
	{
		myInstance = this;
	}
	
	@Override
	public void packetReceived(String packet) 
	{
		System.out.println(packet);
	}
}
