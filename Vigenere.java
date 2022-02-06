/*
 * Author: Maxwell Leu
 * Github: CmdIglo
 * About: 
 * This small code is a console-application to encrypt a message with the Vigenere-Encryption. The user can input a message
 * and a key. The message will then be encrypted using the user-specified key. For a, a little more detailed, explaination of
 * the Vigenere-Encryption see my repo "Python-scripts" where I implemented the exact same encryption with Python. And as 
 * said in that document, my implementations aren't perfect and they are not meant to be the most efficient and easiest
 * ways to code a solution for a problem or task. They are written because I had fun diving a little deeper into some 
 * topics and these codes really help to get a better grasp of some problems in the world of computer science and 
 * its surroundings. The encrypted message is far from perfect, which means, that the encrypted message doesn't support
 * spaces. Its also important to note, that my encryption algorithm only supports standard English alphabet ASCII characters.
 * I guess Vigenere didn't know too many other characters either :)
*/
package any;
import java.util.*;

public class Vigenere {
	
	public static void main(String[] args) {
		
		//reading in user input such as the message being encrypted as well as the key used for the vigenere encryption
		Scanner sc = new Scanner(System.in);
		System.out.println("Input a sentence for encryption:");
		String satz = sc.nextLine(); //message
		
		char[] satz_char = satz.toCharArray();
		
		System.out.println("----------------------------");
		
		System.out.println("Input a key:");
		String key = sc.nextLine(); //key
		
		sc.close();
		
		char[] key_list = key.toCharArray();
		
		System.out.println("----------------------------");
		System.out.println("The message: " + satz);
		System.out.println();
		System.out.println("The key: " + key);
		System.out.println();
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
		System.out.println();
		System.out.println("The encrypted message: ");
		
		encrypt(satz_char, key_list);
		
	}
	
	//the encryption function, where the magic happens
	//takes in a message/sentence and a key/word as input
	//currently spaces are being ignored and the encrypted message is not spaced but a long pascal-case String
	public static void encrypt(char[] satz, char[] key) {
		
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
		//recognizes capital letters -> pascal-case String as output
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
		
		System.out.println(clean_satz);
	
	}
	
	//function to find the index of a specific value in a character array
	// -> Only yields first occurrence and doesn't take into consideration more occurrences i.e. can only be used for one iteration
	// -> Problem can be solved by making two instances of the iterable and deleting the occurrences of a specific value in one iterable
	// in each iteration, so that this function finds the next position in the next iteration
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
