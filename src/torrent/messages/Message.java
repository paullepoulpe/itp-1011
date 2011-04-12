package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe abstraite: super classe de tous les messages "normaux" c a d pas le
 * HandShake ni le KeepAlive, mais tous les autres
 * 
 */
public abstract class Message {
	abstract public void accept(MessageVisitor v);
	abstract public void send(DataOutputStream output) throws IOException;
}