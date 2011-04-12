package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import torrent.Torrent;

public class Handshake {
	private byte pstrLength;
	private byte[] infoHash, reserved, peerID;
	private byte[] protocol;

	public Handshake(Torrent torrent) {
		protocol = "BitTorrent protocol".getBytes();
		pstrLength = (byte) protocol.length;
		infoHash = torrent.getMetainfo().getInfoHash().binaryHash();
		reserved = new byte[8];
		try {
			peerID = Torrent.PEER_ID.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

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

	/**
	 * On teste si le Handshake recu est compatible avec le protocole qu on
	 * utilise en comparant le protocole et le hash.
	 * 
	 * @param otherHanshake
	 * @return true si le HS est compatible, false sinon.
	 */
	public boolean isCompatible(Handshake otherHanshake) {
		/*
		 * System.out.println(Arrays.toString(this.reserved) +
		 * Arrays.toString(otherHanshake.reserved));
		 */
		return Arrays.equals(this.protocol, otherHanshake.protocol)
				&& Arrays.equals(this.infoHash, otherHanshake.infoHash)
		/* && Arrays.equals(this.reserved, otherHanshake.reserved) */;
	}

	public void send(DataOutputStream output) throws IOException {
		try {
			output.writeByte(pstrLength);
			output.write(protocol);
			output.write(reserved);
			output.write(infoHash);
			output.write(peerID);
		} catch (IOException e) {
			throw e;
		}
	}

	public byte[] getPeerID() {
		return peerID;
	}

	public byte[] getReserved() {
		return reserved;
	}

	public boolean equals(Handshake otherHandshake) {
		return this.pstrLength == otherHandshake.pstrLength
				&& Arrays.equals(this.infoHash, otherHandshake.infoHash)
				&& Arrays.equals(this.reserved, otherHandshake.reserved)
				&& Arrays.equals(this.peerID, otherHandshake.peerID)
				&& Arrays.equals(this.protocol, otherHandshake.protocol);
	}
}
