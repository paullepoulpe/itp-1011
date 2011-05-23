package IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import torrent.Torrent;
import torrent.piece.Piece;

public class TorrentFileWriter extends TorrentIO implements Runnable {
	private boolean[] piecesWritten;
	private boolean writtenOnFile;

	public TorrentFileWriter(Torrent torrent) {
		super(torrent);
		piecesWritten = new boolean[metainfo.getNbPieces()];
		new Thread(this).start();
	}

	@Override
	public void run() {
		buildFiles();
	}

	public boolean writePiece(Piece piece) throws FileNotFoundException {
		if (!piecesWritten[piece.getIndex()] && piece.isChecked()) {
			//data a ecrire
			byte[] data = piece.getData();
			//ce qu'il reste a ecrire
			int toWrite = data.length;
			// jusqu'a ou on a ecrit dans le tableau data
			int positionWritten = 0;
			//position de depart de data dans les fichiers
			int beginPosition = piece.getIndex() * metainfo.getPieceLength();
			//quand on a trouve le bon fichier
			boolean stop = false;
			//l'index du fichier dans lequel il faut ecrire
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
			// tant qu'il me reste quelquechose a ecrire
			while (toWrite > 0) {
				RandomAccessFile raf = null;
				// j'initialise le stream du fichier courant
				if (!allFiles[fileIndex].exists()) {
					throw new FileNotFoundException();
				}
				raf = new RandomAccessFile(allFiles[fileIndex], "rw");
				// System.out.println("New RAF sur "
				// + allFiles[fileIndex].getAbsolutePath());

				try {
					// je me positionne a l'endroit ou je vais commencer a
					// ecrire
					raf.seek(beginPosition);
					// je regarde combien on peut encore ecrire dans ce fichier
					int writable = filesLength[fileIndex] - beginPosition;

					// si on peut tout ecrire dans le fichier
					if (writable >= toWrite) {
						// on ecris tout ce qu'il reste
						raf.write(data, positionWritten, toWrite);
						toWrite = -1;
					} else {
						// sinon on ecris ce qu'on peut et on passe au debut de
						// la piece suivante
						raf.write(data, positionWritten, writable);
						toWrite -= writable;
						positionWritten += writable;
						fileIndex++;
						beginPosition = 0;
					}
					raf.close();
				} catch (IOException e) {
					System.err.println("Probleme d'ecriture de Piece");
					try {
						raf.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
			piece.setWritten(true);
			piece.releaseMemory();
			piecesWritten[piece.getIndex()] = true;
		}
		return false;

	}

	public void terminate() {
		if (!writtenOnFile) {
			writtenOnFile = true;
			for (boolean b : piecesWritten) {
				if (!b) {
					writtenOnFile = false;
					break;
				}
			}
			if (writtenOnFile) {
				System.out.println("Fichier ecris dans : "
						+ dossier.getAbsolutePath());
			}
		}
	}

	public File getDownloadingFolder() {
		return dossier;
	}
}