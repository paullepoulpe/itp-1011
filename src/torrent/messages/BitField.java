package torrent.messages;

/*
 * Ce message correspond a un tableau de bits de taille egale au nombre de pieces du torrent.
 * Les bits a 1 sont ceux que le pair possede. Va savoir comment transformer des 
 * tableaux de byte en tableaux de bits
 */
public class BitField extends Message {
	private boolean[] posessedPieces;

	public BitField(byte[] bitField) {
		// verifier que le bit field soit de la bonne taille sinon, se
		// deconnecter du pair
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

	public boolean[] getPosessedPieces() {
		return posessedPieces;
	}
}
