package IO;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TorrentFileReader {
	/**
	 * Cette methode permet d'initialiser les pieces d'un torrent depuis un
	 * fichier. Elle regarde dans le dossier Downloads/ si un fichier contient
	 * le nom du fichier contenu dans metainfo, il le lit et essaye
	 * d'initialiser les pieces avec. La piece s'occupe de verifier qu'elle soit
	 * correcte. Si le fichier n'est pas trouve cette methode retourne false
	 * 
	 * 
	 * @return true si le fichier a ete trouve, false sinon
	 */
	public boolean readFromFile() {
		// TODO gerer le multifile! // TODO Choisir le fichier de depart
		File folder = new File(System.getProperty("user.home"), "Downloads"
				+ File.separator);
		String[] liste = folder.list();
		boolean trouve = false;
		int indexFichier = -1;
		for (int i = 0; i < liste.length && !trouve; i++) {
			System.out.println(liste[i]);
			if (liste[i].contains(metainfo.getFileName())) {
				trouve = true;
				indexFichier = i;
			}
		}
		if (trouve) {
			File file = new File(System.getProperty("user.home"), "Downloads"
					+ File.separator + liste[indexFichier]);
			DataInputStream lecteur = null;
			try {
				lecteur = new DataInputStream(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] read;
			for (int i = 0; i < pieces.length; i++) {

				read = new byte[this.pieces[i].getSizeTab()];

				try {
					lecteur.read(read);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.pieces[i].setData(read);
			}
			try {
				lecteur.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;

		} else {
			System.out.println("File not found!");
			return false;
		}
	}
}
