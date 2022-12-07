package application;
	
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;


public class Main extends Application {
	 static TextArea space ; 
	 static String str = "" ;
	 static Button bactivate;
	 
	@Override
	public void start(Stage primaryStage) {
		Thread reciveUsers = new Thread () { // this thread to receive any request from clients 
			 public void run () {
				 try {
					 Server.recive() ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			} ;
			
			
			Thread OnlineClient = new Thread (){// this thread to  Send active usernames to all users
				 public void run () {
					 
					try {
						while (true) {

							Server.OnlineClient();
							Server.sendAllUsers(); // send  userNames to all client
                            Thread.sleep(6000); // This delay helps the queue store new information
						}
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	 
				 }
				} ;
				
				// start threads
		   OnlineClient.start(); 
			reciveUsers.start();
			
		try {
		BorderPane root = new BorderPane();
		   Label server = new Label ("SERVER") ;
		    space = new TextArea() ;
			space.setFont(new Font(30));

			bactivate = new Button ("Users") ;
			bactivate.setPrefSize(180 , 80);
		   root.setTop(server);
		   root.setCenter(space);
		   root.setRight(bactivate);
			Scene scene = new Scene(root,700,400);

			primaryStage.setScene(scene );
			primaryStage.show();
			
			
			bactivate.setOnAction(e->{
				Stage s = new Stage () ;
				TextArea space2 = new TextArea() ;
				space.setFont(new Font(30));
				String str  = " " ;
				System.out.println(Server.users.size() );

				for (int i = 0 ; i < Server.users.size() ; i++) {
					str +=Server.users.get(i).toString()+"\n";
					System.out.println("*****************************************");
				}
				space2.setText(str);
				s.setScene(new Scene (space2));
				s.show();
				
			});
			
		
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SocketException {
		  DatagramSocket ds = new DatagramSocket(1288) ;// prepare socket and put port number 
	      Server s = new Server(ds);
	   
		 launch(args);
			
		
			

	}
}
	
 