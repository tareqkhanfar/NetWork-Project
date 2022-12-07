
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javafx.application.Platform;

public class Client {
	static DatagramSocket datagramSocket; // // to send and receive messages for clients and server
	static InetAddress IP_Server; // store ip server to connect with it
	private static final int sizeBuffer = 60000; // size of buffer in byte
	static byte[] buffer = new byte[sizeBuffer]; // decleartion array of byte , (buffer)
	private static int portToClient = 0; // It will contain the port of the user you want to communicate with
	private static String ipToClient = ""; // It will contain the ip address of the user you want to communicate with
	private static String str2 = ""; // contain message to showing on screen during run time
	static String s;

	public Client(DatagramSocket ds, InetAddress ip) { // assgin socket , and ip of server
		this.datagramSocket = ds;
		this.IP_Server = ip;
	}

	public static synchronized void recive() throws IOException {
		while (true) {
			byte[] buffer = new byte[sizeBuffer]; // decleartion array of byte , (buffer)

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);// Prepare a package and assign it buffer
			// and and its size
			datagramSocket.receive(packet); // Receiving the incoming packet from the client or server

			// convert the incoming data to String
			String messageFromServer = new String(packet.getData(), packet.getOffset(), packet.getLength());
			// Check the first letter to see the type of incoming message
			int x = Integer.parseInt(messageFromServer.charAt(0) + "");
			if (x == 1) {
				try {
					// this message contain ip adddress and port number for requested user
					String str[] = messageFromServer.substring(1).split("#");
					str2 += "MESAGR From SERVER ## : " + str[0] + "      " + str[1] + "\n";
					ipToClient = str[0];
					portToClient = Integer.parseInt(str[1]);

				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			if (x == 2) {
				// message from client and showing on screen
				str2 += messageFromServer.substring(1) + "\n";
			}
			if (x == 9) {
				// message contain all online usernames
				System.out.println("MESSAGE : " + messageFromServer);

				String[] stt = messageFromServer.substring(1).split("#");
				s = "";
				for (int i = 0; i < stt.length; i++) {
					s += stt[i] + "\n";
				}
				Platform.runLater(new Runnable() {
					String s = Client.s;

					@Override
					public void run() {
						Main.ShowAllUsers.setText(s);

					}
				});

			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.MessageOnScreen.appendText(str2);
					str2 = "#################################################\n";
					str2 = "";
				}
			});
		}

	}

	public static void send(String Myusername, String str, int x) throws IOException {
		byte[] buffer = new byte[sizeBuffer]; // decleartion array of byte , (buffer)

		if (x == 0) {
			// Send a message to the server to store the user in the list
			buffer = ("P" + Myusername + " :  " + str).getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP_Server, 1288);
			datagramSocket.send(packet);
		}

		else if (x == 1) {
			/*
			 * send message to server , and this message contain two protocol : ‘A’ This
			 * message means that it must be sent to the server to add the user ‘@’ This
			 * message means that it must be sent to the server to get IPAdress and port
			 * number for selected user .
			 * 
			 */
			buffer = str.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP_Server, 1288);
			datagramSocket.send(packet);
		} else if (x == 2) {
			/*
			 * send message to another user
			 * 
			 */
			InetAddress ipClient = InetAddress.getByName(ipToClient);
			buffer = ("2" + Myusername + ": " + str).getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ipClient, portToClient);
			datagramSocket.send(packet);
		}

	}

	public static void sendMessageActive() throws IOException, InterruptedException {
		/*
		 * Client keeps sending the aforementioned message every 5 seconds, Server keeps
		 * updating the entry in the List, every time change has happened, it sends an
		 * updated List to all online Clients.
		 */
		byte[] buffer = ("k").getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP_Server, 1288);
		while (true) {
			datagramSocket.send(packet);
			Thread.sleep(5000); // delay 5 second to send next message
			System.out.println("NEXT");

		}
	}

}
