package torrent.messages;

public class Interested extends Message{
	public void accept(MessageVisitor v) {
		v.visit(this);
	}
}
