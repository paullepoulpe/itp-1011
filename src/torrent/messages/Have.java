package torrent.messages;

public class Have extends Message {
	private int pieceIndex;

	public Have(byte[] message) {
		pieceIndex = (message[0] << 24) + (message[1] << 16)
				+ (message[2] << 8) + message[3];
	}

	public int getPieceIndex() {
		return pieceIndex;
	}
}
