package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

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

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) {
		try {
			output.write((int) 5);
			output.writeByte(4);
			output.write((int) pieceIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
