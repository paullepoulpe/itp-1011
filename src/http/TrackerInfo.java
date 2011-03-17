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
	 * Crée un objet Tracker info paramétrisé par l'adresse url du tracker
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
	 *            l'algorithme SHA1. Cette signature sur 20 bytes doit être
	 *            url-encodée
	 * @param left
	 *            le nombres de bytes que le client doit encore télécharger
	 *            (codé en base 10 ASCII)
	 * @param trackerId
	 *            si le tracker a renvoyé un trackerid lors d’une précédente
	 *            requête, il doit être renvoyé ici (par mesure de sécurité)
	 * @param event
	 *            soit “started”, “stopped”, ou “completed”. La première requête
	 *            doit inclure l’évènement “started”
	 * @param port
	 *            numéro du port sur lequel le client accepte des connexions de
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
	 * Imprime de manière lisible(human readable) les informations concernant
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
	 * @return la liste de pairs recencés par ce tracker
	 */
	public ArrayList<Peer> getPeersList() {
		return peersList;
	}
}
