package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Cette classe est un message de requete de blocs.
 * 
 * @author Damien, Maarten
 * 
 */
public class Request extends Message {
	private int index, begin, length;

	public Request(int index, int begin, int length) {
		this.index = index;
		this.begin = begin;
		this.length = length;
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

	@Override
	public void send(DataOutputStream output) throws IOException {
		output.writeInt(13);
		output.writeByte(6);
		output.writeInt(index);
		output.writeInt(begin);
		output.writeInt(length);

	}

	public boolean equals(Object otherRequest) {
		if (otherRequest instanceof Request) {
			return ((Request) otherRequest).begin == this.begin
					&& ((Request) otherRequest).index == this.index
					&& ((Request) otherRequest).length == this.length;
		}
		return false;

	}
}
