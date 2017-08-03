package ch.wisteca.robot.connection;

/**
 * Interface implémentant une méthode de callback. 
 * @author Wisteca
 */
public interface PacketListener {

	/**
	 * Callback appelée lorsqu'un packet est reçu du client.
	 * @param packet le contenu du packet
	 */
	public void packetReceived(Packet packet);
	
}
