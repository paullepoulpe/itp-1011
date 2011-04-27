package torrent.messages;

/**
 * Interface dictee par le VisitorPattern Elle oblige les classes l'implementant
 * de definir ses methodes.
 * 
 * @author Damien, Maarten
 * 
 */
public interface MessageVisitor {
	public void visit(Choke c);

	public void visit(Request r);

	public void visit(NotInterested n);

	public void visit(Have h);

	public void visit(Interested i);

	public void visit(BitField b);

	public void visit(SendBlock s);

	public void visit(Unchoke u);
	
	public void visit(SendRSAKey s);
	
	public void visit(SendSymmetricKey s);
}
