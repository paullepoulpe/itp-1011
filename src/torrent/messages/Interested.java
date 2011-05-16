package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe representant un signe que un pair est interesse par nos pieces ou que
 * nous sommes interesses par les pieces d'un pair.
 * 
 * @author Damien, Maarten
 * 
 */
public class Interested extends Message {
	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {

		output.writeInt(1);
		output.writeByte(2);

	}

}
