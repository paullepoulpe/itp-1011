package torrent.peer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import torrent.messages.*;
import torrent.piece.*;

public class PeerHandler implements Runnable {
	private Socket socket;
	private Peer peer;
	private MessageReader messageHandler;
	private InputStream input;
	private OutputStream output;
	private PieceManager pieceMgr;
	private ArrayList<Piece> peerPieces;
	private LinkedList<Request> pendingRequests;
	private LinkedList<Message> queue;

	public void run() {

	}
}
