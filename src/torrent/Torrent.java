package torrent;

import http.TrackerInfo;

import java.io.File;
import java.util.ArrayList;

import torrent.peer.*;

public class Torrent {
	private ArrayList<Peer> peerList;
	private TrackerInfo[] trackers;
	private Metainfo metainfo;
	private int numPort;

	public Torrent(File metainfo, int numPort) {
		this.metainfo = new Metainfo(metainfo);
		this.numPort = numPort;
		this.peerList = new ArrayList<Peer>();
	}

	public Torrent(File metainfo) {
		this.metainfo = new Metainfo(metainfo);
		this.numPort = 6881 + (int) (Math.random() * 30001);
	}

	public void massAnnounce() {
		ArrayList<String> trackersUrl = metainfo.getTrackerList();
		this.trackers = new TrackerInfo[trackersUrl.size()];
		for (int i = 0; i < trackers.length; i++) {
			trackers[i] = new TrackerInfo(trackersUrl.get(i));
			trackers[i].announce(metainfo.getInfoHash(), metainfo.getSize(),
					"<?>", "started", this.numPort);
			ArrayList<Peer> peers = trackers[i].getPeersList();
			for (int j = 0; j < peers.size(); j++) {
				if (!this.peerList.contains(peers.get(j))) {
					peerList.add(peers.get(j));
				}
			}
		}

	}

	
	public Metainfo getMetainfo() {
		return metainfo;
	}
}
