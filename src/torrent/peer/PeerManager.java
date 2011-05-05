package torrent.peer;

import java.net.Socket;
import java.util.ArrayList;

import torrent.Torrent;

public class PeerManager extends Thread {
	private boolean finished;
	private Torrent torrent;
	private ArrayList<Peer> peerList;
	private ArrayList<PeerHandler> peerHandlers;
	private final static int maxNbPeer = 30;

	public PeerManager(Torrent torrent) {
		this.torrent = torrent;
		peerList = new ArrayList<Peer>();
		peerHandlers = new ArrayList<PeerHandler>(maxNbPeer);
	}

	// Ajoute un peer, sans le peer handler associé
	public void addPeer(Peer peer) {
		synchronized (peerList) {

			if (peerList.contains(peer)) {
			} else {
				peerList.add(peer);
				System.out.println("Nouveau pair : " + peer);
			}
		}
		synchronized (peerHandlers) {
			if (peerHandlers.size() < maxNbPeer) {
				PeerHandler newPeerHandler = new PeerHandler(peer, torrent);
				newPeerHandler.start();
				peerHandlers.add(newPeerHandler);
			}
		}
	}

	// ajoute un peer qui s'est connecté avec nous, donc le peerHandler associé,
	// sauf si les pairs qu'on a sont super
	public void addPeer(Socket s) {
		PeerHandler newPeerHandler = new PeerHandler(s, torrent);
		newPeerHandler.start();
		Peer peer = newPeerHandler.getPeer();
		PeerHandler lazyOne = getTheLazyOne();
		if (lazyOne.getNotation() < 5) {
			synchronized (peerList) {
				peerList.remove(peer);
				peerList.add(lazyOne.getPeer());
			}
			synchronized (peerHandlers) {
				peerHandlers.remove(lazyOne);
				peerHandlers.add(newPeerHandler);

			}
			lazyOne.finish();
			try {
				lazyOne.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			newPeerHandler.finish();
			try {
				newPeerHandler.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (peerList) {
				if (!peerList.contains(peer)) {
					peerList.add(peer);
				}
			}
		}

	}

	// update la liste toutes les demi-secondes
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

	// regarde si nos pairactifs sont parresseux, et en met un nouveau si c'est
	// le cas

	private void update() {
		PeerHandler lazyPeerHandler = getTheLazyOne();
		double minActivePeerNotation = 10;
		if (lazyPeerHandler != null) {
			minActivePeerNotation = lazyPeerHandler.getNotation();
		}

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
			youngPeerHandler.start();
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

	// termine le thread
	public void finish() {
		this.finished = true;
	}

	// trouve le plus parresseux des peerhandlers
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
		if (lazyPeerHandler != null) {
			System.out.println("Ce pair est un noob => "
					+ lazyPeerHandler.getPeer().toString() + " "
					+ lazyPeerHandler.getNotation());
		}
		return lazyPeerHandler;
	}
}
