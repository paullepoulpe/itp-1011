package torrent.messages;

/*
 * Ce message correspond a un tableau de bits de taille egale au nombre de pieces du torrent.
 * Les bits a 1 sont ceux que le pair possede. Va savoir comment transformer des 
 * tableaux de byte en tableaux de bits
 */
public class BitField extends Message {
	private int[] posessedPieces;

	public BitField(byte[] bitField) {

	}
}
