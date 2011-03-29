package torrent.messages;

import torrent.messages.ID;

/**
 * Cette classe s'occupe de lire les messages recus par le pair et des les
 * mettres dans le bon format.
 * 
 * @author engels
 * 
 */
public class MessageReader {
	private byte[] message;
	private int lengthMess;

	public MessageReader(byte[] mess) {
		lengthMess = (mess[0] << 24) + (mess[1] << 16) + (mess[2] << 8)
				+ mess[3];
		//verifier que 0<mess[4]<9
		switch (ID.values()[mess[4]]) {
		case choke:
			
			break;
		case unchoke:
			break;
		case have:
			break;
		case bitField:
			break;
		case interested:
			break;
		case notInterested:
			break;
		case request:
			break;
		case piece:
			break;
		case cancel:
			break;
		case port:
			break;
		default:
			break;
		}

	}
	
}
