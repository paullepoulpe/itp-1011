package torrent.peer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import torrent.*;

public class Peer {
	private InetAddress ipAdress;
	private int port;
	private String id = "<?>";
	private Torrent torrent;

	public Peer(byte[] data) {
		byte[] ip = { data[0], data[1], data[2], data[3] };
		try {
			this.ipAdress = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			System.out.println(e.getLocalizedMessage());
		}
		port = (data[4] << 8) + data[5];

	}

	public String toString() {
		return "IpAdress : " + ipAdress + "\n" + "Port : " + port + "\n\n";
	}
}
