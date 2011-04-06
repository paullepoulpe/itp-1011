package torrent.peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import torrent.Torrent;
import torrent.piece.PieceManager;

public class Peer {
	private PeerHandler peerHandler;
	private InetAddress ipAdress;
	private int port;
	private String id;

	public Peer(byte[] data, Torrent torrent) {
		byte[] ip = { data[0], data[1], data[2], data[3] };
		try {
			this.ipAdress = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			System.out.println(e.getLocalizedMessage());
		}
		port = (int) (((data[4] << 8) + data[5]) & 0xffff);

		this.id = "<?>";
		this.peerHandler = new PeerHandler(this, torrent);
		peerHandler.start();

	}

	public String toString() {
		return "IpAdress : " + ipAdress + "\n" + "Port : " + port + "\n";
	}

	public void setId(String id) {
		this.id = id;
	}

	public InetAddress getIpAdress() {
		return ipAdress;
	}

	public int getPort() {
		return port;
	}

	public String getId() {
		return id;
	}

	public boolean equals(Peer peer2) {
		return (this.ipAdress.equals(peer2.ipAdress))
				&& (this.port == peer2.port);
	}

	public void runPeerHandler() {
		peerHandler.start();
	}
}
