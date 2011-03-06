package torrent;

import java.io.File;

import torrent.peer.*;

public class Torrent {
	private Peer[] list;
	private File metainfo;
	private int numPort;
	
	public Torrent(File metainfo, int numPort) {
		this.metainfo = metainfo;
		this.numPort = numPort;
	}
	public Torrent(File metainfo){
		this.metainfo = metainfo;
		this.numPort = 6881 + (int)(Math.random()*30001);
	}

	public void massAnnounce() {

	}
}
