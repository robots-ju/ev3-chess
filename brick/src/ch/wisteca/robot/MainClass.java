package ch.wisteca.robot;

import ch.wisteca.robot.connection.Connection;

public class MainClass  {

	public static void main(String[] args) throws InterruptedException
	{
		Connection.connect();
		Connection.getInstance().addListener(new MotorController());
		
		while(true)
		{
			
		}
	}
}
