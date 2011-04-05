package torrent.messages;

public class Request extends Message {
	private int index, begin, length;

	public Request(byte[] message) {
		index = (message[0] << 24) + (message[1] << 16) + (message[2] << 8)
				+ message[3];
		begin = (message[4] << 24) + (message[5] << 16) + (message[6] << 8)
				+ message[7];
		length = (message[8] << 24) + (message[9] << 16) + (message[10] << 8)
				+ message[11];
	}

	public int getIndex() {
		return index;
	}

	public int getBegin() {
		return begin;
	}

	public int getLength() {
		return length;
	}
	public void accept(MessageVisitor v) {
		v.visit(this);
	}
}
