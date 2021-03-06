package torrent.piece;

import gui.FunnyBar;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import IO.TorrentFileReader;
import IO.TorrentFileWriter;

import settings.GeneralSettings;
import torrent.Metainfo;
import torrent.Torrent;
import torrent.messages.MessageHandler;
import torrent.peer.Peer;

/**
 * Cette classe s'occupe de la gestion des {@link Piece}s. Elle selectionne
 * lesquelles doivent etre demandees aux {@link Peer}s.
 * 
 * @author Damien Engels, Maarten Sap
 */
public class PieceManager {
	private ArrayList<Piece> allPieces;
	private LinkedList<Piece> piecesOfInterest, leftPieces;
	private Torrent torrent;
	private TorrentFileWriter writer;
	private TorrentFileReader reader;
	private FunnyBar funnyBar;

	/**
	 * Contructeur qui initialise les champs ainsi que la {@link FunnyBar} a
	 * partir de la liste des {@link Piece}s
	 * 
	 * @param torrent
	 *            le {@link Torrent} conteneur des {@link Piece}
	 */
	public PieceManager(Torrent torrent) {
		this.torrent = torrent;
		this.leftPieces = new LinkedList<Piece>();
		this.piecesOfInterest = new LinkedList<Piece>();
		initPieces();
		funnyBar = new FunnyBar((int) Math.ceil((double) torrent.getMetainfo()
				.getSize() / (double) Piece.BLOCK_SIZE), new Dimension(600, 80));
		reader = new TorrentFileReader(torrent);
		reader.readFromFile(allPieces);
		this.writer = new TorrentFileWriter(torrent);
		// on met les pieces dans la liste de pieces a telecharger
		leftPieces.addAll(allPieces);
		ListIterator<Piece> iterator = leftPieces.listIterator();
		while (iterator.hasNext()) {
			Piece piece = iterator.next();
			if (piece.isChecked()) {
				iterator.remove();
				for (int i = 0; i < piece.getNbBlocs(); i++) {
					funnyBar.add((piece.getIndex() * torrent.getMetainfo()
							.getPieceLength()) / Piece.BLOCK_SIZE + i);
				}

			}
		}

		// On melange toutes les pieces
		Collections.shuffle(this.leftPieces);

		iterator = leftPieces.listIterator();
		while (iterator.hasNext()
				&& piecesOfInterest.size() < GeneralSettings.MAX_NUM_OF_CURRENT_PIECES) {
			Piece piece = iterator.next();
			iterator.remove();
			piece.allocateMemory();
			piecesOfInterest.addLast(piece);

		}

	}

	/**
	 * Cette methode doit tout d abord tester si le torrent est complet. Si
	 * c'est pas le cas, on enleve les pieces completes des pieces a telecharger
	 * 
	 * Cette methode est appelee dans la classe {@link MessageHandler} lors d'un
	 * {@link MessageHandler#visit(torrent.messages.SendBlock)}.
	 */
	public void updatePriorities() {
		if (!isComplete()) {
			synchronized (piecesOfInterest) {
				ListIterator<Piece> iterator = piecesOfInterest.listIterator();
				while (iterator.hasNext()) {
					Piece piece = iterator.next();
					if (piece.isChecked()) {
						iterator.remove();
						try {
							writer.writePiece(piece);
						} catch (FileNotFoundException e) {
							torrent.stop();
							System.err
									.println("Le Fichier de destination a disparu, veuillez le remettre dans le dossier de telechargement et redemarrer le telechargement");
						}
						torrent.notifyPeerHandlers(piece.getIndex());
						if (!leftPieces.isEmpty()) {
							Piece newPiece = leftPieces.removeFirst();
							newPiece.allocateMemory();
							iterator.add(newPiece);
						}
					}
				}
			}
			System.out.println((int) Math
					.round(getDownloadedCompleteness() * 100)
					/ 100.0
					+ " %....................");
		} else {
			writer.terminate();
		}

	}

	/**
	 * check si il y a une piece interessante dans les pieces que le peer a ,
	 * sinon retourne l'index -1
	 * 
	 * @param peerPieceIndex
	 * @return l'index de la {@link Piece} que l'on voudrait bien telecharger
	 *         (int)
	 */
	public int getPieceOfInterest(boolean[] peerPieceIndex) {
		int index = 0;
		int minDemandes = Integer.MAX_VALUE;
		synchronized (piecesOfInterest) {
			Iterator<Piece> iterator = piecesOfInterest.iterator();
			while (iterator.hasNext()) {
				Piece piece = iterator.next();
				if (piece.getNbDemandes() < minDemandes
						&& peerPieceIndex[index]) {
					index = piece.getIndex();
					minDemandes = piece.getNbDemandes();
				}
			}
		}
		if (minDemandes < Integer.MAX_VALUE) {
			return index;
		} else {
			return -1;
		}

	}

	/**
	 * retourne la completion du telechargement
	 * 
	 * @return le pourcentage d'avancement du telechargement (double)
	 */
	public double getDownloadedCompleteness() {
		double downloadedCompleteness = 0;
		ListIterator<Piece> iterator = allPieces.listIterator();
		while (iterator.hasNext()) {
			downloadedCompleteness += iterator.next().getDownloadCompleteness();
		}
		return downloadedCompleteness / allPieces.size();
	}

	/**
	 * verifie si on a au moins une piece!
	 * 
	 * @return true si on a aucune piece de complete
	 */
	public boolean isEmpty() {
		return piecesOfInterest.size() + leftPieces.size() == allPieces.size();
	}

	/**
	 * teste si le torrent est complet ou non
	 * 
	 * @return true si toutes les pieces sont completes et verfifiees
	 */
	public boolean isComplete() {
		return (leftPieces.size() + piecesOfInterest.size()) == 0;
	}

	/**
	 * initialise le tableau des pieces a la bonne taille, et initialise chaque
	 * piece avec son index et sa somme de controle
	 */
	private void initPieces() {
		Metainfo metainfo = torrent.getMetainfo();
		int nbPieces = metainfo.getNbPieces();
		allPieces = new ArrayList<Piece>(nbPieces);

		for (int i = 0; i < nbPieces; i++) {
			byte[] pieceHash = Arrays.copyOfRange(metainfo.getPiecesHash(),
					20 * i, 20 * (i + 1));
			if (i == nbPieces - 1) {
				int length = metainfo.getSize()
						- ((nbPieces - 1) * metainfo.getPieceLength());
				allPieces.add(i, new Piece(i, length, pieceHash));
			} else {
				allPieces.add(i, new Piece(i, metainfo.getPieceLength(),
						pieceHash));
			}
		}

	}

	public Piece[] getPieces() {
		return allPieces.toArray(new Piece[allPieces.size()]);
	}

	public int getNbPieces() {
		return torrent.getMetainfo().getNbPieces();
	}

	public Piece getPiece(int index) {
		return allPieces.get(index);
	}

	public Piece[] getCurrentPieces() {
		// System.out.println(piecesOfInterest.size());
		return piecesOfInterest.toArray(new Piece[piecesOfInterest.size()]);
	}

	public FunnyBar getFunnyBar() {
		return funnyBar;
	}

	public File getDownloadingFolder() {
		return writer.getDownloadingFolder();
	}

	/**
	 * Lit dans le fichier la piece a l'index avec l'objet
	 * {@link TorrentFileReader}
	 * 
	 * @param index
	 *            index de la {@link Piece} a lire sur le disque
	 * @return la piece lue, vide ou non vide
	 * @throws FileNotFoundException
	 *             si le fichier n'est pas trouve
	 */
	public Piece readPiece(int index) throws FileNotFoundException {
		reader.readPiece(allPieces.get(index));
		return allPieces.get(index);
	}
}
