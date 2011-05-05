package torrent.peer;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import torrent.Torrent;

/**
 * Cette classe represente un pair. Elle implemente les methodes necessaires
 * pour changer ses attributs ainsi que pour activer son PeerHandler.
 * 
 * @author Damien Engels, Maarten Sap
 * 
 */
public class Peer {
	private PeerHandler peerHandler;
	private InetAddress ipAdress;
	private int port;
	private String id;
	private double notation;

	/**
	 * Constructeur. On demarrer (start) le processus PeerHandler, car c'est
	 * cette classe qui s'occupe de tout ce qui est traffic de donnees.
	 * 
	 * @param data
	 *            tableau de 6 bytes contenant l'adresse IP et le port du pair
	 *            auquel on souhaiterait se connecter.
	 * @param torrent
	 *            on passe le torrent en argument pour pouvoir construire un
	 *            PeerHandler. C'est lui qui se chargera de tout une fois qu'il
	 *            est demarre.
	 */
	public Peer(byte[] data, Torrent torrent) {
		byte[] ip = { data[0], data[1], data[2], data[3] };
		try {
			this.ipAdress = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			System.out.println(e.getLocalizedMessage());
		}
		port = (int) (((data[4] << 8) + data[5]) & 0xffff);

		this.id = "<?>";
		notation = 5;
		// this.peerHandler = new PeerHandler(this, torrent);
		// peerHandler.start();

	}

	/**
	 * Second constructeur, utile pour construire un paiir a partir d'un Socket
	 * et d'un torrent. Cette methode ne fait que creer un PeerHandler avec ses
	 * deux arguments puis de demarrer ce dernier.
	 * 
	 * @param socket
	 *            afin de pouvoir se connecter au pair, on a besoin d'un socket.
	 * @param torrent
	 *            contient les informations necessaires au telechargement au
	 *            niveau pieces et block.
	 */
	public Peer(Socket socket, Torrent torrent) {
		this.peerHandler = new PeerHandler(socket, torrent);
		peerHandler.run();
	}

	public String toString() {
		return "IpAdress : " + ipAdress + "\t" + "Port : " + port;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setId(byte[] id) {
		char[] idChar = new char[id.length];
		for (int i = 0; i < id.length; i++) {
			idChar[i] = (char) id[i];
		}
		this.id = String.valueOf(idChar);
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

	public boolean equals(Object peer) {
		if (peer instanceof Peer) {
			return (this.ipAdress.equals(((Peer) peer).ipAdress))
					&& (this.port == ((Peer) peer).port);
		}
		return false;

	}

	public void runPeerHandler() {
		peerHandler.start();
	}

	public void setPort(int port) {
		this.port = port;

	}

	public void setInet(InetAddress inetAddress) {
		this.ipAdress = inetAddress;
	}

	public double getNotation() {
		return notation;
	}
}
