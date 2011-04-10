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
	public void send(DataOutputStream output) throws IOException {
		try {
			output.writeInt(5);
			output.writeByte(4);
			output.writeInt(pieceIndex);
			System.out.println("Sent Have");
		} catch (IOException e) {
			throw e;
		}

	}

	public boolean equals(Have otherHave) {
		return this.pieceIndex == otherHave.pieceIndex;
	}
}
