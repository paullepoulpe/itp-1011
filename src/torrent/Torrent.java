package torrent;

import http.TrackerInfo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
		this.pieces = new Piece[(int) Math.ceil(((double) this.metainfo
				.getSize()) / ((double) this.metainfo.getPieceLength()))];
		for (int i = 0; i < this.pieces.length; i++) {
			byte[] pieceHash = new byte[20];
			for (int j = 0; j < pieceHash.length; j++) {
				pieceHash[j] = this.metainfo.getPiecesHash()[(20 * i) + j];
			}
			pieces[i] = new Piece(i, (byte) this.metainfo.getPieceLength(),
					pieceHash);
		}
		System.out.println(this.metainfo);
	}

	public Torrent(File metainfo) {
		this(metainfo, 6881 + (int) (Math.random() * 30001));

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
public boolean isComplete(){
	boolean complet=true;
	for (int i=0;i<this.pieces.length;i++){
		complet=complet && this.pieces[i].isComplete() ;
	}
	return complet;
}
	public Metainfo getMetainfo() {
		return metainfo;
	}

	public boolean readFromFile() {
		File folder = new File(System.getProperty("user.home"), "Downloads"
				+ File.separator);
		String[] liste = folder.list();
		boolean trouvé = false;
		int indexFichier = -1;
		for (int i = 0; i < liste.length && !trouvé; i++) {
			System.out.println(liste[i]);
			if (liste[i].contains(metainfo.getFileName())) {
				trouvé = true;
				indexFichier = i;
			}
		}
		if (trouvé) {
			File file = new File(System.getProperty("user.home"), "Downloads"
					+ File.separator + liste[indexFichier]);
			DataInputStream lecteur = null;
			try {
				lecteur = new DataInputStream(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < pieces.length; i++) {
				byte[] read = new byte[this.metainfo.getPieceLength()];

				try {
					lecteur.read(read);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		} else {
			System.out.println("File not found!");
			return false;
		}
	}
}
