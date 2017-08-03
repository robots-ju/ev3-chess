package ch.wisteca.robot;

import ch.wisteca.robot.connection.Packet;
import ch.wisteca.robot.connection.PacketListener;
import ch.wisteca.robot.motors.YMotor;
import lejos.hardware.Sound;

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
		YMotor motor = new YMotor();
		motor.clamp();
		Sound.beepSequence();
		motor.unclamp();
		//new XZMotor();
	}
	
	@Override
	public void packetReceived(Packet packet) 
	{
		System.out.println(packet);
	}
}
