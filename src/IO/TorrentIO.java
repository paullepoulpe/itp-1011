package IO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import settings.GeneralSettings;
import torrent.Metainfo;
import torrent.Torrent;

/**
 * Cette classe est un supertype representant l'acces aux fichiers du torrent.
 * Implement la methode pour creer des fichiers vides s'ils n'existent pas
 * encore.
 * 
 * @author Damien, Maarten
 */
public class TorrentIO {
	protected Metainfo metainfo;
	protected File[] allFiles;
	protected int[] filesLength;
	protected File mainFile;
	protected File dossier;

	public TorrentIO(Torrent torrent) {
		this.dossier = new File(GeneralSettings.DOWNLOADING_FOLDER.getAbsolutePath());
		this.metainfo = torrent.getMetainfo();
	}

	/**
	 * Permet de creer les fichiers sur le disque s'ils n'existent pas encore.
	 * Ensuite les informations sur les fichiers contenus dans le
	 * {@link Metainfo} sont utilisees pour creer les {@link File}s si c'est un
	 * torrent multifile.
	 */
	protected void buildFiles() {
		if (!dossier.exists()) {
			dossier.mkdirs();
		}
		if (metainfo.isMultifile()) {
			filesLength = metainfo.getFilesLength();
			ArrayList<String[]> filesPaths = metainfo.getFilesPath();
			allFiles = new File[filesPaths.size()];
			ListIterator<String[]> iterator = filesPaths.listIterator();
			mainFile = new File(dossier, metainfo.getFileName());
			while (iterator.hasNext()) {
				String[] path = iterator.next();
				StringBuilder s = new StringBuilder(mainFile.getAbsolutePath());
				for (int i = 0; i < path.length - 1; i++) {
					s.append(File.separator + path[i]);
				}
				File dossierFinal = new File(s.toString());
				dossierFinal.mkdirs();
				File file = new File(dossierFinal, File.separator
						+ path[path.length - 1]);
				try {
					file.createNewFile();
					allFiles[iterator.nextIndex() - 1] = file;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			filesLength = new int[1];
			filesLength[0] = metainfo.getSize();
			File file = new File(dossier, metainfo.getFileName());
			mainFile = file;
			allFiles = new File[1];
			allFiles[0] = file;
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
