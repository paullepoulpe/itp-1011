package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import torrent.Torrent;

/**
 * Cette classe correspond a un message handshake, cest a dire le premier
 * message quon envoie a un pair pour initialiser la connexion.
 * 
 * @author Damien, Maarten
 * 
 */
public class Handshake {
	private byte pstrLength;
	private byte[] infoHash, reserved, peerID;
	private byte[] protocol;

	/**
	 * Constructeur pour creer notre handshake.
	 * 
	 * @param torrent
	 *            contient toutes les informations necessaires.
	 */
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

	/**
	 * Constructeur pour recuperer un HandShake a partir d'un DataInputStream.
	 * 
	 * @param input
	 *            le Stream qui "contient" le handShake.
	 * @throws IOException
	 */
	public Handshake(DataInputStream input) throws IOException {
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

	}

	/**
	 * Methode pour definir les bytes reserves a des information complementaires
	 * sur le protocole d echange
	 * 
	 * @param seq
	 *            le tableau de byte qui contient les informations necessaires
	 * @throws IllegalArgumentException
	 *             si la longueur n'est pas compatible
	 */
	public void setReserved(byte[] seq) throws IllegalArgumentException {
		if (seq.length != 8) {
			throw new IllegalArgumentException();
		} else {
			this.reserved = seq.clone();
		}

	}

	public void setEncryptionEnabled() {
		System.out.println("EnableEncryption!");
		this.reserved[7] = 0x10;
	}

	/**
	 * On teste si le Handshake recu est compatible avec le protocole qu on
	 * utilise en comparant le protocole et le hash.
	 * 
	 * @param otherHanshake
	 * @return true si le HS est compatible, false sinon.
	 */
	public boolean isCompatible(Handshake otherHanshake) {
		boolean compatibleEncryption = (this.reserved[7] & 0x10) == (otherHanshake.reserved[7] & 0x10);
		return Arrays.equals(this.protocol, otherHanshake.protocol)
				&& Arrays.equals(this.infoHash, otherHanshake.infoHash)
				&& compatibleEncryption;
	}

	public void send(DataOutputStream output) throws IOException {

		output.writeByte(pstrLength);
		output.write(protocol);
		output.write(reserved);
		output.write(infoHash);
		output.write(peerID);
		output.flush();

	}

	public byte[] getPeerID() {
		return peerID;
	}

	public byte[] getReserved() {
		return reserved;
	}

	/**
	 * Verifie si deux handShake sont egaux
	 * 
	 * @param otherHandshake
	 *            est compare a this
	 * @return si les HandShakes sont egaux
	 */
	public boolean equals(Handshake otherHandshake) {
		return this.pstrLength == otherHandshake.pstrLength
				&& Arrays.equals(this.infoHash, otherHandshake.infoHash)
				&& Arrays.equals(this.reserved, otherHandshake.reserved)
				&& Arrays.equals(this.peerID, otherHandshake.peerID)
				&& Arrays.equals(this.protocol, otherHandshake.protocol);
	}
}
