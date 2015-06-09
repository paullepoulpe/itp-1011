package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Ce message indique (comme le dit tres bien son nom) que l'emetteur n'est plus
 * iteresse par les donnees du recepteur.
 * 
 * @author Damien, Maarten
 * 
 */
public class NotInterested extends Message {
	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {

		output.writeInt(1);
		output.writeByte(ID.notInterested.ordinal());
		System.out.println("Sent NotInterested");

	}
}
