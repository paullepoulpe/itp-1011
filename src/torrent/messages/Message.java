package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/*
 * Classe abstraite:
 * 
 * 
 */
public abstract class Message {
	abstract public void accept(MessageVisitor v);

	abstract public void send(DataOutputStream output) throws IOException;

}