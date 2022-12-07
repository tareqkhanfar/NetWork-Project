package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javafx.application.Platform;


public class Server {
   public static ArrayList<User>users = new ArrayList<>() ; // will be contain all users 
   public  static Queue<String> q = new LinkedList<>(); //Contains the names of active users

	private static final int sizeBuffer = 500000; 
	static byte[] buffer = new byte[ sizeBuffer];
	static DatagramSocket datagramSocket ; // to send and receive messages for clients 
	
   public Server(DatagramSocket datagramSocket) {
	this.datagramSocket = datagramSocket ;
   }
   
   
	 public static void recive() throws IOException {
			while (true) {
				 byte[] buffer = new byte[ sizeBuffer];

				// Prepare a package and assign it buffer and and its size 
			DatagramPacket datapacket = new DatagramPacket(buffer, buffer.length); 
			// Receiving the incoming packet from the client
			// and and its size

			datagramSocket.receive(datapacket); 
			InetAddress ipUser = datapacket.getAddress(); // get IP of sender
			int portUser = datapacket.getPort(); // get Port number of sender
			// show message on screen
			Main.str += "Message from client : IP : " + ipUser  +"     PORT : " + portUser+"\n"; 
			String temp = ipUser.getHostAddress()+"#"+portUser;
			
			// convert the incoming data to String 
			String messageFromClient = new String(datapacket.getData(), datapacket.getOffset(), datapacket.getLength());
			//Check the first letter to see the type of incoming message
			char x = messageFromClient.charAt(0) ; 
			
			switch (x) {
			case 'k' : 
				
				temp = ipUser.getHostAddress()+"#"+portUser;
				 // if the IP and Port of Client already exist in queue , not added
				if (!q.contains(temp)) {
		       	q.add(temp);
				}
				break ;
				case 'A':
					// show message on screen
                Main.str+="Message : " + messageFromClient.substring(1)+"\n"; 
             // add new user to list
				users.add(new User (messageFromClient.substring(1) ,ipUser , portUser )); 
            Main.str += "Added User is done" + "#of" + users.size() +"\n";
				break ;
				      // Here the requested user information is sent by the sender
			case '@' : send(search(messageFromClient.substring(1)) , ipUser , portUser) ;
				break ;
			case 'P' : 	 
				// send public message to all users in the list
				brodCast(messageFromClient.substring(1));
				Main.str += "Send Message for all users " +"\n";

				 
				
				break ;
				
			}

	          Platform.runLater(new Runnable() { // to display any events (Strings) during run time
	            @Override
	            public void run() {
	            	Main.space.appendText(Main.str);
	                Main.str = "" ;

	            }
	          });
			}
			
			
		 
	 }
	 public static void send (User u , InetAddress ip , int port ) throws IOException {
			 byte[] buffer = new byte[ sizeBuffer];

		// if the u equal null ,  ip equal null or port equal 0 , refused it
		 if (u==null || ip == null || port ==0) { 
			 System.out.println("u is null");
		 }else {
		 /* Put the IP address and port of the person requested 
			 in the message and then send it to the sender */
			 String n = ("1"+(u.getIP_User().getHostAddress())+"#"+u.getPORT()); // put 
			 System.out.println(n);
			buffer = n.getBytes();
			DatagramPacket packet = new DatagramPacket (buffer , buffer.length , ip , port) ;
			datagramSocket.send(packet);
			
            Main.str += "user :  "+ip.getHostAddress() +" he want to connect with user : " +u.toString() +"\n";

			
		 }
	 }

	 private static void brodCast ( String message ) throws IOException {
			 byte[] buffer = new byte[ sizeBuffer];

		 // A general message is sent to all users and is received from one of the users
		 for (int i = 0 ; i < users.size() ; i++) {
			buffer =(("2")+message).getBytes();
			DatagramPacket packet = new DatagramPacket (buffer , buffer.length , users.get(i).getIP_User() , users.get(i).getPORT()) ;
			datagramSocket.send(packet);
		}
	 }


	private static User search (String id) {
	/* this Function accept user name then search it in list ,
	 if exist then return object contain user info. */
		for (int i = 0 ; i < users.size() ; i++) {
			if (users.get(i).getId_User().equals(id)) {
				return users.get(i) ;
			}
		}
		return null ;
	}
	
	public static void OnlineClient() throws IOException, InterruptedException {
		/* Here the IP's and port's in the list are compared with the IP's and port's in the queue. 
		 * If the first IP and port in the list is in the queue, 
		 * it is deleted from the queue only and skipped in the list.
		 *  But if the IP and port is not in the queue,  this means that the user has become inactive 
		 * and therefore must be deleted from the list and the queue as well
		 * */
		for (int i = 0 ; i < users.size() ; i++) {
			String s = users.get(i).getIP_User().getHostAddress()+"#" +users.get(i).getPORT() ;
			if (q.contains(s)) {
				q.remove(s);
			}
			else {
				q.remove(s);
	            Main.str += users.get(i).toString() +" --LEFT THE CHAT --\n";
				users.remove(i);
				  Platform.runLater(new Runnable() { // to display any events (Strings) during run time
			            @Override
			            public void run() {
			            	Main.space.appendText(Main.str);
			                Main.str = "" ;
			            }
			          });
					}
			}
		
		 
		
	}
public static void sendAllUsers () throws IOException, InterruptedException {
	// Send active usernames to all users
	String str = "" ;
	for (int i = 0 ; i < users.size() ; i++) {
		str += users.get(i).getId_User()+"#" ;
	}
	System.out.println(str);
    byte [] buffer_ = new byte[str.length()*8];
	for (int i = 0 ; i < users.size() ; i++) {
		buffer_ =("9"+str).getBytes();
		DatagramPacket packet = new DatagramPacket (buffer_ , buffer_.length , users.get(i).getIP_User() , users.get(i).getPORT()) ;
		datagramSocket.send(packet);
		Thread.sleep(500); // put delay half second to avoid any loss , aim : minimized loss in data
	}
}

	
}


