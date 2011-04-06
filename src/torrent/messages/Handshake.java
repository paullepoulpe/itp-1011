package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import torrent.Torrent;
import torrent.peer.Peer;

public class Handshake {
	private Torrent torrent;
	private byte pstrLength;
	private byte[] infoHash, reserved, peerID;
	private byte[] protocol;

	public Handshake(Peer peer, Torrent torrent) {
		protocol = "BitTorrent Protocol".getBytes();
		pstrLength = (byte) protocol.length;
		this.torrent = torrent;
		infoHash = torrent.getMetainfo().getInfoHash().binaryHash();
		reserved = new byte[8];
		peerID = peer.getId().getBytes();
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

	public void send(DataOutputStream output) {
		try {
			output.writeByte(pstrLength);
			output.write(protocol);
			output.write(reserved);
			output.write(infoHash);
			output.write(peerID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getPstrLength() {
		return pstrLength;
	}

	public byte[] getPeerID() {
		return peerID;
	}
}
