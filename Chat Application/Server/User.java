package application;

import java.net.InetAddress;

public class User {
private String id_User ; // contain user name 
private InetAddress IP_User ; // contain IP Address for user
private int PORT ;// contain IP Address for user
public User(String id_User, InetAddress iP_User, int pORT) { // Generate new User
	this.id_User = id_User;
	this.IP_User = iP_User;
	this.PORT = pORT;
}
public String getId_User() {
	return id_User;
}
public void setId_User(String id_User) {
	this.id_User = id_User;
}
public InetAddress getIP_User() {
	return IP_User;
}
public void setIP_User(InetAddress iP_User) {
	IP_User = iP_User;
}
public int getPORT() {
	return PORT;
}
public void setPORT(int pORT) {
	PORT = pORT;
}
@Override
public String toString() {
	return "User [id_User=" + id_User + ", IP_User=" + IP_User + ", PORT=" + PORT + "]";
}



}