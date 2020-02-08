package multiplayer.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * CLIENT SIDE STUFF
 *
 */
public class Client {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("169.254.12.131",5000);
			
			Scanner consoleIn = new Scanner(System.in);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println("initiating with the server");
			
			String fromServer;
			while((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				
				String fromUser = consoleIn.nextLine();
				
				if (fromUser.equalsIgnoreCase("exit"))
					break;
				else {
					out.println(fromUser);
					System.out.println("[CLIENT] "+fromUser);
				}
			}
			
			consoleIn.close();
			socket.close();
			
		} catch (Exception e) {
			
		}
	}
}
