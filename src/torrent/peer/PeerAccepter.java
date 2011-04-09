package torrent.peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import torrent.Torrent;

public class PeerAccepter extends Thread {
	private int port;
	private Torrent torrent;

	public PeerAccepter(int port, Torrent torrent) {
		this.port = port;
		this.torrent = torrent;
	}

	public void run() {
		ServerSocket s = null;
		try {
			s = new ServerSocket(port);

			System.out.println(s.getLocalPort());
			Socket socket = s.accept();
			System.out.println("Connexion entrante");
			Peer peer = new Peer(socket, torrent);
			torrent.addPeer(peer);
			new PeerAccepter(port, torrent).start();
			this.interrupt();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
