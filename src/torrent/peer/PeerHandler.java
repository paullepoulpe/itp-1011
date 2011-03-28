package torrent.peer;

import java.net.Socket;

import torrent.messages.*;

public class PeerHandler implements Runnable {
	private Socket socket;
	private Peer peer;
	private MessageReader messageHandler;

	public void run() {

	}
}
