/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package http;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import torrent.peer.*;

public class TrackerInfo {
	private String urlAnnounce;
	private AnnounceInfo info;
	private List<Peer> peersList;

	public TrackerInfo(String urlAnnounce) {
		this.urlAnnounce = urlAnnounce;
	}

	public AnnounceInfo announce(File metainfo) {
		HTTPGet query = new HTTPGet(urlAnnounce, metainfo);
		this.info = new AnnounceInfo(query.get());
		this.peersList = new ArrayList<Peer>();
		initPeers();

		return null;
	}

	private void initPeers() {
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

	public String toString() {
		String toString = "Tracker : " + urlAnnounce + "\n\n" + "Info : \n"
				+ info.toString() + "\n\nPeers : \n\n";
		Iterator<Peer> i = this.peersList.iterator();
		while (i.hasNext()) {
			toString = toString.concat(i.next().toString());
		}
		return toString;
	}
}
