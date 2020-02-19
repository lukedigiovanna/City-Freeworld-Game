package multiplayer.networking;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class DateClient {
	public static void main(String[] args) {
		String ip = "10.0.0.3";
		int port = 55055;
		try (Socket socket = new Socket(ip,port)) {
			Scanner in = new Scanner(socket.getInputStream());
			System.out.println(in.nextLine());
			in.close();
		} catch (IOException e) {
			System.out.println("Connection Error! No server found on "+ip+":"+port);
		}
	}
}
