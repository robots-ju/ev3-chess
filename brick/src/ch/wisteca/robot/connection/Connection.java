package ch.wisteca.robot.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Singleton qui s'occupe de la connexion avec le client de jeu.
 * Cette classe est thread safe.
 * @author Wisteca
 */
public class Connection {

	private static Connection myInstance;
	
	private Socket myClient;
	private boolean myConnected;
	
	private ConcurrentLinkedQueue<String> myPacketsToSend = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<PacketListener> myListeners = new ConcurrentLinkedQueue<>();
	
	/**
	 * Crée le singleton et fais attendre le thread jusqu'à  la connexion d'un client. Si cette méthode
	 * est appelée une 2eme fois, elle déconnectera le client actuelle et attendra une nouvelle connexion.
	 */
	public static void connect()
	{
		try {
			
			if(myInstance != null)
				myInstance.myClient.close();
			
			myInstance = new Connection();
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * @return l'instance du singleton
	 */
	public static Connection getInstance()
	{
		return myInstance;
	}
	
	private Connection() throws IOException 
	{
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(4000);
		
		System.out.println("En attente de connexion...");
		myClient = server.accept();
		
		myConnected = true;
		new PacketSender();
		new PacketReceiver();
		
		System.out.println("Connexion etablie avec : " + myClient.getInetAddress().toString());
	}
	
	/**
	 * Ajoute un callback sur l'objet passé en paramètre, cet objet doit hériter de l'interface {@link PacketListener}.
	 */
	public void addListener(PacketListener listener)
	{
		myListeners.add(listener);
	}
	
	/**
	 * Envoit un packet au client.
	 * Un client doit Ãªtre connectÃ© au serveur ! Voir : {@link Connection#isConnected()}
	 */
	public void sendPacket(String packet)
	{
		myPacketsToSend.add(packet);
	}
	
	/**
	 * @return true si un client est connectÃ©.
	 */
	public boolean isConnected()
	{
		return myClient != null && myClient.isClosed() == false;
	}
	
	private void disconnect()
	{
		if(myConnected)
		{
			myConnected = false;
			for(PacketListener listener : myListeners)
				listener.packetReceived("disconnect");
		}
	}
	
	private class PacketSender implements Runnable {
		
		private PrintWriter myWriter;
		
		public PacketSender() throws IOException 
		{
			myWriter = new PrintWriter(myClient.getOutputStream());
			new Thread(this, "PacketSender").start();
		}
		
		@Override
		public void run() 
		{
			while(myClient.isClosed() == false && myConnected)
			{
				String packet = myPacketsToSend.poll();
				if(packet == null)
					continue;
				
				myWriter.println(packet);
				myWriter.flush();
			}
			
			disconnect();
		}
	}
	
	private class PacketReceiver implements Runnable {
		
		private BufferedReader myReader;
		
		public PacketReceiver() throws IOException 
		{
			myReader = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
			new Thread(this, "PacketReceiver").start();
		}
		
		@Override
		public void run()
		{			
			while(myClient.isClosed() == false && myConnected)
			{
				try {
					
					String packet = myReader.readLine();
					if(packet == null)
						continue;
					
					for(PacketListener listener : myListeners)
						listener.packetReceived(packet);
					
				} catch (IOException ex) {
					break;
				}
			}
			
			disconnect();
		}	
	}
}
