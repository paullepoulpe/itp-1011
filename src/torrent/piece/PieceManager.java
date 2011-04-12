package torrent.piece;

import java.util.Collections;
import java.util.LinkedList;

import torrent.Torrent;

/**
 * Cette classe s'occupe de la gestion des pieces. Elle selectionne lesquelles
 * doivent etre demandees aux peers.
 * 
 * @author msap, engels
 * 
 */
public class PieceManager {
	private LinkedList<Piece> PiecesOfInterest, allPieces;
	final static int MAX_NUM_OF_PIECES = 100;
	private Torrent torrent;

	public PieceManager(Torrent torrent) {
		this.torrent = torrent;
		this.allPieces = new LinkedList<Piece>();
		this.PiecesOfInterest = new LinkedList<Piece>();
		// on met les pieces dans la liste de pieces
		for (int i = 0; i < torrent.getPieces().length; i++) {
			this.allPieces.add(torrent.getPieces()[i]);
		}
		// On melange toutes les pieces
		Collections.shuffle(this.allPieces);
		for (int i = 0; i < MAX_NUM_OF_PIECES && i < allPieces.size(); i++) {
			PiecesOfInterest.add(allPieces.get(i));
		}
		allPieces.removeAll(PiecesOfInterest);

	}

	/**
	 * Cette methode doit tout d abord tester si le torrent est complet. Si c
	 * est pas le cas, on enleve les pieces completes des pieces a telecharger
	 * 
	 * 
	 */
	public void updatePriorities() {
		if (!torrent.isComplete()) {
			for (int i = 0; i < PiecesOfInterest.size(); i++) {
				if (PiecesOfInterest.get(i).isChecked()) {
					PiecesOfInterest.remove(i);
					if (!allPieces.isEmpty()) {
						PiecesOfInterest.addLast(allPieces.getFirst());
						allPieces.removeFirst();
					}
				}
			}
			System.out.println((int) Math.round(torrent
					.getDownloadedCompleteness() * 100)
					/ 100.0
					+ " %....................");
		} else {
			synchronized (System.out) {
				System.gc();
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
		for (int i = 0; i < PiecesOfInterest.size(); i++) {
			if (PiecesOfInterest.get(i).getNbDemandes() < minDemandes
					&& peerPieceIndex[index]) {
				index = PiecesOfInterest.get(i).getIndex();
				minDemandes = PiecesOfInterest.get(i).getNbDemandes();
			}
		}
		if (minDemandes < Integer.MAX_VALUE) {
			return index;
		} else {
			return -1;
		}

	}
}
