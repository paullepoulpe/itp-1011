package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class Interested extends Message {
	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) {
		try {
			output.write((int) 1);
			output.writeByte(2);
			System.out.println("Sent Interested");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
