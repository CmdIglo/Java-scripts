

import java.net.*;
import java.io.*;


public class Server {

	private ServerSocket serversocket;
	
	public Server(ServerSocket serversocket) {
		this.serversocket = serversocket;
	}
	
	public static void main(String[] args) {
		
		try {
			ServerSocket serversocket = new ServerSocket(3509);
			Server server = new Server(serversocket);
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		
		try {
			
			while(!serversocket.isClosed()) {
				
				Socket client_socket = serversocket.accept();
				System.out.println("A new client has connected");
				
				Handler handler = new Handler(client_socket);
				Thread thread = new Thread(handler);
				
				thread.start();
				
			}
			
		} catch (IOException e) {
			
			try {
				if (serversocket != null) {
					serversocket.close();
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			
		}
		
	}
	
} 