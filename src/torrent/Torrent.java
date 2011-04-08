package torrent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import torrent.peer.Peer;
import torrent.peer.PeerAccepter;
import torrent.peer.PeerIDGenerator;
import torrent.piece.*;
import torrent.tracker.TrackerInfo;

public class Torrent {
	private ArrayList<Peer> peerList;
	private Piece[] pieces;
	private ArrayList<TrackerInfo> trackers;
	private Metainfo metainfo;
	private int numPort;
	private PieceManager pieceManager;
	public static String PEER_ID = PeerIDGenerator.generateID();
	private boolean writtenOnFile;

	public Torrent(File metainfo, int numPort) {
		this.metainfo = new Metainfo(metainfo);
		this.numPort = numPort;
		this.peerList = new ArrayList<Peer>();
		System.out.println(this.metainfo);
		this.pieces = new Piece[(int) (Math.ceil(((double) this.metainfo
				.getSize())
				/ ((double) this.metainfo.getPieceLength())))];
		for (int i = 0; i < this.pieces.length; i++) {
			byte[] pieceHash = new byte[20];
			for (int j = 0; j < pieceHash.length; j++) {
				pieceHash[j] = this.metainfo.getPiecesHash()[(20 * i) + j];
			}
			if (i == pieces.length - 1) {
				int length = this.metainfo.getSize()
						- ((pieces.length - 1) * this.metainfo.getPieceLength());
				pieces[i] = new Piece(i, length, pieceHash);
			} else {
				pieces[i] = new Piece(i, this.metainfo.getPieceLength(),
						pieceHash);
			}

		}
		this.pieceManager = new PieceManager(this);
	}

	public Torrent(File metainfo) {
		this(metainfo, 6881 + (int) (Math.random() * 30001));

	}

	public void massAnnounce() {
		ArrayList<String> trackersUrl = metainfo.getTrackerList();
		this.trackers = new ArrayList<TrackerInfo>();
		for (int i = 0; i < trackersUrl.size(); i++) {
			trackers.add(new TrackerInfo(trackersUrl.get(i), this));
			trackers.get(i).start();
		}
		new PeerAccepter(this.numPort, this);

	}

	public int getDownloadedCompleteness() {
		int downloadedCompleteness = 0;
		for (int i = 0; i < this.pieces.length; i++) {
			downloadedCompleteness += downloadedCompleteness
					+ this.pieces[i].getDownloadCompleteness();
		}
		return downloadedCompleteness / this.pieces.length;
	}

	public boolean isEmpty() {
		boolean vide = true;
		for (int i = 0; i < this.pieces.length; i++) {
			vide = vide && !this.pieces[i].isComplete();
		}
		return vide;
	}

	public boolean isComplete() {
		boolean complet = true;
		for (int i = 0; i < this.pieces.length; i++) {
			complet = complet && this.pieces[i].isComplete();
		}
		return complet;
	}

	public boolean addPeer(Peer peer) {
		if (peerList.contains(peer)) {
			return false;
		} else {
			peerList.add(peer);
			System.out.println("Nouveau pair : " + peer);
			return true;
		}
	}

	public boolean writeToFile() {
		if (isComplete() && !writtenOnFile) {
			File file = new File(System.getProperty("user.home"), "Downloads"
					+ File.separator + this.numPort + "_suffix_"
					+ metainfo.getFileName());

			if (this.isComplete()) {
				DataOutputStream lecteur = null;
				try {
					lecteur = new DataOutputStream(new FileOutputStream(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < pieces.length; i++) {
					if (pieces[i].getData() == null) {
						try {
							for (int j = 0; j < pieces[i].getSizeTab(); i++) {
								lecteur.write(0);
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							lecteur.write(pieces[i].getData());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
				try {
					lecteur.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				writtenOnFile = true;
				System.out.println("Fichier ecrit dans "
						+ System.getProperty("user.home") + "Downloads");
				System.exit(0);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Cette methode permet d'initialiser les pieces d'un torrent depuis un
	 * fichier. Elle regarde dans le dossier Downloads/ si un fichier contient
	 * le nom du fichier contenu dans metainfo, il le lit et essaye
	 * d'initialiser les pièces avec. La piece s'occupe de verifier qu'elle soit
	 * correcte. Si le fichier n'est pas trouve cette metode retourne false
	 * 
	 * @return true si le fichier a ete trouve, false sinon
	 */
	public boolean readFromFile() {
		File folder = new File(System.getProperty("user.home"), "Downloads"
				+ File.separator);
		String[] liste = folder.list();
		boolean trouve = false;
		int indexFichier = -1;
		for (int i = 0; i < liste.length && !trouve; i++) {
			System.out.println(liste[i]);
			if (liste[i].contains(metainfo.getFileName())) {
				trouve = true;
				indexFichier = i;
			}
		}
		if (trouve) {
			File file = new File(System.getProperty("user.home"), "Downloads"
					+ File.separator + liste[indexFichier]);
			DataInputStream lecteur = null;
			try {
				lecteur = new DataInputStream(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] read;
			for (int i = 0; i < pieces.length; i++) {

				read = new byte[this.pieces[i].getSizeTab()];

				try {
					lecteur.read(read);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.pieces[i].setData(read);
			}
			try {
				lecteur.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writtenOnFile = true;
			return true;

		} else {
			System.out.println("File not found!");
			return false;
		}
	}

	public int getNumPort() {
		return numPort;
	}

	public Piece[] getPieces() {
		return pieces;
	}

	public Metainfo getMetainfo() {
		return metainfo;
	}

	public PieceManager getPieceManager() {
		return pieceManager;
	}
}
