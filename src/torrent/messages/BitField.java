package torrent.messages;

import java.util.LinkedList;

import torrent.Torrent;
import torrent.piece.Piece;

/*
 * Ce message correspond a un tableau de bits de taille egale au nombre de pieces du torrent.
 * Les bits a 1 sont ceux que le pair possede. Va savoir comment transformer des 
 * tableaux de byte en tableaux de bits
 */
public class BitField extends Message {
	private boolean[] posessedPieces;
	private byte[] bitField;

	public BitField(byte[] bitField) {
		// verifier que le bit field soit de la bonne taille sinon, se
		// deconnecter du pair
		this.bitField = bitField.clone();
		posessedPieces = new boolean[bitField.length * 8];

		for (int i = 0; i < bitField.length; i++) {
			byte bits = bitField[i];
			for (int j = 0; j < 8; j++) {
				if ((bits & 1) == 1) {
					if (8 * (i + 1) - j - 1 < posessedPieces.length) {
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
		Piece[] pieces = torrent.getPieces();
		this.bitField = new byte[(int) Math.ceil(pieces.length / 8) + 5];
		int length = bitField.length - 4;
		for (int i = 0; i < 4; i++) {
			bitField[3 - i] = (byte) (length % (1 << 8));
			length >>= 8;
		}
		for (int i = 0; i < bitField.length; i++) {
			byte bit = 0;
			for (int j = 0; j < 8; j++) {
				if ((i * 8) + j < pieces.length) {
					if (pieces[(i * 8) + j].isComplete()) {

						bit &= 1;
					}
				}
				bit <<= 1;
			}
			bitField[i + 5] = bit;
		}
	}

	public boolean[] getPosessedPieces() {
		return posessedPieces;
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	public byte[] getBitField() {
		return bitField;
	}
}
