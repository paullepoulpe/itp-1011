package torrent.peer;

import gui.DynamicFlowLabel;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

import crypto.KeyGenerator;
import crypto.RSA.*;
import crypto.XOR.*;

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
	private DynamicFlowLabel upload = new DynamicFlowLabel(),
			download = new DynamicFlowLabel();
	private PeerManager peerManager;

	public PeerHandler(Peer peer, Torrent torrent, boolean encryptionEnabled,
			PeerManager peerManager) {
		this(torrent, encryptionEnabled, peerManager);
		this.peer = peer;

	}

	public PeerHandler(Socket socket, Torrent torrent,
			boolean encryptionEnabled, PeerManager peerManager) {
		this(torrent, encryptionEnabled, peerManager);
		this.socket = socket;
		this.peer = new Peer(socket.getInetAddress(), socket.getPort());

	}

	private PeerHandler(Torrent torrent, boolean encryptionEnabled,
			PeerManager peerManager) {
		this.encryptionEnabled = encryptionEnabled;
		this.messageHandler = new MessageHandler(this, torrent);
		this.pieceMgr = torrent.getPieceManager();
		this.peerPiecesIndex = new boolean[pieceMgr.getNbPieces()];
		this.torrent = torrent;
		this.peerManager = peerManager;

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
				peerManager.connect(this);
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
				while (!interrupted()) {
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
			}

		} catch (IOException e) {
			peer.multiplyNotation(0.1);
		}
		System.out.println("Peer deconnecte");
		interrupt();

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
	public void addAEnvoyer(Message message) {
		if (!interrupted()) {
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

		while (requetes.contains(requete) && !interrupted()) {
			requetes.remove(requete);
		}

		while (requetesEnvoyee.contains(requete) && !interrupted()) {
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
		long timeStarted = System.currentTimeMillis();
		while (!connecte && !interrupted()) {
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
			if (!connecte && !interrupted()) {
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
		if (!interrupted()) {
			Handshake ourHS = new Handshake(torrent);
			if (encryptionEnabled) {
				ourHS.setEncryptionEnabled();
			}
			ourHS.send(output);
			if (!interrupted()) {
				Handshake theirHS = new Handshake(input);
				this.peer.setId(theirHS.getPeerID());
				return theirHS.isCompatible(ourHS);
			}
		}
		return false;

	}

	/**
	 * Cette methode est vitale lors du protocole de HandShake encrypte
	 * 
	 * @return true si le hanshake encrypte s'est bien deroule, false sinon
	 * @throws IOException
	 *             s'il y a un quelconque probleme avec les flux
	 */
	private boolean shareKeys() throws IOException {
		try {

			System.out.println("Encryption supportee, echange de cles");
			KeyPair myKey = KeyGenerator
					.generateRSAKeyPair(GeneralSettings.RSA_KEY_SIZE);

			SendRSAKey ourRSA = new SendRSAKey(myKey);
			ourRSA.send(output);
			SendRSAKey theirRSA = new SendRSAKey(input);
			System.out.println("Ma cleRSA : " + ourRSA);
			System.out.println("Sa cleRSA : " + theirRSA);

			DataInputStream in = new DataInputStream(new RSAInputStream(myKey,
					input));
			DataOutputStream out = new DataOutputStream(new RSAOutputStream(
					theirRSA.getKeyPair(), output));

			SendSymmetricKey ourSym = new SendSymmetricKey();
			System.out.println("Ma cle XOR" + ourSym);
			ourSym.send(out);
			SendSymmetricKey theirSym = new SendSymmetricKey(in);
			System.out.println("Sa cle XOR" + theirSym);
			if (ourSym.getId() != theirSym.getId()) {
				System.err.println("Identifiants de symKey faux");
				return false;
			}

			output = new DataOutputStream(new SymmetricOutputStream(
					theirSym.getXORKey(), output));
			input = new DataInputStream(new SymmetricInputStream(
					ourSym.getXORKey(), input));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * lis tous les messages entrants et les traite
	 */
	private void readMessages() throws IOException {
		int nbRead = 0;
		while (input.available() > 0 && !interrupted() && nbRead <= 10) {
			nbRead++;
			Message message = messageReader.readMessage();
			if (message != null && !interrupted()) {
				// System.out.println("Je Lis des Messages" +
				// message.getClass());
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
				&& !hasNoPieces() && !interrupted()) {
			int index = -1;
			index = pieceMgr.getPieceOfInterest(peerPiecesIndex);
			// System.out.println("Je prepare requete :" + index);
			if (index != -1) {
				Piece wanted = pieceMgr.getPiece(index);
				requetes.add(wanted.getBlockOfInterest(this));
			} else if (!isChocking && amInterested) {
				aEnvoyer.add(new NotInterested());
				amInterested = false;
			}

		} else if (requetesEnvoyee.size() >= GeneralSettings.NB_MAX_REQUESTS
				&& !interrupted()) {
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
		if (!aEnvoyer.isEmpty() && !interrupted()) {
			synchronized (output) {
				aEnvoyer.removeFirst().send(output);
				// System.out.println("J'envoie un message");
			}
		}

		if (!isChocking && !requetes.isEmpty() && !interrupted()) {
			Request requete = requetes.removeFirst();
			synchronized (output) {
				requete.send(output);
				// System.out.println("J'envoie une requete");
			}
			requetesEnvoyee.addLast(requete);
		}
	}

	/**
	 * envoye un interested si le peer a des chose interessantes a donner et
	 * qu'il nous choke
	 */
	private void amMaybeInterested() throws IOException {
		if (isChocking && !amInterested && !interrupted()) {
			boolean interested;
			interested = pieceMgr.getPieceOfInterest(peerPiecesIndex) != -1;
			if (interested && !interrupted()) {
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

	@Override
	public void interrupt() {
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
			System.err.println("Probleme de fermeture de sockets");
		}
		peerManager.disConnect(this);
		super.interrupt();
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
		if (!interrupted()) {
			aEnvoyer.addLast(new Have(i));
		}
	}

	public void multiplyNotation(double d) {
		peer.multiplyNotation(d);
	}

	public void receivedBlock() {
		download.add(Piece.BLOCK_SIZE);
	}

	public void sentBlock() {
		upload.add(Piece.BLOCK_SIZE);
	}

	public DynamicFlowLabel getDownload() {
		return download;
	}

	public DynamicFlowLabel getUpload() {
		return upload;
	}

}
