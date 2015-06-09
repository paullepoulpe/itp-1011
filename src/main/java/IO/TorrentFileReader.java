package IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import torrent.Torrent;
import torrent.piece.Piece;

/**
 * Cette classe est un lecteur des fichiers du {@link Torrent}. Elle implement
 * diverses methodes pour lire tous les fichiers ou lire une {@link Piece} en
 * particulier
 * 
 * @author Damien, Maarten
 * 
 */
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
				Piece piece = pieces.get(i);
				readPiece(piece);
				piece.releaseMemory();
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
			FileInputStream fis = null;
			// j'initialise le stream du fichier courant
			if (!allFiles[fileIndex].exists()) {
				System.err.println("Ce fichier n'existe pas :"
						+ allFiles[fileIndex].getAbsolutePath());
				throw new FileNotFoundException();
			}
			fis = new FileInputStream(allFiles[fileIndex]);

			try {
				// je me positionne a l'endroit ou je vais commencer a
				// lire
				fis.skip(beginPosition);
				// je regarde combien on peut encore lire dans ce fichier
				int available = filesLength[fileIndex] - beginPosition;

				// si on peut tout lire dans le fichier
				if (available >= toRead) {
					// on lis tout ce qu'il reste
					fis.read(data, positionReading, toRead);
					toRead = -1;
				} else {
					// sinon on lis ce qu'on peut et on passe au debut de
					// la piece suivante
					fis.read(data, positionReading, available);
					toRead -= available;
					positionReading += available;
					fileIndex++;
					beginPosition = 0;
				}
				fis.close();
			} catch (IOException e) {
				System.err.println("Probleme de lecture de Piece");
				try {
					fis.close();
				} catch (IOException e1) {
					System.err
							.println("Probleme de fermeture du fichier pendant la lecture depuis le disque");
				}
			}
		}
		piece.setData(data);

	}

}
