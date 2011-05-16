package torrent.piece;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import torrent.Torrent;

/**
 * Cette classe s'occupe de la gestion des pieces. Elle selectionne lesquelles
 * doivent etre demandees aux peers.
 * 
 * @author Damien Engels, Maarten Sap
 */
public class PieceManager {
	private LinkedList<Piece> piecesOfInterest, allPieces;
	final static int MAX_NUM_OF_PIECES = 100;
	private Torrent torrent;

	public PieceManager(Torrent torrent) {
		this.torrent = torrent;
		this.allPieces = new LinkedList<Piece>();
		this.piecesOfInterest = new LinkedList<Piece>();
		// on met les pieces dans la liste de pieces
		for (int i = 0; i < torrent.getPieces().length; i++) {
			this.allPieces.add(torrent.getPieces()[i]);
		}
		// On melange toutes les pieces
		Collections.shuffle(this.allPieces);
		for (int i = 0; i < MAX_NUM_OF_PIECES && i < allPieces.size(); i++) {
			piecesOfInterest.add(allPieces.get(i));
		}
		allPieces.removeAll(piecesOfInterest);

	}

	/**
	 * Cette methode doit tout d abord tester si le torrent est complet. Si c
	 * est pas le cas, on enleve les pieces completes des pieces a telecharger
	 * 
	 * Cette methode est appelee dans la classe MessageHandler lors d'un
	 * visit(SendBlock s).
	 */
	public void updatePriorities() {
		if (!torrent.isComplete()) {
			synchronized (piecesOfInterest) {

				ListIterator<Piece> iterator = piecesOfInterest.listIterator();
				while (iterator.hasNext()) {
					Piece piece = iterator.next();
					if (piece.isChecked()) {
						iterator.remove();
						torrent.notifyPeerHandlers(piece.getIndex());
						if (!allPieces.isEmpty()) {
							iterator.add(allPieces.getFirst());
							allPieces.removeFirst();
						}
					}
				}
			}
			 System.out.println((int) Math.round(torrent
			 .getDownloadedCompleteness() * 100)
			 / 100.0 + " %....................");
		} else {
			synchronized (System.out) {
				torrent.writeToFile();
			}

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
}
