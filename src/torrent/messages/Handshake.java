package torrent.messages;

import torrent.Torrent;
import torrent.peer.Peer;

public class Handshake {
	private byte[] Handshake;
	private Torrent torrent;
	private byte pstrLength;
	private byte[] infoHash, reserved, peerID;
	private static final byte[] protocol = "BitTorrent Protocol".getBytes();

	public Handshake(Peer peer) {
		pstrLength = (byte) protocol.length;
		infoHash = torrent.getMetainfo().getInfoHash().binaryHash();
		reserved = new byte[8];
		peerID = peer.getId().getBytes();
		Handshake = new byte[49 + pstrLength];
		Handshake[0] = pstrLength;
		for (int i = 0; i < pstrLength; i++) {
			Handshake[1 + i] = protocol[i];
		}
		for (int i = 0; i < 8; i++) {
			Handshake[1 + pstrLength + i] = reserved[i];
		}
		for (int i = 0; i < 20; i++) {
			Handshake[9 + pstrLength] = infoHash[i];
		}
		for (int i = 0; i < 20; i++) {
			Handshake[29 + pstrLength] = peerID[i];
		}

	}

	public void setReserved(byte[] seq) throws IllegalArgumentException {
		if (seq.length != 8) {
			throw new IllegalArgumentException();
		} else {
			this.reserved = seq.clone();
		}

	}

	public byte[] getHandshake() {
		return this.Handshake.clone();
	}

	public byte getPstrLength() {
		return pstrLength;
	}
}
