package ch.wisteca.robot.connection;

/**
 * Interface impl�mentant une m�thode de callback. 
 * @author Wisteca
 */
public interface PacketListener {

	/**
	 * Callback appel�e lorsqu'un packet est re�u du client.
	 * @param packet le contenu du packet
	 */
	public void packetReceived(Packet packet);
	
}
