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
		// on met les pieces dans la liste de pieces
		for (int i = 0; i < torrent.getPieces().length; i++) {
			this.allPieces.add(torrent.getPieces()[i]);
		}
		// On melange toutes les pieces
		Collections.shuffle(this.allPieces);
		this.PiecesOfInterest = (LinkedList<Piece>) this.allPieces.subList(0,
				MAX_NUM_OF_PIECES - 1);
	}

	/**
	 * Cette methode doit tout d abord tester si le torrent est complet. Si c
	 * est pas le cas, on met
	 * 
	 * @return index de la piece qu'on doit demander (int)
	 */
	public int updatePriorities() {
		if (!torrent.isComplete()) {
			// On enleve toutes les pieces quon vient de mettre dans la liste
			// a telécharger
			allPieces.removeAll(PiecesOfInterest);
			// la on doit faire en sorte que la piece la moins demandee soit
			// celle que nous on demande aux autres
			return PiecesOfInterest.getFirst().getIndex();
		}
		return -1;
	}

	public void feedPiece(int pieceIndex, byte[] bloc, int begin) {
		torrent.getPieces()[pieceIndex].feed(begin, bloc);
	}
}
