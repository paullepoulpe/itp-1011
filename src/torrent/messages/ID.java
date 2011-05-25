package torrent.messages;

/**
 * Contient les enum utilises pour identifier les messages recus
 * 
 * @author Damien, Maarten
 * 
 */
public enum ID {
	choke, unchoke, interested, notInterested, have, bitField, request, piece, cancel, port, sendRSAkey, sendSymmetricKey;
}
