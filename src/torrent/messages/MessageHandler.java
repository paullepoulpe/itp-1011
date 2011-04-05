package torrent.messages;

import torrent.peer.PeerHandler;

/*
 * 
 */
public class MessageHandler implements MessageVisitor {
	PeerHandler peerHandler;

	public MessageHandler(PeerHandler peerHandler) {
		this.peerHandler = peerHandler;
	}

	@Override
	public void visit(Choke c) {
		// TODO Quand on recoit un message Choke, on ne fait plus partie de la
		// liste des pairs interesants de l emetteur. Le pair emetteur ne
		// repondra plus a nos requetes
		

	}

	@Override
	public void visit(Request r) {
		// TODO si on recoit cela, on doit idealement preparer un message
		// SendBloc dans notre queue de messages avec les attributs de request

	}

	@Override
	public void visit(NotInterested n) {
		// TODO On doit preparer un message choke pour le pair qui nous envoie
		// cela, afin de eviter les requetes inutiles

	}

	@Override
	public void visit(Have h) {
		// TODO Ce message indique que le pair a la piece decrite dans le corps
		// du message (attribut pieceIndex de Have). il faudrait donc ajouter
		// cette piece a la liste des pieces que le pair possede

	}

	@Override
	public void visit(Interested i) {
		// TODO Si on recois cela, l emetteur veut nous demander des pieces, il
		// faut donc (si on le veut bien sur) preparer un message Unchoke a
		// mettre dans la queue de messages

	}

	@Override
	public void visit(BitField b) {
		// TODO donne la liste des pieces que le pair emetteur possede.

	}

	@Override
	public void visit(SendBlock s) {
		// TODO on recoit un bloc... il faut le mettre dans notre liste de
		// blocs, mettre a jour tout! (liste de pieces interessantes
		// (updatePriorities de PieceManager)

	}

	@Override
	public void visit(Unchoke u) {
		// TODO quand on recoit cela, on peut faire des requests. Il faut aller
		// chercher quelle piece on veut requester dans notre updatePriorities
		// liste de pieces interessantes etc

	}

}
