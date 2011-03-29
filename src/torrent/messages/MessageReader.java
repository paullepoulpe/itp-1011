package torrent.messages;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Cette classe s'occupe de lire les messages recus par le pair et des les
 * mettres dans le bon format.
 * 
 * @author engels
 * 
 */
public class MessageReader {
	private byte[] mess;
	private byte id;
	private int lengthMess;
	private Message message;

	public MessageReader(DataInputStream input) {
		try {
			lengthMess = input.readInt();
			id = input.readByte();
			mess = new byte[lengthMess-1];
			// input.readFully(mess);
			// verifier que 0<mess[4]<9
			switch (ID.values()[id]) {

			case choke:
				message = new Choke();
				break;

			case unchoke:
				message = new Unchoke();
				break;

			case have:
				message = new Have(input.readInt());
				break;

			case bitField:
				// attention, lengthMess est-ce la longueur du tableau de bytes
				// ou le nombre de bits dans le message + 1 ????
				input.readFully(mess);
//				message = new BitField();
				break;

			case interested:
				message = new Interested();
				break;

			case notInterested:
				message = new NotInterested();
				break;

			case request:
				input.readFully(mess);
				message = new Request(mess);
				break;

			case piece:
				int numPiece, numBloc;
				numPiece = input.readInt();
				numBloc = input.readInt();
				input.readFully(mess);
				message = new SendBlock(numPiece,numBloc,mess);
				break;

			case cancel:
				input.skipBytes(lengthMess-1);
				break;

			case port:
				input.skipBytes(lengthMess-1);
				break;

			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Message getMessage() {
		return message;
	}
}
