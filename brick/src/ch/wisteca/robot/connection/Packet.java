package ch.wisteca.robot.connection;

/**
 * Structure contenant les infos d'un paquet.
 * @author Wisteca
 */
public class Packet {
	
	public char myLettreDepart;
	public int myNumDepart;
	public char myLettreArrive;
	public int myNumArrive;
	public boolean myCapture;
	
	@Override
	public String toString()
	{
		return myLettreDepart + ", " + myNumDepart + ", " + myLettreArrive + ", " + myNumArrive + ", " + myCapture;
	}
}
