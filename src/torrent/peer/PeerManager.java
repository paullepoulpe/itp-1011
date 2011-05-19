package torrent.peer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

import settings.GeneralSettings;
import torrent.Torrent;

public class PeerManager extends Thread {
	private boolean finished;
	private Torrent torrent;
	private ArrayList<Peer> peerList;
	private ArrayList<PeerHandler> peerHandlers;
	private boolean encrytionEnabled = GeneralSettings.ENCRYPTION_ENABLED;

	public PeerManager(Torrent torrent) {
		this.torrent = torrent;
		peerList = new ArrayList<Peer>();
		peerHandlers = new ArrayList<PeerHandler>(
				GeneralSettings.NB_MAX_PEERHANDLERS);
	}

	public void addPeer(Peer peer) {
		boolean started = false;
		// System.out.println("PeerAjouté :" + peer);
		synchronized (peerHandlers) {
			if (peerHandlers.size() < GeneralSettings.NB_MAX_PEERHANDLERS
					&& !finished) {
				PeerHandler peerHandler = new PeerHandler(peer, torrent,
						encrytionEnabled);
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
		PeerHandler peerHandler = new PeerHandler(s, torrent, encrytionEnabled);
		Peer peer = peerHandler.getPeer();
		PeerHandler lazyOne = getTheLazyOne();
		if (lazyOne.getNotation() < 5 && !finished) {

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
			if (!finished) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		ListIterator<PeerHandler> iterator = peerHandlers.listIterator();
		while (iterator.hasNext()) {
			iterator.next().finish();
			iterator.remove();
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
					if (peer.getNotation() > minActivePeerNotation && !finished) {
						Peer lazyPeer = lazyPeerHandler.getPeer();
						peerList.remove(peer);
						peerList.add(lazyPeer);
						youngPeer = peer;
						trouve = true;
						break;
					}
				}
			}
			if (trouve && !finished) {
				PeerHandler youngPeerHandler = new PeerHandler(youngPeer,
						torrent, encrytionEnabled);
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
		if (!finished) {
			double minActivePeerNotation = 10;
			PeerHandler lazyPeerHandler = null;
			synchronized (peerHandlers) {
				for (PeerHandler ph : peerHandlers) {
					double notation = ph.getNotation();
					if (notation < minActivePeerNotation && !finished) {
						minActivePeerNotation = notation;
						lazyPeerHandler = ph;
					}
				}
			}

			return lazyPeerHandler;
		}
		return null;
	}

	public void notifyPeerHandlers(int index) {
		if (!finished) {
			synchronized (peerHandlers) {
				for (PeerHandler ph : peerHandlers) {
					ph.newPieceHave(index);
				}
			}
		}

	}
}
