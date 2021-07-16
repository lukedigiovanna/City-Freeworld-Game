package multiplayer.networking;

import java.io.*;
import java.net.*;

import world.World;

public class Server {

	private ServerSocket server;
	private boolean active = true;
	
	// game related info.
	private World world;
	
	public Server(int port) {
		// create the server
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// inform the user of the IP address that the server is currently run on
		try {			
			InetAddress address = InetAddress.getLocalHost();
			System.out.println("Hosting on IP: " + address.getHostAddress() + ":" + port);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void acceptConnections() {
		while (active) {
			try {
				Socket client = server.accept();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		active = false;
	}
	
	public static void main(String[] args) {
		new Server(5000);
	}
	
}
