import java.net.InetAddress;

public class User {
	private String id_User;
	private InetAddress IP_User;
	private int PORT;

	public User(String id_User, InetAddress iP_User, int pORT) {
		super();
		this.id_User = id_User;
		IP_User = iP_User;
		PORT = pORT;
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

}