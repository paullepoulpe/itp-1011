package torrent;

import java.io.File;

import torrent.peer.*;

public class Torrent {
	private Peer[] list;
	protected File metainfo;
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
	public File getMetaInfo(){
		return this.metainfo;
	}
}
