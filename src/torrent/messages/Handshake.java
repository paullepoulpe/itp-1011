package torrent.messages;

import torrent.Torrent;
import torrent.peer.Peer;

public class Handshake {
	private String Handshake;
	private Torrent torrent;
	private byte length;
	private byte[] infoHash, reserved, peerID;
	private static final String protocol = "BitTorrent Protocol";

	public Handshake(Peer peer) {
		length = (byte) protocol.getBytes().length;
		infoHash = torrent.getMetainfo().getInfoHash();
		reserved = new byte[8];
		peerID = peer.getId();
		Handshake = protocol+length+reserved+infoHash+peerID;
	}

	public void setReserved(byte[] seq) {
		this.reserved = seq;
	}

	public String getHandshake() {
		return this.Handshake;
	}
}
