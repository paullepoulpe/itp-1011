package torrent.messages;

import java.io.DataInputStream;
import java.io.IOException;

import torrent.Torrent;
import torrent.peer.Peer;

public class Handshake {
	private byte[] handshake;
	private Torrent torrent;
	private byte pstrLength;
	private byte[] infoHash, reserved, peerID;
	private byte[] protocol;

	public Handshake(Peer peer) {
		protocol = "BitTorrent Protocol".getBytes();
		pstrLength = (byte) protocol.length;
		infoHash = torrent.getMetainfo().getInfoHash().binaryHash();
		reserved = new byte[8];
		peerID = peer.getId().getBytes();
		handshake = new byte[49 + pstrLength];
		handshake[0] = pstrLength;
		for (int i = 0; i < pstrLength; i++) {
			handshake[1 + i] = protocol[i];
		}
		for (int i = 0; i < 8; i++) {
			handshake[1 + pstrLength + i] = reserved[i];
		}
		for (int i = 0; i < 20; i++) {
			handshake[9 + pstrLength] = infoHash[i];
		}
		for (int i = 0; i < 20; i++) {
			handshake[29 + pstrLength] = peerID[i];
		}

	}

	public Handshake(DataInputStream input) {
		try {
			pstrLength = input.readByte();
			protocol = new byte[pstrLength];
			input.readFully(protocol);
			reserved = new byte[8];
			input.readFully(reserved);
			infoHash = new byte[20];
			input.readFully(infoHash);
			peerID = new byte[20];
			input.readFully(peerID);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setReserved(byte[] seq) throws IllegalArgumentException {
		if (seq.length != 8) {
			throw new IllegalArgumentException();
		} else {
			this.reserved = seq.clone();
		}

	}

	public boolean isCompatible(Handshake otherHanshake) {
		return this.protocol.equals(otherHanshake.protocol)
				&& this.infoHash.equals(otherHanshake.infoHash)
				&& this.reserved.equals(otherHanshake.reserved);
	}

	public byte[] getHandshake() {
		return this.handshake.clone();
	}

	public byte getPstrLength() {
		return pstrLength;
	}

	public byte[] getPeerID() {
		return peerID;
	}
}
