package torrent.peer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import torrent.Torrent;

/**
 * Cette classe sert a traiter les connexions entrantes. Elle est censee tourner
 * en tant que thread a part entiere.
 * 
 * @author Damien Engels, Maarten Sap
 * 
 */
public class PeerAccepter extends Thread {
	private int port;
	private Torrent torrent;

	public PeerAccepter(int port, Torrent torrent) {
		this.port = port;
		this.torrent = torrent;
	}

	/**
	 * Cette methode cree un nouveau pair depuis les informations recues dans le
	 * constucteur. On ajoute le peer a la liste des peers du torrent concerne,
	 * puis on demarre le PeerHandler correspondant. Finalement, on arrete
	 * l'instance presente de l'ecouteur de connexions entrantes.
	 */
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
