package torrent.messages;

import java.io.FileNotFoundException;

import torrent.Torrent;
import torrent.peer.PeerHandler;
import torrent.piece.Piece;

/**
 * cette classe s'occupe de traiter les messages qu'on recoit, en faisant le
 * necessaire suivant ce qu'on recoit.
 * 
 * @author Damien Engels, Maarten Sap
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
		peerHandler.multiplyNotation(1 / 2);

	}

	@Override
	public void visit(Request r) {
		// si on recoit cela, on doit idealement preparer un message
		// SendBloc dans notre queue de messages avec les attributs de request
		Piece piece;
		try {
			piece = torrent.getPieceManager().readPiece(r.getIndex());
			SendBlock sendBlock = new SendBlock(r.getIndex(), r.getBegin(),
					piece.getBlock(r.getBegin()));
			peerHandler.addAEnvoyer(sendBlock);
			piece.releaseMemory();

			// Pour le debit de upload
			peerHandler.sentBlock();
			torrent.sentBlock();

			System.out.println("Recu un request pour piece : " + r.getIndex()
					+ ", bloc : " + r.getBegin());
			peerHandler.multiplyNotation(1.001);
		} catch (FileNotFoundException e) {
			System.err
					.println("Fichier non trouvé pour envoyer un sendBlock, le message request sera ignoré!");
		}

	}

	@Override
	public void visit(NotInterested n) {
		// On doit preparer un message choke pour le pair qui nous envoie
		// cela, afin de eviter les requetes inutiles

		peerHandler.setInterested(false);
		peerHandler.addAEnvoyer(new Choke());

		peerHandler.multiplyNotation(1);

	}

	@Override
	public void visit(Have h) {
		// Ce message indique que le pair a la piece decrite dans le corps
		// du message (attribut pieceIndex de Have). Il faudra donc ajouter
		// cette piece a la liste des pieces que le pair possede
		peerHandler.addPeerPiece(h.getPieceIndex());
		peerHandler.multiplyNotation(1.0001);

	}

	@Override
	public void visit(Interested i) {
		// Si on recoit cela, l emetteur veut nous demander des pieces, il
		// faut donc (si on le veut bien sur) preparer un message Unchoke a
		// mettre dans la queue de messages

		peerHandler.setInterested(true);
		peerHandler.addAEnvoyer(new Unchoke());
	}

	@Override
	public void visit(BitField b) {
		// donne la liste des pieces que le pair emetteur possede.

		peerHandler.setPeerPiecesIndex(b.getPosessedPieces());
		peerHandler.multiplyNotation(Math.pow(1.0001, b.getNbPiece()));
	}

	@Override
	public void visit(SendBlock s) {
		/*
		 * On recoit un bloc. D abord on extrait la piece auquel appartient le
		 * bloc Puis on feed le bloc a la piece Ensuite on s assure que le
		 * PieceManager se rende compte qu une piece a ete recue
		 */
		Piece entering = null;
		entering = torrent.getPieceManager().getPiece(s.getPieceIndex());

		// pour le debit de download
		peerHandler.receivedBlock();
		torrent.receivedBlock();

		// pour la FunnyBar
		peerHandler
				.getPieceMgr()
				.getFunnyBar()
				.add((s.getPieceIndex()
						* torrent.getMetainfo().getPieceLength() + s
						.getBlocIndex()) / Piece.BLOCK_SIZE);

		entering.feed(s.getBlocIndex(), s.getBloc());
		peerHandler.removeRequest(new Request(s.getPieceIndex(), s
				.getBlocIndex(), s.getBloc().length));
		peerHandler.getPieceMgr().updatePriorities();
		peerHandler.multiplyNotation(1.01);
	}

	@Override
	public void visit(Unchoke u) {
		// quand on recoit cela, on peut faire des requests. Il faut aller
		// chercher quelle piece on veut requester dans notre updatePriorities
		// liste de pieces interessantes etc

		peerHandler.setChocking(false);
		peerHandler.multiplyNotation(1.01);
	}

	@Override
	public void visit(SendRSAKey s) {
		/*
		 * si on recoit ca on doit verifier que le message est bien de type
		 * SendRSAKey, sinon faire echouer le Handshake... Si cest correct , il
		 * faut faire baculer les Stream du peer vers des RSAStream avec la cle.
		 * ensuite on doit immediatement lire un message de type
		 * SendSymmetricKey.
		 */
		System.out.println("Recu une cle RSA publique ;)");
	}

	@Override
	public void visit(SendSymmetricKey s) {
		/*
		 * Si on recoit ca, il faut faire basculer les Streams associes au Peer
		 * vers des SymmetricStreams avec la cle adequate
		 */
		System.out.println("Recue cle symmetrique");
	}
}
