package torrent;

import torrent.tracker.TrackerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
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
	private PieceManager pieceManager;

	public Torrent(File metainfo, int numPort) {
		this.metainfo = new Metainfo(metainfo);
		this.numPort = numPort;
		this.peerList = new ArrayList<Peer>();
		this.pieces = new Piece[(int) Math.ceil(((double) this.metainfo
				.getSize())
				/ ((double) this.metainfo.getPieceLength()))];
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
		// this.pieceManager = new PieceManager(this);
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
		System.out.println("\nTous les pairs initialises :\n\n");
		for (int i = 0; i < this.peerList.size(); i++) {
			System.out.println(peerList.get(i));
		}

	}

	public int getCompleteness() {
		int completeness = 0;
		for (int i = 0; i < pieces.length; i++) {
			completeness += pieces[i].getDownloadCompleteness();
		}
		return completeness / pieces.length;

	}

	public boolean isComplete() {
		boolean complet = true;
		for (int i = 0; i < this.pieces.length; i++) {
			complet = complet && this.pieces[i].isComplete();
		}
		return complet;
	}

	public Piece[] getPieces() {
		return pieces;
	}

	public boolean writeToFile() {
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
							lecteur.writeByte(0);
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
			return true;

		} else {
			return false;
		}

	}

	public Metainfo getMetainfo() {
		return metainfo;
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
					lecteur.readFully(read);
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

			return true;
		} else {
			System.out.println("File not found!");
			return false;
		}
	}

	public TrackerInfo[] getTrackers() {
		return trackers;
	}
}
