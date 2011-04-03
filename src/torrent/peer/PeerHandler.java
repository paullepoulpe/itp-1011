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
		/*
		 * Choses a faire :
		 *  - initialiser le socket relaif au pair (dabord tester si il existe deja,
		 *    dans quel cas il ne faut plus rien faire) sinon il faut initialiser
		 *    les flots entrant et sortant.
		 *  -  
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		if (socket == null){
			try {
				//initialisation des streams
				socket = new Socket(peer.getIpAdress(), peer.getPort());
				input = socket.getInputStream();
				output = socket.getOutputStream();
				
				//etablissement du Handshake
				Handshake ourHS = new Handshake(peer);
				output.write(ourHS.getHandshake());
				byte[] theirHandshake = new byte[49+ourHS.getLength()];
				input.read(theirHandshake);
				
				//test si le handshake est non nul
				if(!theirHandshake.equals(new byte[49+ourHS.getLength()])){
					
				}else{
					input.close();
					output.close();
					socket.close();
					//il faut arreter lexecution du thread
				}
				
			}catch (IOException e){
				e.printStackTrace();
			}
			
		} else{
			// il faut arreter l'execution du thread
		}
	}
}
