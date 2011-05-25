package gui.Actions;

import gui.MainFrame;

import java.io.File;

import javax.swing.AbstractAction;

import settings.GeneralSettings;
import torrent.Torrent;

public abstract class IconActions extends AbstractAction {
	protected Torrent torrent;

	public IconActions(String s) {
		super(s);
		setEnabled(false);
	}

	public IconActions(Torrent t, String s) {
		super(s);
		this.torrent = t;
	}

	protected void stop() {
		if (torrent.getDownloadingStatus() != Torrent.STOPPED)
			torrent.stop();
	}

	protected void start() {
		if (torrent.getDownloadingStatus() == Torrent.STOPPED) {
			System.out.println("Start:  massannounce");
			torrent.massAnnounce();
		}

	}

	protected File getDownloadFile() {
		if (torrent.getMetainfo().isMultifile()) {
			return new File(torrent.getDownloadinFolder().getAbsolutePath(),
					torrent.getMetainfo().getFileName());
		}
		return torrent.getDownloadinFolder();
	}

	protected boolean isDownloading() {
		return (torrent.getDownloadingStatus() == Torrent.STARTED);
	}

	public void delete(MainFrame mf) {
		mf.deleteTorrent(torrent);

	}
}
