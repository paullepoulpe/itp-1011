package torrent.peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import torrent.Torrent;

/**
 * Cette classe represente un pair. Elle implemente les methodes necessaires
 * pour changer ses attributs. Le systeme de notations est utile pour optimiser
 * les requetes aux "meilleurs pairs".
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
	 * Constructeur.
	 * 
	 * @param data
	 *            tableau de 6 bytes contenant l'adresse IP et le port du pair
	 *            auquel on souhaiterait se connecter.
	 * @param torrent
	 *            le torrent pour lequel ce pair est un seeder/leecher.
	 */
	public Peer(byte[] data, Torrent torrent) {
		byte[] ip = { data[0], data[1], data[2], data[3] };
		try {
			this.ipAdress = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			System.out.println(e.getLocalizedMessage());
		}
		port = ((data[4] & 0xff) << 8) + (data[5] & 0xff);
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

	/**
	 * Cette methoe permet de changer la notation du pair. Elle est appelee
	 * lorsqu'on recoit des messages
	 * 
	 * @param d
	 *            facteur qui multiplie la notaion.
	 */
	public void multiplyNotation(double d) {
		notation *= d;
		if (notation < 0) {
			notation = 0;
		} else if (notation > 10) {
			notation = 10;
		}
	}

}
