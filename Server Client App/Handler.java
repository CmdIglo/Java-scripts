
import java.net.*;
import java.io.*;
import java.util.*;

public class Handler implements Runnable{

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String clientUsername;
	public static ArrayList<Handler> clients = new ArrayList<>();
	
	@Override
	public void run() {
		
		String message;
		
		while(socket.isConnected()) {
			try {
				message = reader.readLine();
				System.out.println(message);
				sendMessage(message);
			} catch (IOException e) {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				break;
			}
		}
		
	}
	
	public Handler(Socket socket) {
		
		try {
			
			this.socket = socket;
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.clientUsername = reader.readLine();
			clients.add(this);
			sendMessage("SERVER: " + clientUsername + " has entered the chat\r\n");
		
		} catch(IOException e) {
			clients.remove(this);
			try {
	            if (reader != null) {
	                reader.close();
	            }
	            if (writer != null) {
	                writer.close();
	            }
	            if (socket != null) {
	                socket.close();
	            }
	        } catch (IOException except) {
	            except.printStackTrace();
	        }
		}
		
	}
	
	
	//Error: stackOverFlow Error is connected with removeHandler
	public void sendMessage(String message) {
		
		if (clients.size() > 1) {
			for(Handler client : clients) {
			
				try {
					if(!client.clientUsername.equals(clientUsername)) {
						client.writer.write(message);
						client.writer.newLine();
						client.writer.flush();
					}
				} catch(IOException e) {
					clients.remove(this);
					try {
		            	if (reader != null) {
		                	reader.close();
		            	}
		            	if (writer != null) {
		                	writer.close();
		            	}
		            	if (socket != null) {
		                	socket.close();
		            	}
		        	} 	catch (IOException except) {
		            	except.printStackTrace();
		        	}
				}
				
			}
		}
		
	}
	
	
	//removeHandler class sendMessage class removeHandler calls sendMessage ...
	public void removeHandler() {
		
		clients.remove(this);
		sendMessage("SERVER: " + clientUsername + " left");
		
	}
}
