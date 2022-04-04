


import java.net.*;
import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

public class Client extends JFrame implements ActionListener {

	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	JFrame win;
	JMenuBar menu;
	JPanel field_input;
	JTextArea textfield;
	JScrollPane areaScrollPane; 
	JLabel label;
	JTextField text_input;
	JButton send;
	JMenu encryption;
	JMenu settings;
	JMenuItem _username;
	JMenuItem vigenere;
	JMenuItem caesar;
	JMenuItem disconnect;
	
	JFrame popup;
	JButton set;
	JPanel user_input;
	JLabel prompt;
	JTextField input;
	
	//declares which decryption method shall be used
	String method;
	
	public static String username;
	
	public static void main(String[] args) {
		
		Socket socket;
		try {
			socket = new Socket("localhost", 3509);
			Client client = new Client(socket, username);
			client.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Client(Socket socket, String username) {
		
		try {
		
			this.socket = socket;
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
			
			win = new JFrame("Messenger");
			menu = new JMenuBar();
			field_input = new JPanel();
			textfield = new JTextArea();
			textfield.setEditable(false);
			//textfield.setText("Hallo Welt");
			areaScrollPane = new JScrollPane(textfield);
			areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			areaScrollPane.setPreferredSize(new Dimension(250, 250));
		
			label = new JLabel("Type in a message");
			text_input = new JTextField(20);
			send = new JButton("Send");
		
			send.addActionListener(this);
		
			field_input.add(label);
			field_input.add(text_input);
			field_input.add(send);
		
			settings = new JMenu("Settings");
			encryption = new JMenu("Encryption");
		
			_username = new JMenuItem("Username");
			_username.addActionListener(this);
			settings.add(_username);
			
			disconnect = new JMenuItem("Disconnect");
			disconnect.addActionListener(this);
			settings.add(disconnect);
		
			vigenere = new JMenuItem("Vigenere");
			vigenere.addActionListener(this);
			caesar = new JMenuItem("Caesar");
			caesar.addActionListener(this);
			encryption.add(vigenere);
			encryption.add(caesar);
		
			menu.add(encryption);
			menu.add(settings);

			win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			win.setSize(500, 500);
			win.getContentPane().add(BorderLayout.NORTH, menu);
			win.getContentPane().add(BorderLayout.SOUTH, field_input);
			win.getContentPane().add(BorderLayout.CENTER, areaScrollPane);
			win.setVisible(true);
			
		} catch(IOException e) {
			
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
	
	//vigenere encryption algorithm
	public char[] vigenere(char[] satz, char[] key) {
		
		//counts the number of spaces
		int num_spaces = 0;
		for (char elem : satz) {
			if ((int) elem == 32) {
				num_spaces++;
			}
		}
				
		//declares variables for the clean-up of the message, so the deletion of spaces in the message
		char[] clean_satz1 = new char[satz.length];
		int last_insert = 0;
			    
		//cleans the spaces
	    for (int i = 0; i < satz.length; i++){
			if (i == findIndex(satz, (char) 32)) {
			     satz[i] = 0xFF;
			     i++;
			}
			clean_satz1[last_insert++] = satz[i];
	    }
				
	    //because I messed up the cleansing above, the code adds 0s to the end of the cleaned-up String, so these have to be 
		//deleted by "list comprehension"
	    //luckily the number of 0s corresponds to the number of spaces, so thats why we declared a num_spaces before
	    char[] clean_satz = Arrays.copyOfRange(clean_satz1, 0, clean_satz1.length-num_spaces);
			    
	    //System.out.println(clean_satz);
				
	    //the algorithm
	    //basically the alphabet-index-value of a character in the message is being added to the corresponding 
	    //alphabet-index-value of a character in the key
	    //works with the decimal values of characters of the English alphabet in the ASCII table
	    //recognizes capital letters -> Pascal-case String as output
	    for (int i = 0; i < clean_satz.length; i++) {
	    	if ((int) clean_satz[i] >= 65 && (int) clean_satz[i] <= 90) {
	    		if ((int) key[i%key.length] >= 65 && (int) key[i%key.length] <= 90) {
	    			clean_satz[i] = (char) ((((((int) clean_satz[i]) - 65) + (((int) key[i%key.length]) - 65)) % 26) + 65);
	    		} else {
	    			clean_satz[i] = (char) ((((((int) clean_satz[i]) - 65) + (((int) key[i%key.length] - 32) - 65)) % 26) + 65);
	    		}
	    	} else {
	    		if ((int) key[i%key.length] >= 65 && (int) key[i%key.length] <= 90) {
	    			clean_satz[i] = (char) ((((((int) clean_satz[i]) - 97) + (((int) key[i%key.length] + 32) - 97)) % 26) + 97);
	    		} else {
	    			clean_satz[i] = (char) ((((((int) clean_satz[i]) - 97) + (((int) key[i%key.length]) - 97)) % 26) + 97);	
	    		}
	    	}
	    }
				
	    return clean_satz;
		
	}
	
	//caesar encryption algorithm
	/**
	 * TODO: Implement caesar encryption algorithm
	 * @param text, the text that is supposed to be encrypted
	 * @return -
	 */
	public String caesar(String text) {
		
		return "";
		
	}
	
	//will be called, when menuitem "username" is selected to set the username of the client
	public void set_username() {
		
		popup = new JFrame();
		user_input = new JPanel();
		prompt = new JLabel("Set your username: ");
		input = new JTextField(20);
		set = new JButton("Set");
		
		set.addActionListener(this);
		
		user_input.add(prompt);
		user_input.add(input);
		user_input.add(set);
		
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(300, 100);
		popup.getContentPane().add(BorderLayout.CENTER, user_input);
		popup.setVisible(true);
		
	}
	
	public void actionPerformed (ActionEvent event) {
		
		if (event.getSource() == this.send && username != null && text_input.getText() != "") {
			
			String text = text_input.getText();
			send();
			textfield.append(username + ": " + text + "\r\n");
			
		} else if (event.getSource() == this.send && username == null && text_input.getText() == "") {
			
			//textfield.append("Set username first!\r\n");
			
		} else if (event.getSource() == this.send && username == null && text_input.getText() != "") {
			
			text_input.setText("");
			textfield.append("Set username first!\r\n");
			
		}
		
		if (event.getSource() == this._username) {
		
			set_username();
		
		} 
		
		
		// before the text gets send to the server, the client app checks the "method" variable
		// and decides, based on its value, which encryption procedure is used for encrypting the
		// text
		if (event.getSource() == this.vigenere) {
			//do something
			method = "vigenere";
		}
		
		if (event.getSource() == this.caesar) {
			//do something
			method = "caesar";
		}
		
		if (event.getSource() == this.set) {
			
			String name = input.getText();
			username = name;
			win.setTitle("Messenger - " + username);
			System.out.println("Username set!");
			try {
				writer.write(username);
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			popup.dispose();
			
		}
		
		if (event.getSource() == this.disconnect && socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void listen() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				String msg;
				
				while(socket.isConnected()) {
					
					try {
						msg = reader.readLine();
						textfield.append(msg);
						textfield.append("\r\n");
					} catch (IOException e) {
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
				
			}
		}).start();
		
	}
	
	public void send() {
		
		try {
			if (socket.isConnected()) {
				
				String message = text_input.getText();
				text_input.setText("");
				
				if (method == "vigenere") {
					
					char[] encrypted = message.toCharArray();
					String key = "hamburg";
					char[] _key = key.toCharArray();
					char[] en_msg = vigenere(encrypted, _key);
					message = String.valueOf(en_msg);
					
				} else if (method == "caesar") {
					
					message = caesar(message);
					
				}
				
				if (message != "") {
				
					writer.write(username + ": " + message);
					writer.newLine();
					writer.flush();

				}
				
			}
		} catch (IOException e) {

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
	
	//function to find the index of a specific value in a character array
	// -> Only yields first occurrence and doesn't take into consideration more occurrences i.e. can only be used for one iteration
	// -> Problem can be solved by making two instances of the iterable and deleting the occurrences of a specific value in one iterable
	// in each iteration, so that this function finds the next position in the next iteration
	/** 
	 * @param char_list  The List which will be searched in
	 * @param character  The character that will be looked for 
	 * @return int       Returns the index of the asked character
	 */
	public static int findIndex(char[] char_list, char character) {
		
		//initiating a "index" variable which will hold the index of the specific element in an array
		//the value is set to -1 so that if there was something going wrong in the code, the function would return
		//a value which could never be a realistic index in an array
		int index = -1;
		
		for (int i = 0; i < char_list.length; i++) {
			if (char_list[i] == character) {
				index = i;
				break;
			}
		}
			
		return index;
	}
}
