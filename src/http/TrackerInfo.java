package http;

import java.util.ArrayList;
import java.util.Iterator;

import torrent.peer.*;

/**
 * Cette classe contient les informations relative a un certain tracker, il y a
 * autant d'objets TrackerInfo que de trackers
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class TrackerInfo {
	private String urlAnnounce;
	private AnnounceInfo info;
	private ArrayList<Peer> peersList;

	/**
	 * Cree un objet Tracker info parametrisee par l'adresse url du tracker
	 * 
	 * @param urlAnnounce
	 *            Un String contenant l'adresse url du tracker
	 */
	public TrackerInfo(String urlAnnounce) {
		this.urlAnnounce = urlAnnounce;
	}

	/**
	 * 
	 * @param infoHash
	 *            une signature digitale du fichier Metainfo produite par
	 *            l'algorithme SHA1. Cette signature sur 20 bytes doit etre
	 *            url-encodee
	 * @param left
	 *            le nombres de bytes que le client doit encore telecharger
	 *            (code en base 10 ASCII)
	 * @param trackerId
	 *            si le tracker a renvoye un trackerid lors d’une precedente
	 *            requete, il doit etre renvoye ici (par mesure de securite)
	 * @param event
	 *            soit started, stopped, ou completed. La première requete
	 *            doit inclure l evenement started
	 * @param port
	 *            numero du port sur lequel le client accepte des connexions de
	 *            pairs
	 * @return un objet AnnounceInfo relatif a ce tracker
	 */
	public AnnounceInfo announce(byte[] infoHash, int left, String trackerId,
			String event, int port) {
		HTTPGet query = new HTTPGet(urlAnnounce, infoHash, left, trackerId,
				event, port);
		this.info = new AnnounceInfo(query.get());
		this.peersList = new ArrayList<Peer>();
		initPeers();

		return null;
	}

	/**
	 * Initialise la liste de pairs a partir du tableau de pairs sous forme
	 * compacte qui se trouve dans l'objet announce info
	 */
	private void initPeers() {
		System.out.println("Initialisation des pairs ...\n");
		byte[] peers = info.getPeers();
		if (peers.length % 6 != 0) {
			System.out.println("Erreur taille tableau de pairs");
		}
		for (int i = 0; i < peers.length; i = i + 6) {
			byte[] data = new byte[6];
			for (int j = 0; j < 6; j++) {
				data[j] = peers[i + j];
			}
			this.peersList.add(new Peer(data));

		}
	}

	/**
	 * Imprime de maniere lisible(human readable) les informations concernant
	 * cette classe
	 */
	public String toString() {
		String toString = "Tracker : " + urlAnnounce + "\n\n" + "Info : \n"
				+ info.toString() + "\n\nPeers : \n\n";
		Iterator<Peer> i = this.peersList.iterator();
		while (i.hasNext()) {
			toString = toString.concat(i.next().toString());
		}
		return toString;
	}

	/**
	 * 
	 * @return la liste de pairs recences par ce tracker
	 */
	public ArrayList<Peer> getPeersList() {
		return peersList;
	}
}
