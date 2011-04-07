package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

import com.sun.org.apache.xpath.internal.operations.Equals;

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
	public void send(DataOutputStream output) {
		try {
			output.write(13);
			output.writeByte(6);
			output.write(index);
			output.write(begin);
			output.write(length);
			System.out.println("Sent Request");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean equals(Request otherRequest) {
		return otherRequest.begin == this.begin
				&& otherRequest.index == this.index;
	}
}
