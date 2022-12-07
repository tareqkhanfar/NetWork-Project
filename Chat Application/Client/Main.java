
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	static TextArea MessageOnScreen;
	static TextField message;
	static Label client_Label;
	static Button UsersActive;
	static TextField username;
	static TextField selectUserName;
	static TextArea ShowAllUsers = new TextArea();

	@Override
	public void start(Stage primaryStage) throws SocketException, UnknownHostException {
		// Create a UDP Socket to send and receive data
		DatagramSocket ds = new DatagramSocket();
		// put IP address of server
		InetAddress ipServer = InetAddress.getByName("127.0.0.1");
		// Generate Object from client Class and assign socket and ipServer
		Client clienttt = new Client(ds, ipServer);
		InetAddress oo = InetAddress.getLocalHost();
		System.out.println(oo.getHostAddress());
		Thread recive = new Thread() { // this thread to receive any data from server or client
			public void run() {
				try {
					Client.recive();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread send = new Thread() { // this thread to prove attendance to server
			public void run() {
				try {
					Client.sendMessageActive();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		recive.start();
		send.start();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 800, 600);
		MessageOnScreen = new TextArea();
		username = new TextField();
		username.setPrefSize(200, 30);
		message = new TextField();
		message.setPrefSize(300, 60);
		UsersActive = new Button("Users");
		UsersActive.setPrefSize(60, 60);
		RadioButton public_ = new RadioButton("public");

		HBox h2 = new HBox(10);
		client_Label = new Label("User Name :  ");

		h2.getChildren().addAll(client_Label, username, public_);

		root.setTop(h2);
		root.setCenter(MessageOnScreen);
		root.setRight(UsersActive);
		HBox h = new HBox(10);
		h.getChildren().addAll(message);
		root.setBottom(h);
		Main.MessageOnScreen.setFont(new Font(20));
		primaryStage.setScene(scene);
		primaryStage.show();

		/////////////////////////////////////////////////////////////////////////////
		username.setOnAction(e -> {
			username.setDisable(true);

			try {
				Client.send("", "A" + username.getText(), 1); // A that mean add user
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		message.setOnAction(e -> {
			try {
				if (public_.isSelected()) {
					Client.send(Main.username.getText(), message.getText(), 0);

				} else {
					Client.send(Main.username.getText(), message.getText(), 2);
					Main.MessageOnScreen.appendText("ME : " + message.getText() + "\n");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		UsersActive.setOnAction(e -> {
			Stage s = new Stage();
			selectUserName = new TextField();
			VBox v = new VBox(10);
			selectUserName.setFont(new Font(30));
			v.getChildren().addAll(ShowAllUsers, selectUserName);
			s.setScene(new Scene(v, 500, 500));
			s.show();
			selectUserName.setOnAction(e2 -> {
				try {
					Client.send("", "@" + selectUserName.getText(), 1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // A that mean add user

			});

		});

	}

	public static void main(String[] args) {
		launch(args);

	}
}
