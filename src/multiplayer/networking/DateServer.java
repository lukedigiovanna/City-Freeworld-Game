package multiplayer.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {
	public static void main(String[] args) {
	
		try (ServerSocket server = new ServerSocket(55055)) {
			InetAddress address = InetAddress.getLocalHost();
			System.out.println("ip: "+address.getHostAddress());
			try (Socket socket = server.accept()) {
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				out.println(new Date());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
