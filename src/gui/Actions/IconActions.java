package gui.Actions;

import javax.swing.AbstractAction;

import torrent.Torrent;

public abstract class IconActions extends AbstractAction {
	private Torrent torrent;

	public IconActions(Torrent t, String s) {
		super(s);
		this.torrent = t;
	}

	protected void stop() {
		if (torrent.getDownloadingStatus() != Torrent.STOPPED)
			torrent.stop();
	}

	protected void start() {
		if (torrent.getDownloadingStatus() == Torrent.STOPPED)
			torrent.massAnnounce();
		if (torrent.getDownloadingStatus() == Torrent.PAUSED)
			torrent.unPause();
	}

	protected void pause() {
		if (torrent.getDownloadingStatus() == Torrent.STARTED)
			torrent.pause();
	}
}
