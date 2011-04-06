package torrent.peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import torrent.Torrent;
import torrent.messages.*;
import torrent.piece.*;

/*
 * Un PeerHandler par pair
 */
public class PeerHandler extends Thread {
	private Socket socket;
	private Peer peer;
	private Torrent torrent;
	private MessageReader messageReader;
	private MessageHandler messageHandler;
	private DataInputStream input;
	private DataOutputStream output;
	private PieceManager pieceMgr;
	private boolean[] peerPiecesIndex;
	private LinkedList<Message> aTraiter;
	private LinkedList<Message> aEnvoyer;
	private LinkedList<Request> requetes;
	private boolean amChocking;
	private boolean amInterested;
	private boolean isChocking;
	private boolean isInterested;

	public PeerHandler(Peer peer, Torrent torrent) {
		this.peer = peer;
		this.messageHandler = new MessageHandler(this, torrent);
		this.peerPiecesIndex = new boolean[torrent.getPieces().length];
		this.aTraiter = new LinkedList<Message>();
		this.aEnvoyer = new LinkedList<Message>();
		this.torrent = torrent;
		this.amChocking = true;
		this.amInterested = false;
		this.isChocking = true;
		this.isInterested = false;
		this.pieceMgr = torrent.getPieceManager();

	}

	public void run() {
		if (socket == null) {
			try {
				// initialisation des streams
				boolean connect = false;
				while (!connect) {
					try {
						socket = new Socket(peer.getIpAdress(), peer.getPort());
						connect = true;
					} catch (IOException e) {
						connect = false;
					}
				}
				System.out.println("Connection a " + peer.getIpAdress()
						+ " reussie!");
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());

				// etablissement du Handshake
				Handshake ourHS = new Handshake(peer, torrent);
				ourHS.send(output);
				Handshake theirHS = new Handshake(input);
				System.out.println("Handshake recu");

				// test si le handshake est non nul
				if (theirHS.isCompatible(ourHS)) {
					this.peer.setId(theirHS.getPeerID().toString());
					// ecrire un bitfield au client pour lui indiquer quelles
					// pieces on a
					BitField bitField = new BitField(torrent);
					bitField.send(output);

					// demarrer le thread KeepAlive, qui envoie des messages
					// KeepAlive toutes les 2 minutes
					KeepAlive kA = new KeepAlive(output);
					kA.start();
					this.messageReader = new MessageReader(this, input);
					messageReader.start();

					while (true) {
						if (!aTraiter.isEmpty()) {
							aTraiter.getFirst().accept(messageHandler);
							aTraiter.removeFirst();
						}
						if (requetes.size() < 10 && !isChocking) {
							int index = pieceMgr.getPieceOfInterest();
							int begin = torrent.getPieces()[index]
									.getBlockOfInterest(this);
							requetes.add(new Request(index, begin,
									Piece.BLOCK_SIZE));
						}
						if (!aEnvoyer.isEmpty()) {
							aEnvoyer.getFirst().send(output);
							aEnvoyer.removeFirst();
						}
						if (!isChocking && !requetes.isEmpty()) {
							requetes.getFirst().send(output);
							requetes.removeFirst();
						}

					}
					// preparer des requetes (max 10 normalement)
					/*
					 * 1 demander au PieceMng quelle piece on doi requeter (si
					 * yen a pas, arreter) 2 envoyer un interested si le pair
					 * est en etat de Choking (seulement si on avait pas deja
					 * envoye de Interested) 3 une fois quon est plus etrangle,
					 * on envoie une requete dans la queue des messages
					 */

				} else {
					input.close();
					output.close();
					socket.close();
					// il faut arreter lexecution du thread
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// il faut arreter l'execution du thread
		}
	}

	public void setAmChocking(boolean amChocking) {
		this.amChocking = amChocking;
	}

	public void setAmInterested(boolean amInterested) {
		this.amInterested = amInterested;
	}

	public void setInterested(boolean isInterested) {
		this.isInterested = isInterested;
	}

	public void setChocking(boolean isChocking) {
		this.isChocking = isChocking;
	}

	public void addPeerPiece(int index) {
		peerPiecesIndex[index] = true;
	}

	public PieceManager getPieceMgr() {
		return pieceMgr;
	}

	public void setPeerPiecesIndex(boolean[] peerPiecesIndex) {
		for (int i = 0; i < this.peerPiecesIndex.length; i++) {
			this.peerPiecesIndex[i] = peerPiecesIndex[i];
		}
	}

	public void addATraiter(Message message) {
		aTraiter.addLast(message);
	}

	public void addAEnvoer(Message message) {
		aEnvoyer.addLast(message);
	}

	public void removeRequest(int index, int begin) {
		for (int i = 0; i < requetes.size(); i++) {
			if ((requetes.get(i)).getBegin() == begin
					&& requetes.get(i).getIndex() == index) {
				requetes.remove(i);
			}
		}
	}
}
