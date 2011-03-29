package torrent.messages;

/*
 * Ce message est constitue d'un entier qui correspond a l index de la piece qu on possede.
 */
public class Have extends Message {
	private int pieceIndex;

	public Have(int i) {
		pieceIndex = i;
	}

	public int getPieceIndex() {
		return pieceIndex;
	}
}
