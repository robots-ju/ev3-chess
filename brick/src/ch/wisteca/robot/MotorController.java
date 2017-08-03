package ch.wisteca.robot;

import ch.wisteca.robot.connection.Packet;
import ch.wisteca.robot.connection.PacketListener;
import ch.wisteca.robot.motors.YMotor;

/**
 * Singleton qui interprète les commandes envoyées au serveur pour faire bouger les moteurs.
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
		new YMotor();
	}
	
	@Override
	public void packetReceived(Packet packet) 
	{
		System.out.println(packet);
	}
}
