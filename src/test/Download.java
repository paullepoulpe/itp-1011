package test;

import java.io.File;
import java.io.IOException;

import settings.PeerSettings;
import torrent.Torrent;
import torrent.peer.PeerAccepter;
import torrent.tracker.TrackerInfo;
import bencoding.InvalidBEncodingException;

public class Download {
	
	public Torrent torrent;

	/** Used for testing mostly - max number of peers we attempt to download from */
	public static int MAX_PEERS = 80;
	
	public static void main(String[] args) throws InvalidBEncodingException, IOException {
		new Download().run("data/LePetitPrince.torrent", false, true);
	}

	public void run(String path, boolean ownTracker, boolean enableEncryption) throws InvalidBEncodingException, IOException {
		torrent = new Torrent(new File(path));
		
		if(ownTracker) {
			torrent.getTrackers().clear();
			torrent.getTrackers().add(new TrackerInfo(torrent, "http://localhost:6969/announce"));
		}
		
		PeerSettings ps = new PeerSettings();
		if(enableEncryption) {
			ps.enableEncryption(PeerSettings.DEFAULT_RSA_KEY_LENGTH, PeerSettings.DEFAULT_SYMMETRIC_KEY_LENGTH);
		}
		new PeerAccepter(torrent, ps);
		torrent.massAnnounce(MAX_PEERS);
	}
}
