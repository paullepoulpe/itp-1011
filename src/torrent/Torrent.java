package torrent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JProgressBar;

import torrent.peer.*;
import torrent.piece.*;
import torrent.tracker.TrackerInfo;

/*
 * IMPLEMENTER LA METHODE Torrent.stop() !!!!!!*/
public class Torrent {
	private PeerManager peerManager;
	// private Piece[] pieces;
	private ArrayList<TrackerInfo> trackers;
	private Metainfo metainfo;
	private int numPort;
	private PieceManager pieceManager;
	public static String PEER_ID = PeerIDGenerator.generateID();
	private long lastBlockReceived, downloadSpeed, uploadSpeed;
	private JProgressBar progressBar;

	/**
	 * comstructeur avec numero de port
	 * 
	 * @param metainfoFile
	 *            le fichier .torrent
	 * 
	 * @param numPort
	 *            le port sur lequel on accepte les connections
	 */
	public Torrent(File metainfoFile, int numPort) {
		this.metainfo = new Metainfo(metainfoFile);
		this.numPort = numPort;
		this.pieceManager = new PieceManager(this);
		peerManager = new PeerManager(this);
		peerManager.start();
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);

		System.out.println(this.metainfo);
	}

	/**
	 * constructeur sans numero de port, appele l'autre constructeur avec un
	 * numero de port aleatoire entre 6881 et 36881
	 * 
	 * @param metainfo
	 *            le fichier .torrent
	 */
	public Torrent(File metainfo) {
		this(metainfo, 6881 + (int) (Math.random() * 30001));

	}

	/**
	 * fais les premieres requetes aux trackers et demarre le @PeerAccepter
	 */
	public void massAnnounce() {
		ArrayList<String> trackersUrl = metainfo.getTrackerList();
		this.trackers = new ArrayList<TrackerInfo>();
		for (int i = 0; i < trackersUrl.size(); i++) {
			trackers.add(new TrackerInfo(trackersUrl.get(i), this));
			trackers.get(i).start();
		}
		new PeerAccepter(this.numPort, this);

	}

	/**
	 * Permet d'ajouter un peer au torrent
	 * 
	 * @param peer
	 *            le peer qu'on veut ajouter
	 * @return false si on l'as deja
	 */
	public void addPeer(Peer peer) {
		peerManager.addPeer(peer);
	}

	public int getNumPort() {
		return numPort;
	}

	public Metainfo getMetainfo() {
		return metainfo;
	}

	public ArrayList<TrackerInfo> getTrackers() {
		return trackers;
	}

	public PieceManager getPieceManager() {
		return pieceManager;
	}

	public void addPeer(Socket socket) {
		peerManager.addPeer(socket);
	}

	public long getUpload() {
		return this.uploadSpeed;
	}

	public void setBlocsReceived() {
		long currentTime = System.currentTimeMillis();
		this.downloadSpeed = (Piece.BLOCK_SIZE)
				/ ((currentTime - lastBlockReceived) * 1000);
		this.lastBlockReceived = System.currentTimeMillis();
	}

	public long getDownload() {
		return downloadSpeed;
	}

	public JProgressBar getProgressBar() {
		progressBar.setValue((int) pieceManager.getDownloadedCompleteness());
		return progressBar;
	}

	public void notifyPeerHandlers(int index) {
		peerManager.notifyPeerHandlers(index);

	}
}
