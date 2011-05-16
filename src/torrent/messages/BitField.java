package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import torrent.Torrent;
import torrent.piece.Piece;

/**
 * Ce message correspond a un tableau de bits de taille egale au nombre de
 * pieces du torrent. Les bits a 1 sont ceux que le pair possede.
 */
public class BitField extends Message {
	private boolean[] posessedPieces;
	private boolean noPieces;
	private int nbPieces;

	public BitField(byte[] bitField) {
		posessedPieces = new boolean[bitField.length * 8];
		for (int i = 0; i < bitField.length; i++) {
			byte bits = bitField[i];
			for (int j = 0; j < 8; j++) {
				if ((bits & 1) == 1) {
					if (8 * (i + 1) - j - 1 < posessedPieces.length) {
						nbPieces++;
						posessedPieces[8 * (i + 1) - j - 1] = true;
					} else {
						break;
					}
				}
				bits >>= 1;
			}
		}
	}

	public BitField(Torrent torrent) {
		Piece[] pieces = torrent.getPieceManager().getPieces();
		noPieces = torrent.getPieceManager().isEmpty();
		posessedPieces = new boolean[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			boolean b = pieces[i].isChecked();
			posessedPieces[i] = b;
			noPieces = noPieces && !b;
		}

	}

	public boolean[] getPosessedPieces() {
		return posessedPieces;
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	public void send(DataOutputStream output) throws IOException {
		if (!noPieces) {
			output.writeInt(1 + (int) Math.ceil(posessedPieces.length / 8.0));

			for (int i = 0; i < Math.ceil(posessedPieces.length / 8.0); i++) {
				byte bits = 0;
				for (int j = 0; j < 8; j++) {
					if (((i * 8) + j < posessedPieces.length)
							&& (posessedPieces[(i * 8) + j])) {
						bits |= 1;
					}
					bits <<= 1;
				}
				output.writeByte(bits);
			}
			output.flush();

		}

	}

	public boolean equals(BitField otherBitField) {
		return Arrays.equals(this.posessedPieces, otherBitField.posessedPieces);
	}

	public boolean hasNoPieces() {
		return noPieces;
	}

	public double getNbPiece() {
		return nbPieces;
	}
}
