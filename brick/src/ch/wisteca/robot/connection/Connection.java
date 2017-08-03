package ch.wisteca.robot.connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.wisteca.robot.MainClass;
import ch.wisteca.robot.MainThreadListener;

/**
 * Singleton qui s'occupe de la connexion avec le client de jeu.
 * Cette classe est thread safe.
 * @author Wisteca
 */
public class Connection implements MainThreadListener {

	private static Connection myInstance;
	
	private Socket myClient;
	private boolean myConnected;
	
	private ConcurrentLinkedQueue<String> myPacketsToSend = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Packet> myPacketsToCall = new ConcurrentLinkedQueue<>();
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
		
		MainClass.addListener(this);
		
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
	 * Un client doit être connecte au serveur ! Voir : {@link Connection#isConnected()}
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
	
	/**
	 * Méthode appelée par le thread main.
	 */
	@Override
	public void onCall()
	{
		while(true)
		{
			Packet packet = myPacketsToCall.poll();
			if(packet == null)
				break;
			
			for(PacketListener listener : myListeners)
				listener.packetReceived(packet);
		}
	}
	
	private void disconnect()
	{
		if(myConnected)
		{
			myConnected = false;
			Packet packet = new Packet();
			for(PacketListener listener : myListeners)
				listener.packetReceived(packet); // Envoi d'un paquet vide pour signaler la déconnexion.
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
		
		private DataInputStream myDataReader;
		
		public PacketReceiver() throws IOException 
		{
			myDataReader = new DataInputStream(myClient.getInputStream());
			new Thread(this, "PacketReceiver").start();
		}
		
		@Override
		public void run()
		{			
			while(myClient.isClosed() == false && myConnected)
			{
				try {
					
					byte[] bytes = new byte[5];
					myDataReader.read(bytes);
					
					Packet packet = new Packet();
					packet.myLettreDepart = (char) (97 + bytes[0]);
					packet.myNumDepart = ((int) bytes[1]) + 1;
					packet.myLettreArrive = (char) (97 + bytes[2]);
					packet.myNumArrive = ((int) bytes[3]) + 1;
					packet.myCapture = bytes[4] == 0 ? false : true;
					 
					myPacketsToCall.add(packet);
					
				} catch (IOException ex) {
					break;
				}
			}
			
			disconnect();
		}	
	}
}
