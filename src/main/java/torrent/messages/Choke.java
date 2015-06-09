package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Cette classe correspond a un message de corps vide nomme Choke. Si un pair
 * nous envoie cela, il ne traitera plus nos requetes.
 * 
 * @author Damien Engels, Maarten Sap
 * 
 */
public class Choke extends Message {
	public Choke() {
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {

		output.writeInt(1);
		output.writeByte(ID.choke.ordinal());
		System.out.println("Sent Choke");

	}
}
