package torrent.messages;

import torrent.Torrent;
import torrent.peer.PeerHandler;
import torrent.piece.Piece;

/**
 * cette classe s occupe de traiter les messages quon recoit, en faisant le
 * necessaire suivant ce quon recoit.
 * 
 * @author DAMIEN,MAARTEN
 * 
 */
public class MessageHandler implements MessageVisitor {
	private PeerHandler peerHandler;
	private Torrent torrent;

	public MessageHandler(PeerHandler peerHandler, Torrent torrent) {
		this.peerHandler = peerHandler;
		this.torrent = torrent;
	}

	@Override
	public void visit(Choke c) {
		// Quand on recoit un message Choke, on ne fait plus partie de la
		// liste des pairs interesants de l emetteur. Le pair emetteur ne
		// repondra plus a nos requetes

		peerHandler.setChocking(true);
		System.out.println("Recu un Choke");

	}

	@Override
	public void visit(Request r) {
		// si on recoit cela, on doit idealement preparer un message
		// SendBloc dans notre queue de messages avec les attributs de request
		/*
		 * SendBlock sendBlock = new SendBlock(r.getIndex(), r.getBegin(),
		 * torrent.getPieces()[r.getIndex()].getBlock(r.getBegin()));
		 * peerHandler.addAEnvoer(sendBlock);
		 */
		System.out.println("Recu un request pour piece : " + r.getIndex()
				+ ", bloc : " + r.getBegin());

	}

	@Override
	public void visit(NotInterested n) {
		// On doit preparer un message choke pour le pair qui nous envoie
		// cela, afin de eviter les requetes inutiles

		peerHandler.setInterested(false);
		peerHandler.addAEnvoer(new Choke());

		System.out.println("Recu not Interested");

	}

	@Override
	public void visit(Have h) {
		// Ce message indique que le pair a la piece decrite dans le corps
		// du message (attribut pieceIndex de Have). Il faudra donc ajouter
		// cette piece a la liste des pieces que le pair possede

		// DONE
		peerHandler.addPeerPiece(h.getPieceIndex());

		// System.out.println("Recu Have piece : " + h.getPieceIndex());

	}

	@Override
	public void visit(Interested i) {
		// Si on recoit cela, l emetteur veut nous demander des pieces, il
		// faut donc (si on le veut bien sur) preparer un message Unchoke a
		// mettre dans la queue de messages

		peerHandler.setInterested(true);
		peerHandler.addAEnvoer(new Unchoke());

		System.out.println("Recu Interested");

	}

	@Override
	public void visit(BitField b) {
		// donne la liste des pieces que le pair emetteur possede.

		peerHandler.setPeerPiecesIndex(b.getPosessedPieces());

		System.out.println("Recu bitfield");
	}

	@Override
	public void visit(SendBlock s) {
		/*
		 * On recoit un bloc. D abord on extrait la piece auquel appartient le
		 * bloc Puis on feed le bloc a la piece Ensuite on s assure que le
		 * PieceManager se rende compte qu une piece a ete recue
		 */
		Piece entering = null;
		synchronized (torrent) {
			entering = torrent.getPieces()[s.getPieceIndex()];
		}

		synchronized (entering) {
			entering.feed(s.getBlocIndex(), s.getBloc());
		}
		synchronized (peerHandler) {
			peerHandler.getPieceMgr().updatePriorities();
		}
		System.out.println("Recu bloc : "
				+ (s.getBlocIndex() / Piece.BLOCK_SIZE) + " de la Piece "
				+ s.getPieceIndex());

	}

	@Override
	public void visit(Unchoke u) {
		// quand on recoit cela, on peut faire des requests. Il faut aller
		// chercher quelle piece on veut requester dans notre updatePriorities
		// liste de pieces interessantes etc

		peerHandler.setChocking(false);
		System.out.println("Recu unchoke :):):):):):):):):):):)");
	}

}
