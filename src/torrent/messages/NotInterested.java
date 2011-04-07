package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class NotInterested extends Message {
	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) {
		try {
			output.write((int) 1);
			output.writeByte(3);
			System.out.println("Sent NotInterested");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
