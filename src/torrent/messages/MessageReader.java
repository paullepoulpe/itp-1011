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
	private DataInputStream input;

	public MessageReader(DataInputStream input) {
		this.input = input;
	}

	/**
	 * lit un message sur le stream et retourne null si c'etait un keep alive
	 * 
	 * @return
	 */
	public Message readMessage() throws IOException {
		Message message = null;
		int lengthMess = 0;
		byte id;
		try {
			lengthMess = input.readInt();
			if (lengthMess == 0) {
				return null;
			} else {
				id = input.readByte();
				switch (ID.values()[id]) {

				case choke:
					// System.out.println("Lu choke");
					message = new Choke();
					break;

				case unchoke:
					// System.out.println("Lu unchoke");
					message = new Unchoke();
					break;

				case interested:
					// System.out.println("Lu interested");
					message = new Interested();
					break;

				case notInterested:
					// System.out.println("Lu notinterested");
					message = new NotInterested();
					break;

				case have:
					// System.out.println("Lu have");
					message = new Have(input.readInt());
					break;

				case bitField:
					// System.out.println("Lu bitfield");
					byte[] bitField = new byte[lengthMess - 1];
					input.readFully(bitField);
					message = new BitField(bitField);
					break;

				case request:
					// System.out.println("Lu request");
					int index = input.readInt();
					int begin = input.readInt();
					int length = input.readInt();
					message = new Request(index, begin, length);
					break;

				case piece:
					// System.out.println("Lu sendblock");
					int pieceIndex = input.readInt();
					int blocIndex = input.readInt();
					byte[] bloc = new byte[lengthMess - 9];
					input.readFully(bloc);
					message = new SendBlock(pieceIndex, blocIndex, bloc);
					break;

				case cancel:
					// System.out.println("Lu cancel");
					input.skipBytes(lengthMess - 1);
					break;

				case port:
					// System.out.println("Lu port");
					input.skipBytes(lengthMess - 1);
					break;

				default:
					break;
				}

			}
		} catch (IOException e) {
			throw e;
		}
		return message;

	}
}
