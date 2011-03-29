package torrent.tracker;

import http.*;
import java.util.ArrayList;
import java.util.Iterator;

import torrent.Torrent;
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
	private Torrent torrent;
	private AnnounceInfo info;
	private ArrayList<Peer> peersList;

	/**
	 * Cree un objet Tracker info parametrisee par l'adresse url du tracker
	 * 
	 * @param urlAnnounce
	 *            Un String contenant l'adresse url du tracker
	 * 
	 * @param torrent
	 *            le torrent associé
	 */
	public TrackerInfo(String urlAnnounce, Torrent torrent) {
		this.urlAnnounce = urlAnnounce;
		this.torrent = torrent;
	}

	/**
	 * Fait l'announce au tracker
	 */
	public void announce() {
		HTTPGet query = new HTTPGet(urlAnnounce);
		query
				.add("info_hash", torrent.getMetainfo().getInfoHash()
						.urlEncoded());
		query.add("peer_id", Torrent.PEER_ID);
		query.add("port", torrent.getNumPort() + "");
		query.add("compact", "1");
		query.add("numwamt", "50");
		query.add("left", torrent.getMetainfo().getSize() + "");
		query.add("event", "started");

		this.info = new AnnounceInfo(query.get());

		this.peersList = new ArrayList<Peer>();
		initPeers();

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

	public String getUrlAnnounce() {
		return urlAnnounce;
	}
}
