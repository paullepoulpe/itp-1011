package torrent;

import http.TrackerInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import torrent.peer.*;
import torrent.piece.*;

public class Torrent {
	private ArrayList<Peer> peerList;
	private Piece[] pieces;
	private TrackerInfo[] trackers;
	private Metainfo metainfo;
	private int numPort;

	public Torrent(File metainfo, int numPort) {
		this.metainfo = new Metainfo(metainfo);
		this.numPort = numPort;
		this.peerList = new ArrayList<Peer>();
		this.pieces = new Piece[(int) Math.ceil(((double) this.metainfo.getSize())
				/ ((double) this.metainfo.getPieceLength()))];
		for(int i = 0;i<this.pieces.length;i++){
			byte[] pieceHash = new byte[20];
			for(int j=0;j<pieceHash.length;j++){
			pieceHash[j]=this.metainfo.getPiecesHash()[(20*i)+j];	
			}
			pieces[i]= new Piece(i,(byte)this.metainfo.getPieceLength(),pieceHash);
		}
		System.out.println(this.metainfo);
	}

	public Torrent(File metainfo) {
		this.metainfo = new Metainfo(metainfo);
		this.numPort = 6881 + (int) (Math.random() * 30001);
		this.peerList = new ArrayList<Peer>();
		System.out.println(this.metainfo);
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
		System.out.println("\nTous les pairs initialisés :\n\n");
		for (int i = 0; i < this.peerList.size(); i++) {
			System.out.println(peerList.get(i));
		}

	}

	public Metainfo getMetainfo() {
		return metainfo;
	}
	
	public
	
	
}
