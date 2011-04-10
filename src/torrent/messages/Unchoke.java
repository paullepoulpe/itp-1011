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
	public void send(DataOutputStream output) throws IOException {
		try {
			output.writeInt(1);
			output.writeByte(1);
			System.out.println("Sent Unchoke");
		} catch (IOException e) {
			throw e;
		}

	}
}
