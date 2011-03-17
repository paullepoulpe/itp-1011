package torrent.peer;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Peer {
	private InetAddress ipAdress;
	private int port;
	private String id;

	public Peer(byte[] data) {
		byte[] ip = { data[0], data[1], data[2], data[3] };
		try {
			this.ipAdress = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			System.out.println(e.getLocalizedMessage());
		}
		port = (data[4] << 8) + data[5];
		this.id = "<?>";

	}

	public String toString() {
		return "IpAdress : " + ipAdress + "\n" + "Port : " + port + "\n\n";
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
