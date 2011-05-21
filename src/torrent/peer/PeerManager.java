package torrent.peer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;

import settings.GeneralSettings;
import torrent.Torrent;

public class PeerManager extends Thread {
	private Torrent torrent;
	private ArrayList<Peer> peerList = new ArrayList<Peer>(),
			connectedPeers = new ArrayList<Peer>();
	private ArrayList<PeerHandler> peerHandlers;
	private boolean encrytionEnabled = GeneralSettings.ENCRYPTION_ENABLED;

	public PeerManager(Torrent torrent) {
		this.torrent = torrent;
		peerHandlers = new ArrayList<PeerHandler>(
				GeneralSettings.NB_MAX_PEERHANDLERS);
	}

	public void addPeer(Peer peer) {
		boolean started = false;
		// System.out.println("PeerAjoute :" + peer);
		synchronized (peerHandlers) {
			if (peerHandlers.size() < GeneralSettings.NB_MAX_PEERHANDLERS
					&& !interrupted()) {
				PeerHandler peerHandler = new PeerHandler(peer, torrent,
						encrytionEnabled, this);
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
		PeerHandler peerHandler = new PeerHandler(s, torrent, encrytionEnabled,
				this);
		Peer peer = peerHandler.getPeer();
		PeerHandler lazyOne = getTheLazyOne();
		if ((lazyOne == null || lazyOne.getNotation() < 5) && !interrupted()) {

			synchronized (peerList) {
				if (peerList.contains(peer)) {
					peerList.remove(peer);
				}
			}
			synchronized (peerHandlers) {
				peerHandler.start();
				peerHandlers.add(peerHandler);

			}
		} else {
			try {
				s.close();
			} catch (IOException e) {
				System.err
						.println("Un pair s'est conncté et ne nous interesse pas, il veut pas se deconnecter le voyou!");
			}
		}

	}

	@Override
	public void run() {
		while (!interrupted()) {
			update();
			if (!interrupted()) {
				try {
					sleep(1000);
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
					if (peer.getNotation() > minActivePeerNotation
							&& !interrupted()) {
						Peer lazyPeer = lazyPeerHandler.getPeer();
						peerList.remove(peer);
						peerList.add(lazyPeer);
						youngPeer = peer;
						trouve = true;
						break;
					}
				}
			}
			if (trouve && !interrupted()) {
				PeerHandler youngPeerHandler = new PeerHandler(youngPeer,
						torrent, encrytionEnabled, this);
				synchronized (peerHandlers) {
					peerHandlers.remove(lazyPeerHandler);

					peerHandlers.add(youngPeerHandler);

				}
				lazyPeerHandler.interrupt();
				try {
					lazyPeerHandler.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void interrupt() {
		ListIterator<PeerHandler> iterator = peerHandlers.listIterator();
		while (iterator.hasNext()) {
			iterator.next().interrupt();
		}
		super.interrupt();
	}

	private PeerHandler getTheLazyOne() {
		if (!interrupted()) {
			double minActivePeerNotation = 10;
			PeerHandler lazyPeerHandler = null;
			synchronized (peerHandlers) {
				for (PeerHandler ph : peerHandlers) {
					double notation = ph.getNotation();
					if (notation < minActivePeerNotation && !interrupted()) {
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
		if (!interrupted()) {
			synchronized (peerHandlers) {
				for (PeerHandler ph : peerHandlers) {
					ph.newPieceHave(index);
				}
			}
		}

	}

	public ArrayList<Peer> getConnectedPeers() {
		return connectedPeers;
	}

	public void connect(Peer peer) {
		synchronized (connectedPeers) {
			connectedPeers.add(peer);
		}
	}

	public void disConnect(Peer peer) {
		synchronized (connectedPeers) {
			connectedPeers.remove(peer);
		}
	}

}
