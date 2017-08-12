package ch.wisteca.robot;

import ch.wisteca.robot.connection.Connection;
import ch.wisteca.robot.connection.Packet;
import ch.wisteca.robot.connection.PacketListener;
import ch.wisteca.robot.motors.XZMotor;
import ch.wisteca.robot.motors.YMotor;

/**
 * Singleton qui interprète les commandes envoyées au serveur pour faire bouger les moteurs.
 * @author Wisteca
 */
public class MotorController implements PacketListener {

	private static MotorController myInstance;
	
	private XZMotor myXZMotor;
	private YMotor myYMotor;
	
	public static MotorController getInstance()
	{
		return myInstance;
	}
	
	public MotorController() throws InterruptedException
	{
		myInstance = this;
		
		myYMotor = new YMotor();
		myXZMotor = new XZMotor();
		/*
		myXZMotor.goTo(3, 6);
		myYMotor.clamp();
		myXZMotor.goTo(1, 3);
		myYMotor.unclamp();*/
	}
	
	@Override
	public void packetReceived(Packet packet) 
	{
		System.out.println("start mouvement");
		
		if(packet.myCapture)
		{System.out.println("pion a capturer");
			myXZMotor.goTo(packet.myNumArrive, packet.myLettreArrive);
			myYMotor.clamp();
			myXZMotor.goTo(0, 0);
			myYMotor.unclamp();
		}
		System.out.println("deplacement");
		myXZMotor.goTo(packet.myNumDepart, packet.myLettreDepart);
		myYMotor.clamp();
		myXZMotor.goTo(packet.myNumArrive, packet.myLettreArrive);
		myYMotor.unclamp();
		System.out.println("retour en 0;0");
		myXZMotor.goTo(0, 0);
		Connection.getInstance().sendPacket("1");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
