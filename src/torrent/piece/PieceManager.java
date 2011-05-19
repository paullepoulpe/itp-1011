package torrent.piece;

import gui.FunnyBar;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import IO.TorrentFileWriter;

import settings.GeneralSettings;
import torrent.Metainfo;
import torrent.Torrent;

/**
 * Cette classe s'occupe de la gestion des pieces. Elle selectionne lesquelles
 * doivent etre demandees aux peers.
 * 
 * @author Damien Engels, Maarten Sap
 */
public class PieceManager {
	private ArrayList<Piece> allPieces;
	private LinkedList<Piece> piecesOfInterest, leftPieces;
	private Torrent torrent;
	private TorrentFileWriter writer;
	private FunnyBar funnyBar;

	public PieceManager(Torrent torrent) {
		this.torrent = torrent;
		this.leftPieces = new LinkedList<Piece>();
		this.piecesOfInterest = new LinkedList<Piece>();
		initPieces();
		this.writer = new TorrentFileWriter(torrent);
		// on met les pieces dans la liste de pieces
		leftPieces.addAll(allPieces);
		// On melange toutes les pieces
		Collections.shuffle(this.leftPieces);

		ListIterator<Piece> iterator = leftPieces.listIterator();
		while (iterator.hasNext()
				&& piecesOfInterest.size() < GeneralSettings.MAX_NUM_OF_CURRENT_PIECES) {
			Piece piece = iterator.next();
			iterator.remove();
			piece.allocateMemory();
			piecesOfInterest.addLast(piece);
		}
		funnyBar = new FunnyBar((int) Math.ceil((double) torrent.getMetainfo()
				.getSize()
				/ (double) Piece.BLOCK_SIZE), new Dimension(600, 80));

	}

	/**
	 * Cette methode doit tout d abord tester si le torrent est complet. Si c
	 * est pas le cas, on enleve les pieces completes des pieces a telecharger
	 * 
	 * Cette methode est appelee dans la classe MessageHandler lors d'un
	 * visit(SendBlock s).
	 */
	public void updatePriorities() {
		if (!isComplete()) {
			synchronized (piecesOfInterest) {
				ListIterator<Piece> iterator = piecesOfInterest.listIterator();
				while (iterator.hasNext()) {
					Piece piece = iterator.next();
					if (piece.isChecked()) {
						iterator.remove();
						writer.writePiece(piece);
						piece.releaseMemory();
						torrent.notifyPeerHandlers(piece.getIndex());
						if (!leftPieces.isEmpty()) {
							Piece newPiece = leftPieces.removeFirst();
							newPiece.allocateMemory();
							iterator.add(newPiece);
						}
					}
				}
			}
			System.out.println((int) Math
					.round(getDownloadedCompleteness() * 100)
					/ 100.0 + " %....................");
			// System.err.println("Nombre de pieces :" + getNbPieces());
			// System.err.println(piecesOfInterest.toString());
			// System.err.println(leftPieces.toString());
		} else {
			// synchronized (System.out) {
			// writer.writeAll();
			// }

		}

	}

	/**
	 * check si il y a une piece interessante dans les pieces que le peer a ,
	 * sinon retourne l'index -1
	 * 
	 * @param peerPieceIndex
	 * @return
	 */
	public int getPieceOfInterest(boolean[] peerPieceIndex) {
		int index = 0;
		int minDemandes = Integer.MAX_VALUE;
		synchronized (piecesOfInterest) {
			Iterator<Piece> iterator = piecesOfInterest.iterator();
			while (iterator.hasNext()) {
				Piece piece = iterator.next();
				if (piece.getNbDemandes() < minDemandes
						&& peerPieceIndex[index]) {
					index = piece.getIndex();
					minDemandes = piece.getNbDemandes();
				}
			}
		}
		if (minDemandes < Integer.MAX_VALUE) {
			return index;
		} else {
			return -1;
		}

	}

	/**
	 * retourne la completion du telechargement
	 * 
	 * @return le pourcentage d'avancement du telechargement (un double)
	 */
	public double getDownloadedCompleteness() {
		double downloadedCompleteness = 0;
		ListIterator<Piece> iterator = allPieces.listIterator();
		while (iterator.hasNext()) {
			downloadedCompleteness += iterator.next().getDownloadCompleteness();
		}
		return downloadedCompleteness / allPieces.size();
	}

	/**
	 * verifie si on a au moins une piece!
	 * 
	 * @return true si on a aucune piece de complete
	 */
	public boolean isEmpty() {
		return piecesOfInterest.size() + leftPieces.size() == allPieces.size();
	}

	/**
	 * teste si le torrent est complet ou non
	 * 
	 * @return true si toutes les pieces sont completes et verfifiees
	 */
	public boolean isComplete() {
		return (leftPieces.size() + piecesOfInterest.size()) == 0;
	}

	/**
	 * initialise le tableau des pieces a la bonne taille, et initialise chaque
	 * piece avec son index et sa somme de controle
	 */
	private void initPieces() {
		Metainfo metainfo = torrent.getMetainfo();
		int nbPieces = metainfo.getNbPieces();
		allPieces = new ArrayList<Piece>(nbPieces);

		for (int i = 0; i < nbPieces; i++) {
			byte[] pieceHash = Arrays.copyOfRange(metainfo.getPiecesHash(),
					20 * i, 20 * (i + 1));
			if (i == nbPieces - 1) {
				int length = metainfo.getSize()
						- ((nbPieces - 1) * metainfo.getPieceLength());
				allPieces.add(i, new Piece(i, length, pieceHash));
			} else {
				allPieces.add(i, new Piece(i, metainfo.getPieceLength(),
						pieceHash));
			}
		}

	}

	public Piece[] getPieces() {
		return allPieces.toArray(new Piece[allPieces.size()]);
	}

	public int getNbPieces() {
		return torrent.getMetainfo().getNbPieces();
	}

	public Piece getPiece(int index) {
		return allPieces.get(index);
	}

	public Piece[] getCurrentPieces() {
		return piecesOfInterest.toArray(new Piece[piecesOfInterest.size()]);
	}

	public FunnyBar getFunnyBar() {
		return funnyBar;
	}
}
