package torrent.messages;

import torrent.Torrent;
import torrent.peer.PeerHandler;

/*
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
		// TODO Quand on recoit un message Choke, on ne fait plus partie de la
		// liste des pairs interesants de l emetteur. Le pair emetteur ne
		// repondra plus a nos requetes
		peerHandler.setChocking(true);

	}

	@Override
	public void visit(Request r) {
		// TODO si on recoit cela, on doit idealement preparer un message
		// SendBloc dans notre queue de messages avec les attributs de request
		/*
		 * SendBlock sendBlock = new SendBlock(r.getIndex(), r.getBegin(),
		 * torrent.getPieces()[r.getIndex()].getBlock(r.getBegin()));
		 * peerHandler.addAEnvoer(sendBlock);
		 */

	}

	@Override
	public void visit(NotInterested n) {
		// TODO On doit preparer un message choke pour le pair qui nous envoie
		// cela, afin de eviter les requetes inutiles
		peerHandler.setInterested(false);
		peerHandler.addAEnvoer(new Choke());

	}

	@Override
	public void visit(Have h) {
		// TODO Ce message indique que le pair a la piece decrite dans le corps
		// du message (attribut pieceIndex de Have). il faudrait donc ajouter
		// cette piece a la liste des pieces que le pair possede

		// DONE
		peerHandler.addPeerPiece(h.getPieceIndex());

	}

	@Override
	public void visit(Interested i) {
		// TODO Si on recois cela, l emetteur veut nous demander des pieces, il
		// faut donc (si on le veut bien sur) preparer un message Unchoke a
		// mettre dans la queue de messages

		peerHandler.setInterested(true);
		peerHandler.addAEnvoer(new Unchoke());

	}

	@Override
	public void visit(BitField b) {
		// TODO donne la liste des pieces que le pair emetteur possede.

		peerHandler.setPeerPiecesIndex(b.getPosessedPieces());
	}

	@Override
	public void visit(SendBlock s) {
		// TODO on recoit un bloc... il faut le mettre dans notre liste de
		// blocs, mettre a jour tout! (liste de pieces interessantes
		// (updatePriorities de PieceManager)

		peerHandler.getPieceMgr().feedPiece(s.getPieceIndex(), s.getBloc(),
				s.getBlocIndex());
		peerHandler.getPieceMgr().updatePriorities();
		if (torrent.isComplete()) {
			torrent.writeToFile();
		}

	}

	@Override
	public void visit(Unchoke u) {
		// TODO quand on recoit cela, on peut faire des requests. Il faut aller
		// chercher quelle piece on veut requester dans notre updatePriorities
		// liste de pieces interessantes etc

		peerHandler.setChocking(false);
		// int index = peerHandler.getPieceMgr().updatePriorities();
		// peerHandler.addAEnvoer(new Request(index, begin, length));
	}

}
