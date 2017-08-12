package ch.wisteca.robot.connection;

/**
 * Structure contenant les infos d'un paquet.
 * @author Wisteca
 */
public class Packet {
	
	public int myLettreDepart;
	public int myNumDepart;
	public int myLettreArrive;
	public int myNumArrive;
	public boolean myCapture;
	
	@Override
	public String toString()
	{
		return myLettreDepart + ", " + myNumDepart + ", " + myLettreArrive + ", " + myNumArrive + ", " + myCapture;
	}
}
