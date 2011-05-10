package torrent.peer;

import java.net.Socket;
import java.util.ArrayList;

import torrent.Torrent;

public class PeerManager extends Thread {
	private boolean finished;
	private Torrent torrent;
	private ArrayList<Peer> peerList;
	private ArrayList<PeerHandler> peerHandlers;
	private final static int MAX_NB_PEERS = 100;

	public PeerManager(Torrent torrent) {
		this.torrent = torrent;
		peerList = new ArrayList<Peer>();
		peerHandlers = new ArrayList<PeerHandler>(MAX_NB_PEERS);
	}

	public void addPeer(Peer peer) {
		boolean started = false;
		synchronized (peerHandlers) {
			if (peerHandlers.size() < MAX_NB_PEERS) {
				PeerHandler peerHandler = new PeerHandler(peer, torrent);
				peerHandler.start();
				started = true;
				peerHandlers.add(peerHandler);
			}
		}
		if (!started) {
			synchronized (peerList) {
				if (!peerList.contains(peer)) {
					peerList.add(peer);
				}
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

				peerHandlers.add(peerHandler);

			}
		} else {

		}

	}

	@Override
	public void run() {
		while (!finished) {
			update();
			System.out.println("Peer Handlers Actifs : " + peerHandlers.size());
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
		if (lazyPeerHandler != null) {

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
				PeerHandler youngPeerHandler = new PeerHandler(youngPeer,
						torrent);
				synchronized (peerHandlers) {
					peerHandlers.remove(lazyPeerHandler);

					peerHandlers.add(youngPeerHandler);

				}
				lazyPeerHandler.finish();
				try {
					lazyPeerHandler.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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

	public void notifyPeerHandlers(int index) {
		synchronized (peerHandlers) {
			for (PeerHandler ph : peerHandlers) {
				ph.newPieceHave(index);
			}
		}

	}
}
