package torrent.peer;

import java.net.InetAddress;
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
	private InetAddress ipAdress;
	private int port;
	private String id = "<?>";
	private double notation = 5;

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
		port = (int) (((data[4] & 0xff) << 8) + (data[5] & 0xff));
	}

	public Peer(InetAddress ipAdress, int port) {
		this.ipAdress = ipAdress;
		this.port = port;
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

	public double getNotation() {
		return notation;
	}

	public void multiplyNotation(double d) {
		notation *= d;
		if (notation < 0) {
			notation = 0;
		} else if (notation > 10) {
			notation = 10;
		}
		// System.err.println("Notation :" + notation);

	}

}
