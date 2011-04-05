package torrent.messages;

public class Unchoke extends Message{
	public Unchoke() {
	}
	public void accept(MessageVisitor v) {
		v.visit(this);
	}
}
