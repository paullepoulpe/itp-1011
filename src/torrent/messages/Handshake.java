package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import torrent.Torrent;
import torrent.peer.Peer;

public class Handshake {
	private Torrent torrent;
	private byte pstrLength;
	private byte[] infoHash, reserved, peerID;
	private byte[] protocol;

	public Handshake(Peer peer, Torrent torrent) {
		protocol = "BitTorrent protocol".getBytes();
		pstrLength = (byte) protocol.length;
		this.torrent = torrent;
		infoHash = torrent.getMetainfo().getInfoHash().binaryHash();
		reserved = new byte[8];
		Arrays.fill(reserved, (byte) 0);
		peerID = peer.getId().getBytes();
	}

	public Handshake(DataInputStream input) {
		try {
			boolean lu = false;
			while (!lu) {
				if (input.available() > 0) {
					pstrLength = input.readByte();
					lu = true;
				}
			}

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
		System.out.println(Arrays.toString(this.reserved)
				+ Arrays.toString(otherHanshake.reserved));
		return Arrays.equals(this.protocol, otherHanshake.protocol)
				&& Arrays.equals(this.infoHash, otherHanshake.infoHash)
				&& Arrays.equals(this.reserved, otherHanshake.reserved);
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

	public byte[] getProtocol() {
		return protocol;
	}

	public byte[] getInfoHash() {
		return infoHash;
	}

	public byte[] getReserved() {
		return reserved;
	}
}
