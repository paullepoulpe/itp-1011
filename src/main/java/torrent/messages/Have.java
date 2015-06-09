package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Ce message est constitue d'un entier qui correspond a l'index de la piece
 * qu'on possede.
 */
public class Have extends Message {
	private int pieceIndex;

	public Have(int i) {
		pieceIndex = i;
	}

	public int getPieceIndex() {
		return pieceIndex;
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {

		output.writeInt(5);
		output.writeByte(ID.have.ordinal());
		output.writeInt(pieceIndex);

	}

	public boolean equals(Have otherHave) {
		return this.pieceIndex == otherHave.pieceIndex;
	}
}
