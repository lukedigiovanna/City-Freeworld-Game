package multiplayer.networking;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Runs a server that hosts a world for players to connect to off of the host server.
 * To play over the internet the host needs to port forward the port they choose
 * to host on.
 */
public class Server {
	
	public static void main(String[] args) {
		InetAddress add;
		try {
			add = InetAddress.getLocalHost();
			System.out.println("IP: "+add.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		Server server = new Server(5000);
	}
	
	private ServerSocket ss;
	private int port;
	
	private int numOfClients = 0;
	
	public Server(int port) {
		this.port = port;
		try {
			ss = new ServerSocket(port);
		
			while (true) {
				Socket client = ss.accept(); //wait for a client
				numOfClients++;
				System.out.println(numOfClients+" clients connected");
				new Thread(new ClientHandler(client)).start(); //start a thread to deal with the client
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
