package torrent;

import gui.DynamicFlowLabel;

import java.awt.Color;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import settings.GeneralSettings;
import torrent.peer.*;
import torrent.piece.*;
import torrent.tracker.TrackerInfo;

public class Torrent {
	public static final int STOPPED = 0, PAUSED = 1, STARTED = 2;
	private PeerManager peerManager;
	private ArrayList<TrackerInfo> trackers;
	private Metainfo metainfo;
	private int numPort, downloadingStatus;
	private PieceManager pieceManager;
	public static String PEER_ID = PeerIDGenerator.generateID();
	private JProgressBar progressBar;
	private DynamicFlowLabel upload = new DynamicFlowLabel(),
			download = new DynamicFlowLabel();

	/**
	 * constructeur avec numero de port. Il demarre le {@link PeerManager} ainsi
	 * que le {@link PeerAccepter} pour gerer les pairs
	 * 
	 * @param metainfoFile
	 *            le fichier .torrent
	 * 
	 * @param numPort
	 *            le port sur lequel on accepte les connections
	 * @throws InvalidFileException
	 */
	public Torrent(File metainfoFile, int numPort) throws InvalidFileException {
		System.out.println("Notre Port : " + numPort);
		this.metainfo = new Metainfo(metainfoFile);
		this.numPort = numPort;
		this.pieceManager = new PieceManager(this);
		peerManager = new PeerManager(this);
		peerManager.start();
		this.downloadingStatus = STOPPED;
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		System.out.println(this.metainfo);
		new PeerAccepter(this.numPort, this).start();
	}

	/**
	 * constructeur sans numero de port, appele l'autre constructeur avec un
	 * numero de port aleatoire entre 6881 et 36881
	 * 
	 * @param metainfo
	 *            le fichier .torrent
	 * @throws InvalidFileException
	 */
	public Torrent(File metainfo) throws InvalidFileException {
		this(metainfo, 6881 + (int) (Math.random() * 30001));
	}

	/**
	 * Cette methode fait les premieres requetes aux trackers par des objets
	 * {@link TrackerInfo}
	 */
	public void massAnnounce() {
		this.downloadingStatus = STARTED;
		ArrayList<String> trackersUrl = metainfo.getTrackerList();
		this.trackers = new ArrayList<TrackerInfo>();
		for (int i = 0; i < trackersUrl.size(); i++) {
			trackers.add(new TrackerInfo(trackersUrl.get(i), this));
			trackers.get(i).start();
		}
	}

	/**
	 * Permet d'ajouter un peer au torrent
	 * 
	 * @param peer
	 *            le peer qu'on veut ajouter
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

	/**
	 * Permet d'ajouter un pair depuis un socket
	 * 
	 * @param socket
	 *            le socket representant la connexion au pair
	 */
	public void addPeer(Socket socket) {
		peerManager.addPeer(socket);
	}

	/**
	 * Permet d'avoir la JProgressBar de notre torrent. On s'assure que la
	 * valeur soit la bonne par un setValue
	 * 
	 * @return la progressBar de notre {@link Torrent} ({@link JProgressBar})
	 */
	public JProgressBar getProgressBar() {
		progressBar.setValue((int) pieceManager.getDownloadedCompleteness());
		return progressBar;
	}

	public void notifyPeerHandlers(int index) {
		peerManager.notifyPeerHandlers(index);
	}

	public void stop() {
		downloadingStatus = STOPPED;
		peerManager.interrupt();
	}

	public void receivedBlock() {
		download.add(Piece.BLOCK_SIZE);
	}

	public void sentBlock() {
		upload.add(Piece.BLOCK_SIZE);
	}

	/**
	 * @deprecated Cette methode devait mettre en pause le telechargment;
	 *             utiliser {@link Torrent#stop} a la place.
	 */
	public void pause() {
		System.out.println("Torrent Paused (Torrent.pause())");
		try {
			peerManager.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @deprecated Tout comme {@link Torrent#pause()}, methode a ne pas
	 *             utiliser.
	 */
	public void unPause() {
		System.out.println("Torrent.unpause()");
		peerManager.notify();
	}

	public int getDownloadingStatus() {
		return downloadingStatus;
	}

	public DynamicFlowLabel getDownload() {
		return download;
	}

	public DynamicFlowLabel getUpload() {
		return upload;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Torrent) {
			return metainfo.getInfoHash().equals(
					((Torrent) obj).metainfo.getInfoHash());
		}
		return false;
	}

	public File getDownloadinFolder() {
		return pieceManager.getDownloadingFolder();
	}

	public ArrayList<PeerHandler> getConnectedPeers() {
		return peerManager.getConnectedPeers();
	}

}
