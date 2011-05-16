package torrent.peer;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import crypto.KeyGenerator;
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
	private LinkedList<Message> aEnvoyer = new LinkedList<Message>();
	private LinkedList<Request> requetes = new LinkedList<Request>();
	private LinkedList<Request> requetesEnvoyee = new LinkedList<Request>();
	private boolean amChocking = true;
	private boolean amInterested = false;
	private boolean isChocking = true;
	private boolean isInterested = false;
	private boolean connecte = false;
	private boolean encryptionEnabled = false;
	private long lastTimeFlush;
	private PeerSettings settings;

	public PeerHandler(Peer peer, Torrent torrent, boolean encryptionEnabled) {
		this(torrent, encryptionEnabled);
		this.peer = peer;

	}

	public PeerHandler(Socket socket, Torrent torrent, boolean encryptionEnabled) {
		this(torrent, encryptionEnabled);
		this.socket = socket;
		this.peer = new Peer(socket.getInetAddress(), socket.getPort());

	}

	private PeerHandler(Torrent torrent, boolean encryptionEnabled) {
		this.settings = new PeerSettings();
		this.encryptionEnabled = encryptionEnabled;
		this.messageHandler = new MessageHandler(this, torrent);
		this.pieceMgr = torrent.getPieceManager();
		this.peerPiecesIndex = new boolean[pieceMgr.getNbPieces()];
		this.torrent = torrent;

	}

	public void run() {
		try {
			// initialisation des streams
			initStreams();
			// System.out.println("Connection a " + peer.getIpAdress()
			// + " reussie!");

			// etablissement du Handshake
			boolean isCompatible = shakeHands();

			// test si le handshake est compatible
			if (isCompatible) {
				if (encryptionEnabled) {
					shareKeys();
				}
				// System.out.println("ID du pair : " + this.peer.getId());

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
		System.err.println(this.getId());
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
	public void addAEnvoyer(Message message) {
		if (!finished) {
			aEnvoyer.addLast(message);
		}
	}

	/**
	 * enleve un requete de la queue de messages
	 * 
	 * @param requete
	 *            Request a enlever de la liste
	 */
	public void removeRequest(Request requete) {

		while (requetes.contains(requete)) {
			boolean b = requetes.remove(requete);
			System.out.println("J'enleve une requete de requetes : " + b);
		}

		while (requetesEnvoyee.contains(requete)) {
			boolean b = requetesEnvoyee.remove(requete);
			System.out.println("J'enleve une requete de requetesEnvoyées : "
					+ b);
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
		long timeStarted = System.currentTimeMillis();
		while (!connecte && !finished) {
			if (socket == null) {
				try {
					socket = new Socket(peer.getIpAdress(), peer.getPort());
					input = new DataInputStream(socket.getInputStream());
					output = new DataOutputStream(socket.getOutputStream());
					connecte = true;
				} catch (IOException e) {
					connecte = false;
					socket = null;
				}
			} else {
				try {
					input = new DataInputStream(socket.getInputStream());
					output = new DataOutputStream(socket.getOutputStream());
					connecte = true;
				} catch (IOException e) {
					connecte = false;
					socket = null;
				}
			}
			if (!connecte && !finished) {
				if (System.currentTimeMillis() - timeStarted > GeneralSettings.PEER_RESPONSE_DELAY) {
					peer.multiplyNotation(1 / 3);
				}
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
	 * Cette methode s'occupe de faire le handshake avec le pair. Elle envoie d
	 * abord notre handshake, puis et teste si la reponse est correcte
	 * 
	 * @return true si le handshake a marche, false sinon
	 */
	private boolean shakeHands() throws IOException {
		if (!finished) {
			Handshake ourHS = new Handshake(torrent);
			System.out.println("Encryption enabled :" + encryptionEnabled);

			if (encryptionEnabled) {
				ourHS.setEncryptionEnabled();
			}
			System.out.println(Arrays.toString(ourHS.getReserved()));

			ourHS.send(output);
			Handshake theirHS = new Handshake(input);
			System.out.println(Arrays.toString(theirHS.getReserved()));
			this.peer.setId(theirHS.getPeerID());

			return theirHS.isCompatible(ourHS);
		}
		return false;

	}

	private boolean shareKeys() throws IOException {
		System.out.println("Encryption supportée, echange de clés");
		KeyPair myKey = KeyGenerator.generateRSAKeyPair(settings
				.getPrivateRSAModLength());

		SendRSAKey ourRSA = new SendRSAKey(myKey);

		input = new DataInputStream(new RSAInputStream(myKey, input));
		output = new DataOutputStream(new RSAOutputStream(myKey, output));
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
		while (input.available() > 0 && !finished) {

			Message message = messageReader.readMessage();
			if (message != null) {
				System.out.println("Je Lis des Messages" + message.getClass());
				// accept: on traite le message
				message.accept(messageHandler);
			}
		}

	}

	/**
	 * prepare une requete pour autant qu'il y en ait une a preparer ou qu'il y
	 * ait moins de 10 requetes pendantes
	 */
	private void prepareRequest() {
		if (requetes.size() + requetesEnvoyee.size() < GeneralSettings.NB_MAX_REQUESTS
				&& !hasNoPieces() && !finished) {
			int index = -1;
			index = pieceMgr.getPieceOfInterest(peerPiecesIndex);
			System.out.println("Je prepare requete :" + index);
			if (index != -1) {
				Piece wanted = pieceMgr.getPiece(index);
				requetes.add(wanted.getBlockOfInterest(this));
			} else if (!isChocking && amInterested) {
				aEnvoyer.add(new NotInterested());
				amInterested = false;
			}

		} else if (requetesEnvoyee.size() >= GeneralSettings.NB_MAX_REQUESTS
				&& !finished) {
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
		if (!aEnvoyer.isEmpty() && !finished) {
			synchronized (output) {
				aEnvoyer.removeFirst().send(output);
				System.out.println("J'envoie un message");
			}
		}

		if (!isChocking && !requetes.isEmpty() && !finished) {
			synchronized (output) {
				requetes.getFirst().send(output);
				System.out.println("J'envoie une requete");
			}
			requetesEnvoyee.addLast(requetes.removeFirst());
		}
	}

	/**
	 * envoye un interested si le peer a des chose interessantes a donner et
	 * qu'il nous choke
	 */
	private void amMaybeInterested() throws IOException {
		if (isChocking && !amInterested && !finished) {
			boolean interested;
			interested = pieceMgr.getPieceOfInterest(peerPiecesIndex) != -1;
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
		connecte = false;
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
			aEnvoyer.addLast(new Have(i));
		}
	}

	public void multiplyNotation(double d) {
		peer.multiplyNotation(d);
	}

}
