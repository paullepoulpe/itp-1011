package torrent.peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
	private LinkedList<Message> aEnvoyer;
	private LinkedList<Request> requetes;
	private LinkedList<Request> requetesEnvoyee;
	private boolean amChocking;
	private boolean amInterested;
	private boolean isChocking;
	private boolean isInterested;
	private long lastTimeFlush;
	private static int requestRestrictions = 15;

	public PeerHandler(Peer peer, Torrent torrent) {
		this.peer = peer;
		this.messageHandler = new MessageHandler(this, torrent);
		this.peerPiecesIndex = new boolean[torrent.getPieces().length];
		this.aEnvoyer = new LinkedList<Message>();
		this.requetes = new LinkedList<Request>();
		this.requetesEnvoyee = new LinkedList<Request>();
		this.torrent = torrent;
		this.amChocking = true;
		this.amInterested = false;
		this.isChocking = true;
		this.isInterested = false;
		this.pieceMgr = torrent.getPieceManager();

	}

	public PeerHandler(Socket socket, Torrent torrent) {
		this.socket = socket;
		this.messageHandler = new MessageHandler(this, torrent);
		this.peerPiecesIndex = new boolean[torrent.getPieces().length];
		this.aEnvoyer = new LinkedList<Message>();
		this.requetes = new LinkedList<Request>();
		this.requetesEnvoyee = new LinkedList<Request>();
		this.torrent = torrent;
		this.amChocking = true;
		this.amInterested = false;
		this.isChocking = true;
		this.isInterested = false;
		this.pieceMgr = torrent.getPieceManager();

	}

	public void run() {
		try {
			// initialisation des streams
			initStreams();

			System.out.println("Connection a " + peer.getIpAdress()
					+ " reussie!");

			// etablissement du Handshake
			boolean isCompatible = shakeHands();

			// test si le handshake est compatible
			if (isCompatible) {

				System.out.println("ID du pair : " + this.peer.getId());

				// ecrire un bitfield au client pour lui indiquer quelles
				// pieces on a
				BitField bitField = new BitField(torrent);

				synchronized (output) {
					bitField.send(output);
				}

				this.messageReader = new MessageReader(input);

				// demarrer le thread KeepAlive, qui envoie des messages
				// KeepAlive toutes les 2 minutes
				KeepAlive kA = new KeepAlive(output);
				kA.start();
				lastTimeFlush = System.currentTimeMillis();
				while (true) {
					readMessages();
					amMaybeInterested();
					prepareRequest();
					sendMessages();
					try {
						sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				// preparer des requetes (max 10 normalement)
				/*
				 * 1 demander au PieceMng quelle piece on doi requeter (si yen a
				 * pas, arreter) 2 envoyer un interested si le pair est en etat
				 * de Choking (seulement si on avait pas deja envoye de
				 * Interested) 3 une fois quon est plus etrangle, on envoie une
				 * requete dans la queue des messages
				 */

			} else {
				input.close();
				output.close();
				socket.close();
				System.out.println("Peer deconnecté");
				this.interrupt();
				// il faut arreter lexecution du thread
			}

		} catch (IOException e) {
			System.out.println("Peer deconnecté");
			this.interrupt();
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

	/**
	 * ajoute une piece que le peer a
	 * 
	 * @param index
	 */
	public void addPeerPiece(int index) {
		peerPiecesIndex[index] = true;
	}

	public PieceManager getPieceMgr() {
		return pieceMgr;
	}

	/**
	 * initialise la liste des pieces que le peer a
	 * 
	 * @param peerPiecesIndex
	 */
	public void setPeerPiecesIndex(boolean[] peerPiecesIndex) {
		for (int i = 0; i < this.peerPiecesIndex.length; i++) {
			this.peerPiecesIndex[i] = peerPiecesIndex[i];
		}
	}

	/**
	 * ajoute un message a envoyer
	 * 
	 * @param message
	 */
	public void addAEnvoer(Message message) {
		aEnvoyer.addLast(message);
	}

	/**
	 * enleve une requete
	 * 
	 * @param index
	 * @param begin
	 */
	public void removeRequest(Request requete) {
		for (int i = 0; i < requetes.size(); i++) {
			if (requetes.get(i).equals(requete)) {
				requetes.remove(i);
			}
		}
		for (int i = 0; i < requetesEnvoyee.size(); i++) {
			if (requetesEnvoyee.get(i).equals(requete)) {
				requetesEnvoyee.remove(i);
			}
		}
	}

	/**
	 * verifie que le client a des pieces
	 * 
	 * @return
	 */
	private boolean hasNoPieces() {
		boolean noPiece = true;
		for (int i = 0; i < peerPiecesIndex.length; i++) {
			noPiece = noPiece && !peerPiecesIndex[i];
		}
		return noPiece;
	}

	/**
	 * initialise les streams
	 */
	private void initStreams() {

		boolean connect = false;
		while (!connect) {
			if (socket == null) {

				try {
					socket = new Socket(peer.getIpAdress(), peer.getPort());
					input = new DataInputStream(socket.getInputStream());
					output = new DataOutputStream(socket.getOutputStream());
					connect = true;
				} catch (IOException e) {
					connect = false;
					socket = null;
				}
			} else {
				try {
					input = new DataInputStream(socket.getInputStream());
					output = new DataOutputStream(socket.getOutputStream());
					this.peer.setPort(socket.getPort());
					this.peer.setInet(socket.getInetAddress());
					connect = true;
				} catch (IOException e) {
					connect = false;
					socket = null;
				}
			}
			if (!connect) {
				yield();
				try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * fais le handshake et teste s'il est correct
	 * 
	 * @return
	 */
	private boolean shakeHands() throws IOException {
		Handshake ourHS = new Handshake(torrent);
		ourHS.send(output);

		Handshake theirHS = new Handshake(input);
		this.peer.setId(theirHS.getPeerID());

		return theirHS.isCompatible(ourHS);
	}

	/**
	 * lis tous les messages entrants et les traites
	 */
	private void readMessages() throws IOException {

		while (input.available() > 0) {

			Message message = messageReader.readMessage();
			if (message != null) {
				message.accept(messageHandler);
			}
		}

	}

	/**
	 * prepare une reequete pour autant qu'il y en ai une a preparer oou qu'il y
	 * ait moins de 10 requetes pendantes
	 */
	private void prepareRequest() {
		if (requetes.size() + requetesEnvoyee.size() < requestRestrictions
				&& !hasNoPieces()) {
			int index = -1;
			synchronized (pieceMgr) {
				index = pieceMgr.getPieceOfInterest(peerPiecesIndex);
			}
			if (index != -1) {
				Piece wanted = torrent.getPieces()[index];
				requetes.add(wanted.getBlockOfInterest(this));
			}
		} else if (requetesEnvoyee.size() == requestRestrictions) {
			long thisTime = System.currentTimeMillis();
			if ((thisTime - lastTimeFlush) > 10000) {
				requetesEnvoyee.removeAll(requetesEnvoyee);
			}
			lastTimeFlush = thisTime;
		}
	}

	/**
	 * envoye un message de la queue de messages (s'il y en a ) et un
	 * request(s'il y en a)
	 */
	private void sendMessages() throws IOException {
		if (!aEnvoyer.isEmpty()) {
			synchronized (output) {
				aEnvoyer.getFirst().send(output);
			}
			aEnvoyer.removeFirst();
		}

		if (!isChocking && !requetes.isEmpty()) {
			synchronized (output) {
				requetes.getFirst().send(output);
			}
			requetesEnvoyee.addLast(requetes.getFirst());
			requetes.removeFirst();
		}
	}

	/**
	 * envoye un interested si le peer a des chose interessantes a donner et
	 * qu'il nous choke
	 */
	private void amMaybeInterested() throws IOException {
		if (isChocking && !amInterested && !requetes.isEmpty()) {
			synchronized (output) {
				new Interested().send(output);
				amInterested = true;
			}

		}
	}
}
