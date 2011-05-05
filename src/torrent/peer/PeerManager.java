package torrent.peer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import torrent.Torrent;

public class PeerManager extends Thread {
	private boolean finished;
	private Torrent torrent;
	private ArrayList<Peer> peerList;
	private ArrayBlockingQueue<PeerHandler> peerHandlers;
	private final static int maxNbPeer = 30;

	public PeerManager(Torrent torrent) {
		this.torrent = torrent;
		peerList = new ArrayList<Peer>();
		peerHandlers = new ArrayBlockingQueue<PeerHandler>(maxNbPeer);
	}

	public boolean addPeer(Peer peer) {
		synchronized (peerList) {

			if (peerList.contains(peer)) {
				return false;
			} else {
				peerList.add(peer);
				System.out.println("Nouveau pair : " + peer);
				return true;
			}
		}
	}

	public void addPeer(Socket s) {
		PeerHandler peerHandler = new PeerHandler(s, torrent);
		Peer peer = peerHandler.getPeer();
		PeerHandler lazyOne = getTheLazyOne();
		if (lazyOne.getNotation() < 5) {

			synchronized (peerList) {
				if (peerList.contains(peer)) {
					peerList.remove(peer);
				}
			}
			synchronized (peerHandlers) {
				try {
					peerHandlers.put(peerHandler);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {

		}

	}

	@Override
	public void run() {
		while (!finished) {
			update();
			if (!finished) {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void update() {
		PeerHandler lazyPeerHandler = getTheLazyOne();
		double minActivePeerNotation = lazyPeerHandler.getNotation();
		boolean trouve = false;
		Peer youngPeer = null;
		synchronized (peerList) {
			for (Peer peer : peerList) {
				if (peer.getNotation() > minActivePeerNotation) {
					Peer lazyPeer = lazyPeerHandler.getPeer();
					peerList.remove(peer);
					peerList.add(lazyPeer);
					youngPeer = peer;
					trouve = true;
					break;
				}
			}
		}
		if (trouve) {
			PeerHandler youngPeerHandler = new PeerHandler(youngPeer, torrent);
			synchronized (peerHandlers) {
				peerHandlers.remove(lazyPeerHandler);
				try {
					peerHandlers.put(youngPeerHandler);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lazyPeerHandler.finish();
			try {
				lazyPeerHandler.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void finish() {
		this.finished = true;
	}

	private PeerHandler getTheLazyOne() {
		double minActivePeerNotation = 10;
		PeerHandler lazyPeerHandler = null;
		synchronized (peerHandlers) {
			for (PeerHandler ph : peerHandlers) {
				double notation = ph.getNotation();
				if (notation < minActivePeerNotation) {
					minActivePeerNotation = notation;
					lazyPeerHandler = ph;
				}
			}
		}
		return lazyPeerHandler;
	}
}
