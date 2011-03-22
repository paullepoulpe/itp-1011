package torrent;

import java.util.Collections;
import java.util.LinkedList;
import torrent.piece.*;

/**
 * Cette classe s'occupe de la gestion des pieces. Elle selectionne lesquelles
 * doivent etre demandees aux peers.
 * 
 * @author msap
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
			this.PiecesOfInterest = (LinkedList<Piece>) this.allPieces.subList(
					0, MAX_NUM_OF_PIECES-1);
	}

	/**
	 * Cette methode doit tout d abord tester si le torrent est complet. Si c
	 * est le cas
	 *  @return le pourcentage de pieces encore a telecharger (int)
	 */
	public void updatePriorities() {
		if (!torrent.isComplete()) {
			// On enleve toutes les pieces quon vient de mettre dans la liste
			// a telécharger
			for (int i = 0; i < this.PiecesOfInterest.size(); i++) {
				this.allPieces.remove(this.PiecesOfInterest.get(i));
			}
			
			
		}
	}
}
