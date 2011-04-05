package torrent.messages;

/*
 * Classe abstraite:
 * 
 * 
 */
public abstract class Message {
	abstract public void accept(MessageVisitor v);
}