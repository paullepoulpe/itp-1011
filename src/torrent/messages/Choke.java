package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Choke extends Message {
	public Choke() {
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) {
		try {
			output.write((int) 1);
			output.writeByte(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
