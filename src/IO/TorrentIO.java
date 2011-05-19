package IO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import settings.GeneralSettings;
import torrent.Metainfo;
import torrent.Torrent;

public class TorrentIO {
	protected Metainfo metainfo;
	protected File[] allFiles;
	protected int[] filesLength;
	protected File dossier = GeneralSettings.DOWNLOADING_FOLDER;

	public TorrentIO(Torrent torrent) {
		this.metainfo = torrent.getMetainfo();
	}

	protected void buildFiles() {

		if (!dossier.exists()) {
			dossier.mkdirs();
		}
		if (metainfo.isMultifile()) {
			filesLength = metainfo.getFilesLength();
			ArrayList<String[]> filesPaths = metainfo.getFilesPath();
			allFiles = new File[filesPaths.size()];
			ListIterator<String[]> iterator = filesPaths.listIterator();
			while (iterator.hasNext()) {
				String[] path = iterator.next();
				StringBuilder s = new StringBuilder(metainfo.getFileName());
				for (int i = 0; i < path.length - 1; i++) {
					s.append(File.separator + path[i]);
				}
				File dossierFinal = new File(dossier, s.toString());
				dossierFinal.mkdirs();
				File file = new File(dossierFinal, path[path.length - 1]
						+ ".temp");
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
			File file = new File(dossier, metainfo.getFileName() + ".temp");
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
