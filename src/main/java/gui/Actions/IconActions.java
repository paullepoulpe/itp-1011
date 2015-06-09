package gui.Actions;

import gui.MainFrame;

import java.io.File;

import javax.swing.AbstractAction;

import torrent.Torrent;

/**
 * Superclasse qui contient la plupart des methodes des classes derivees, afin
 * que celles-ci soient un peu plus allegees.
 * 
 * @author Damien, Maarten
 * 
 */
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

	/**
	 * Stoppe le torrent auquel appartient l'action.
	 */
	protected void stop() {
		if (torrent.getDownloadingStatus() != Torrent.STOPPED)
			torrent.stop();
	}

	/**
	 * Demarre le torrent auquel appartient l'action.
	 */
	protected void start() {
		if (torrent.getDownloadingStatus() == Torrent.STOPPED) {
			torrent.massAnnounce();
		}

	}

	/**
	 * Permet d'acceder au dossier de telechargement du torrent.
	 * 
	 * @return le {@link File} ou les fichiers sont telecharges.
	 */
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

	/**
	 * Supprime le torrent
	 * 
	 * @param mf
	 *            fenetre principale, pour avoir acces a la liste des torrents
	 */
	public void delete(MainFrame mf) {
		mf.deleteTorrent(torrent);
	}
}
