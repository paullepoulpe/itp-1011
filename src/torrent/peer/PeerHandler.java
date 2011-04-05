package torrent.peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import torrent.messages.*;
import torrent.piece.*;

/*
 * Un PeerHandler par pair
 */
public class PeerHandler implements Runnable {
	private Socket socket;
	private Peer peer;
	private MessageReader messageReader;
	private MessageHandler messageHandler;
	private InputStream input;
	private OutputStream output;
	private PieceManager pieceMgr;
	private ArrayList<Piece> peerPieces;
	private LinkedList<Request> pendingRequests;
	private LinkedList<Message> queue;

	public void run() {
		if (socket == null) {
			try {
				// initialisation des streams
				socket = new Socket(peer.getIpAdress(), peer.getPort());
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// etablissement du Handshake
				Handshake ourHS = new Handshake(peer);
				output.write(ourHS.getHandshake());
				byte[] theirHandshake = new byte[49 + ourHS.getPstrLength()];
				input.read(theirHandshake);

				// test si le handshake est non nul
				if (!theirHandshake.equals(new byte[49 + ourHS.getPstrLength()])) {
					// ecrire un bitfield au client pour lui ondiquer quelles
					// pieces on a
					output.write(pieceMgr.generateBitField());
					// demarrer le thread KeepAlive, qui envoie des messages
					// KeepAlive toutes les 2 minutes
					KeepAlive kA = new KeepAlive(output);
					kA.start();
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
}
