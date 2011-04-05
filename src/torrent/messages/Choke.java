package torrent.messages;

public class Choke extends Message {
	public Choke() {
	}
	public void accept(MessageVisitor v) {
		v.visit(this);
	}
}
