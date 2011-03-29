package torrent.messages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import torrent.messages.ID;

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
			mess = new byte[lengthMess];
			// input.readFully(mess);

			// verifier que 0<mess[4]<9
			switch (ID.values()[id]) {
			case choke:
				// message = new Choke(mess[0]);
				break;
			case unchoke:
				break;
			case have:
				message = new Have(input.readInt());
				break;
			case bitField:
				break;
			case interested:
				break;
			case notInterested:
				break;
			case request:
				message = new Request(mess);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
