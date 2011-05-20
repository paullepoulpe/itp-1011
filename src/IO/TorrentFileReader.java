package IO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import torrent.Torrent;
import torrent.piece.Piece;

public class TorrentFileReader extends TorrentIO {
	Torrent torrent;

	public TorrentFileReader(Torrent torrent) {
		super(torrent);
		this.torrent = torrent;
		buildFiles();
	}

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
	public void readFromFile(ArrayList<Piece> pieces) {
		for (int i = 0; i < metainfo.getNbPieces(); i++) {
			try {
				readPiece(pieces.get(i));
			} catch (FileNotFoundException e) {
				System.err.println("Piece " + i + " pas encore telechargée");
			}
		}
	}

	public void readPiece(Piece piece) throws FileNotFoundException {
		int toRead = piece.getSizeTab();
		byte[] data = new byte[toRead];
		int positionReading = 0;
		int beginPosition = piece.getIndex() * metainfo.getPieceLength();
		boolean stop = false;
		int fileIndex = 0;
		// je trouve dans quel fichier ma piece commence
		while (fileIndex < filesLength.length && !stop) {
			if (beginPosition < filesLength[fileIndex]) {
				stop = true;
			} else {
				beginPosition -= filesLength[fileIndex];
				fileIndex++;
			}
		}
		// tant qu'il me reste quelquechose a lire
		while (toRead > 0) {
			RandomAccessFile raf = null;
			// j'initialise le stream du fichier courant
			if (!allFiles[fileIndex].exists()) {
				System.err.println("Ce fichier n'existe pas :"
						+ allFiles[fileIndex].getAbsolutePath());
				throw new FileNotFoundException();
			}
			raf = new RandomAccessFile(allFiles[fileIndex], "rw");
			// System.out.println("New RAF sur "
			// + allFiles[fileIndex].getAbsolutePath());

			try {
				// je me positionne a l'endroit ou je vais commencer a
				// lire
				raf.seek(beginPosition);
				// je regarde combien on peut encore lire dans ce fichier
				int available = filesLength[fileIndex] - beginPosition;

				// si on peut tou lire dans le fichier
				if (available >= toRead) {
					// on lis tout ce qu'il reste
					raf.read(data, positionReading, toRead);
					toRead = -1;
				} else {
					// sinon on lis ce qu'on peut et on passe au debut de
					// la piece suivante
					raf.write(data, positionReading, available);
					toRead -= available;
					positionReading = +available;
					fileIndex++;
					beginPosition = 0;
				}
				raf.close();
			} catch (IOException e) {
				System.err.println("Probleme de lecture de Piece");
				try {
					raf.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

}
