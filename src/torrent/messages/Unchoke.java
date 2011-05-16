package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Quand on recoit cela (non seulement on est tres content) cela veut dire que
 * un pair est d'accord pour partager ses blocs avec nous.
 * 
 * @author Damien, Maarten
 * 
 */
public class Unchoke extends Message {
	public Unchoke() {
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {

		output.writeInt(1);
		output.writeByte(1);
		System.out.println("Sent Unchoke");

	}
}
