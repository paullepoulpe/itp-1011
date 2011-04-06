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
	 * est pas le cas, on met
	 * 
	 * @return index de la piece qu'on doit demander (int)
	 */
	public void updatePriorities() {
		if (!torrent.isComplete()) {
			// On enleve toutes les pieces quon vient de mettre dans la liste
			// a telécharger
			for (int i = 0; i < PiecesOfInterest.size(); i++) {
				if (PiecesOfInterest.get(i).isComplete()) {
					PiecesOfInterest.remove(i);
					if (!allPieces.isEmpty()) {
						PiecesOfInterest.addLast(allPieces.getFirst());
						allPieces.removeFirst();
					}
				}
			}
			// la on doit faire en sorte que la piece la moins demandee soit
			// celle que nous on demande aux autres

		}
	}

	public void feedPiece(int pieceIndex, byte[] bloc, int begin) {
		torrent.getPieces()[pieceIndex].feed(begin, bloc);
	}

	public int getPieceOfInterest() {
		return PiecesOfInterest.getFirst().getIndex();
	}
}
