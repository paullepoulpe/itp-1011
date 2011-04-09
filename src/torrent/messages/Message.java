package torrent.messages;

import java.io.DataOutputStream;

/*
 * Classe abstraite:
 * 
 * 
 */
public abstract class Message {
	abstract public void accept(MessageVisitor v);

	abstract public void send(DataOutputStream output);

}