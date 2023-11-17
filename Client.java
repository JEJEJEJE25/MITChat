

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.StringReader;

public class Client {

  private String host;
  private int port;

  public static void main(String[] args) throws UnknownHostException, IOException {
	Scanner sc = new Scanner(System.in);
	System.out.println("JOIN PROTOCOL");
	System.out.println("Enter the Port Number: ");
	int port1 = sc.nextInt();
	if(port1==12345) {
		System.out.println("Welcome to MIT Chat System");
		new Client("127.0.0.1", port1).run();
	}else {
		System.out.println("Cannot Connect to the Network");
		System.exit(0);
	}
   }

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void run() throws UnknownHostException, IOException {
    // connect client to server
    Socket client = new Socket(host, port);
    System.out.println("Client successfully connected to server!");

    // Get Socket output stream (where the client send her msg)
    PrintStream output = new PrintStream(client.getOutputStream());
    
    // ask for a nickname
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter a nickname: ");
    String nickname = sc.nextLine();
    output.println(nickname);
    menu(client, output, sc);
 

  }

private void menu(Socket client, PrintStream output, Scanner sc) throws IOException {
	// TODO Auto-generated method stub
	Scanner sc2 = new Scanner(System.in);
	System.out.println("MIT Chat System");
	System.out.println("[1] - Send a Message");
	System.out.println("[2] - Show Contacts");
	System.out.println("[3] - Leave Chat");
	System.out.println("[0] - Terminate the Program");
	System.out.println("Enter Choice: ");
	int choice = sc2.nextInt();
	switch(choice) {
	case 1:
		System.out.println("MSG Protocol");
		message(client, output, sc);
		break;
	case 2:
		System.out.println("LIST Protocol");
		System.out.println("USERS LIST: " +new ArrayList<String>(Arrays.asList()));
		break;
	case 3:
		System.out.println("LEAVE Protocol");
		break;
	case 0 :
		System.exit(0);;
		break;
	default:
		break;
	}
}


private void message(Socket client, PrintStream output, Scanner sc2) throws IOException {
	
    new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();
    System.out.println("Messages Panel");
    System.out.println("Type"+" end "+"to exit the chat");
    System.out.println("To send a private message put @ then the nickname");


    		while (sc2.hasNextLine()) {   
    	    output.println(sc2.nextLine());
    	    String input = sc2.nextLine();
    	    if(input.equalsIgnoreCase("end")) {
    	    	System.out.println("Exiting the chat");
    	    	break;
    	    	
    	    }
    	    }
 
    	menu(client, output, sc2);
    	output.close();
    	sc2.close();
    	client.close(); 
	}
}

class ReceivedMessagesHandler implements Runnable {

  private InputStream server;

  public ReceivedMessagesHandler(InputStream server) {
    this.server = server;
  
  }

  public void run() {
    // receive server messages and print out to screen
    Scanner s = new Scanner(server);
    String tmp = "";
    while (s.hasNextLine()) {
      tmp = s.nextLine();
      if (tmp.charAt(0) == '[') {
        tmp = tmp.substring(1, tmp.length()-1);
        System.out.println("LIST Protocol");
       System.out.println("USERS LIST: " +new ArrayList<String>(Arrays.asList(tmp.split(","))));
      }else{
        try {
          System.out.println("\n" );
          // System.out.println(tmp);
        } catch(Exception ignore){}
      }
    }
    s.close();
  }



}