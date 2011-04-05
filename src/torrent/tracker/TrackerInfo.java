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
public class TrackerInfo extends Thread {
	private String urlAnnounce;
	private Torrent torrent;
	private AnnounceInfo info;
	private ArrayList<Peer> peersList;
	private boolean failed;

	/**
	 * Cree un objet Tracker info parametrisee par l'adresse url du tracker
	 * 
	 * @param urlAnnounce
	 *            Un String contenant l'adresse url du tracker
	 */
	public TrackerInfo(String urlAnnounce, Torrent torrent) {
		this.urlAnnounce = urlAnnounce;
		this.torrent = torrent;
		failed = false;
	}

	public void run() {
		try {
			announce();
		} catch (FailureReasonExeption e) {
			failed = true;
			System.out.println("Connection failed (" + urlAnnounce + ") : "
					+ e.getFailureReason());
		}

	}

	public void announce() throws FailureReasonExeption {
		System.out.println("\nRequete à " + urlAnnounce + "...");
		HTTPGet query = new HTTPGet(urlAnnounce);
		query
				.add("info_hash", torrent.getMetainfo().getInfoHash()
						.urlEncoded());
		query.add("peer_id", Torrent.PEER_ID);
		query.add("port", torrent.getNumPort() + "");
		query.add("uploaded", "0");
		query.add("downloaded", "0");
		query.add("left", torrent.getMetainfo().getSize() + "");
		query.add("compact", "1");
		query.add("event", "started");
		query.add("numwant", "50");
		try {
			this.info = new AnnounceInfo(query.get());
		} catch (FailureReasonExeption e) {
			throw e;
		}
		initPeers();

	}

	/**
	 * Initialise la liste de pairs a partir du tableau de pairs sous forme
	 * compacte qui se trouve dans l'objet announce info
	 */
	private void initPeers() {
		this.peersList = new ArrayList<Peer>();
		byte[] peers = info.getPeers();
		if (peers.length % 6 != 0) {
			System.out.println("Erreur taille tableau de pairs");
		}
		for (int i = 0; i < peers.length; i = i + 6) {
			byte[] data = new byte[6];
			for (int j = 0; j < 6; j++) {
				data[j] = peers[i + j];
			}
			Peer peer = new Peer(data, torrent);

			this.peersList.add(peer);
			synchronized (torrent) {
				torrent.addPeer(peer);
			}

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
