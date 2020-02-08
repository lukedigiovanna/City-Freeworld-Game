package multiplayer.networking;

import java.io.*;
import java.net.Socket;

/**
 * This is the server handling the client
 *
 *	SERVER SIDE
 */
public class ClientHandler implements Runnable {
	
	private final Socket client;
	
	public ClientHandler(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			
			//out.println("Welcome to the server!");
			
			String fromClient;
			while ((fromClient = in.readLine()) != null) {
				out.println("[SERVER] "+fromClient+" (but better message)");
				if (fromClient.equalsIgnoreCase("exit"))
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
