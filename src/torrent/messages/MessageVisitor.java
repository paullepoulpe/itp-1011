package torrent.messages;

public interface MessageVisitor {
	public void visit(Choke  c);
	public void visit(Request r);
	public void visit(NotInterested n);
	public void visit(Have h);
	public void visit(Interested i);
	public void visit(BitField b);
	public void visit(SendBlock s);
	public void visit(Unchoke u);
//	public void visit(Stop);
//	public void visit(Cancel);
}
