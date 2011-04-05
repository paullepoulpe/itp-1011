package torrent.messages;

public class NotInterested extends Message{
	public void accept(MessageVisitor v) {
		v.visit(this);
	}
}
