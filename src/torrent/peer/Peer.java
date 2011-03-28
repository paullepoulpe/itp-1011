package torrent.peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Peer {
	private InetAddress ipAdress;
	private int port;
	private byte[] id;

	public Peer(byte[] data) {
		byte[] ip = { data[0], data[1], data[2], data[3] };
		try {
			this.ipAdress = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			System.out.println(e.getLocalizedMessage());
		}
		port = (int) (((data[4] << 8) + data[5]) & 0xffff);

		this.id = "<?>".getBytes();

	}

	public String toString() {
		return "IpAdress : " + ipAdress + "\n" + "Port : " + port + "\n";
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public byte[] getId() {
		return id;
	}
	
	public InetAddress getIPAddress(){
		return this.ipAdress;
	}

	public boolean equals(Peer peer2) {
		return (this.ipAdress.equals(peer2.ipAdress))
				&& (this.port == peer2.port);
	}
}
