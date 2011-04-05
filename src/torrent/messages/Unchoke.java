package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Unchoke extends Message {
	public Unchoke() {
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) {
		try {
			output.write((int) 1);
			output.writeByte(1);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
