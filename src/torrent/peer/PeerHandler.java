package torrent.peer;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

import crypto.RSA.*;

import torrent.Torrent;
import torrent.messages.*;
import torrent.piece.*;
import settings.*;

/**
 * Cette classe s occupe de tout ce qui concerne le traffic avec un pair
 * particulier (il y a un PeerHandler par Peer) elle envoie et recoit des
 * messages.
 */
public class PeerHandler extends Thread {
	private boolean finished;
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
	private boolean amChocking = true;
	private boolean amInterested = false;
	private boolean isChocking = true; // est ce que le pair nous etrangle ?
	private boolean isInterested = false;
	private long lastTimeFlush;
	private PeerSettings settings;
	private static int requestRestrictions = 25;

	// initialise a 5;

	public PeerHandler(Peer peer, Torrent torrent) {
		this(torrent);
		this.peer = peer;

	}

	public PeerHandler(Socket socket, Torrent torrent) {
		this(torrent);
		this.socket = socket;
		this.peer = new Peer(socket.getInetAddress(), socket.getPort());

	}

	private PeerHandler(Torrent torrent) {
		this.messageHandler = new MessageHandler(this, torrent);
		this.peerPiecesIndex = new boolean[torrent.getPieces().length];
		this.aEnvoyer = new LinkedList<Message>();
		this.requetes = new LinkedList<Request>();
		this.requetesEnvoyee = new LinkedList<Request>();
		this.torrent = torrent;
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
				while (!finished) {
					readMessages();
					amMaybeInterested();
					prepareRequest();
					sendMessages();
					try {
						yield();
						sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				this.finish();
			}

		} catch (IOException e) {
			System.out.println("Peer deconnecte");
			peer.multiplyNotation(0.1);
			this.finish();
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
	 *            (int)
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
	 *            tableau de booleans pour indiquer quelles pieces le pair
	 *            possede (boolean[])
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
	 * enleve un requete de la queue de messages
	 * 
	 * @param requete
	 *            Request a enlever de la liste
	 */
	public void removeRequest(Request requete) {
		while (requetes.contains(requete)) {
			requetes.remove(requete);
		}
		while (requetesEnvoyee.contains(requete)) {
			requetesEnvoyee.remove(requete);
		}
	}

	/**
	 * verifie que le client a des pieces
	 * 
	 * @return true si oui, false sinon
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
		if (!finished) {
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

	}

	/**
	 * Cette methode s'occupe de faire le handshake avec le pair. Elle envoie d
	 * abord notre handshake, puis et teste si la reponse est correcte
	 * 
	 * @return true si le handshake a marche, false sinon
	 */
	private boolean shakeHands() throws IOException {
		Handshake ourHS = new Handshake(torrent);
		ourHS.send(output);

		Handshake theirHS = new Handshake(input);
		this.peer.setId(theirHS.getPeerID());
		if (theirHS.isEncryptionSupported()) {
			return shakeEncryptedHands(theirHS) && theirHS.isCompatible(ourHS);
		}
		return theirHS.isCompatible(ourHS);
	}

	private boolean shakeEncryptedHands(Handshake h) throws IOException {
		SendRSAKey ourRSA = new SendRSAKey();
		ourRSA.send(output);
		SendRSAKey theirRSA = new SendRSAKey(input);
		if (ourRSA.getId() != theirRSA.getId())
			return false;
		output = new DataOutputStream(new RSAOutputStream(
				theirRSA.getKeyPair(), output));
		input = new DataInputStream(new RSAInputStream(ourRSA.getKeyPair(),
				input));
		SendSymmetricKey ourSym = new SendSymmetricKey();
		ourSym.send(output);
		SendSymmetricKey theirSym = new SendSymmetricKey(input);
		if (ourSym.getId() != theirSym.getId())
			return false;
		output = new DataOutputStream(new SymmetricOutputStream(theirSym
				.getXORKey(), output));
		input = new DataInputStream(new SymmetricInputStream(
				ourSym.getXORKey(), input));
		return true;
	}

	/**
	 * lis tous les messages entrants et les traite
	 */
	private void readMessages() throws IOException {
		while (input.available() > 0) {
			Message message = messageReader.readMessage();
			if (message != null) {
				synchronized (messageHandler) {
					// accept: on traite le message
					message.accept(messageHandler);
				}
			}
		}

	}

	/**
	 * prepare une requete pour autant qu'il y en ait une a preparer ou qu'il y
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
			} else if (!isChocking && amInterested) {
				aEnvoyer.add(new NotInterested());
				amInterested = false;
			}

		} else if (requetesEnvoyee.size() == requestRestrictions) {
			long thisTime = System.currentTimeMillis();
			if ((thisTime - lastTimeFlush) > 10000) {
				peer.multiplyNotation(1 / 1.2);
				requetesEnvoyee.clear();
			}
			lastTimeFlush = thisTime;
		}
	}

	/**
	 * envoye un message de la queue de messages (s'il y en a) et un
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
		if (isChocking && !amInterested) {
			boolean interested;
			synchronized (pieceMgr) {
				interested = pieceMgr.getPieceOfInterest(peerPiecesIndex) != -1;
			}
			if (interested) {
				synchronized (output) {
					new Interested().send(output);
					amInterested = true;
				}

			}

		}
	}

	public double getNotation() {
		return peer.getNotation();
	}

	public void finish() {
		this.finished = true;
		try {
			if (this.output != null) {
				this.output.flush();
				this.output.close();
			}
			if (this.input != null) {
				this.input.close();
			}

		} catch (IOException e) {
			System.err.println("CHiééééééééééééééééééééééééé");
			e.printStackTrace();
		}

	}

	public Peer getPeer() {
		return peer;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeerHandler) {
			return ((PeerHandler) obj).getPeer().equals(peer);
		}
		return false;
	}

	public void newPieceHave(int i) {
		if (!finished) {
			synchronized (aEnvoyer) {
				aEnvoyer.addLast(new Have(i));
			}
		}
	}

	public void multiplyNotation(double d) {
		peer.multiplyNotation(d);
	}

}
