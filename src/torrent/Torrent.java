package torrent;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import torrent.peer.*;
import torrent.piece.*;
import torrent.tracker.TrackerInfo;

public class Torrent {
	private ArrayList<Peer> peerList; // TODO peerhandlers?
	private Piece[] pieces;
	private ArrayList<TrackerInfo> trackers;
	private Metainfo metainfo;
	private int numPort;
	private PieceManager pieceManager;
	public static String PEER_ID = PeerIDGenerator.generateID();
	private boolean writtenOnFile;
	private boolean isComplete;
	private int[][] fileDelimiters;

	/**
	 * comstructeur avec numero de port
	 * 
	 * @param metainfoFile
	 *            le fichier .torrent
	 * 
	 * @param numPort
	 *            le port sur lequel on accepte les connections
	 */
	public Torrent(File metainfoFile, int numPort) {
		this.metainfo = new Metainfo(metainfoFile);
		this.numPort = numPort;
		this.peerList = new ArrayList<Peer>();
		this.initPieces();
		this.pieceManager = new PieceManager(this);
		this.computeDelimiters();

		System.out.println(this.metainfo);
	}

	/**
	 * constructeur sans numero de port, appele l'autre constructeur avec un
	 * numero de port aleatoire entre 6881 et 36881
	 * 
	 * @param metainfo
	 *            le fichier .torrent
	 */
	public Torrent(File metainfo) {
		this(metainfo, 6881 + (int) (Math.random() * 30001));

	}

	/**
	 * fais les premieres requetes aux trackers et demarre le @PeerAccepter
	 */
	public void massAnnounce() {
		ArrayList<String> trackersUrl = metainfo.getTrackerList();
		this.trackers = new ArrayList<TrackerInfo>();
		for (int i = 0; i < trackersUrl.size(); i++) {
			trackers.add(new TrackerInfo(trackersUrl.get(i), this));
			trackers.get(i).start();
		}
		new PeerAccepter(this.numPort, this);

	}

	/**
	 * retourne la completion du telechargement
	 * 
	 * @return le pourcentage d'avancement du telechargement (un double)
	 */
	public double getDownloadedCompleteness() {
		double downloadedCompleteness = 0;
		for (int i = 0; i < this.pieces.length; i++) {
			downloadedCompleteness += this.pieces[i].getDownloadCompleteness();
		}
		return downloadedCompleteness / this.pieces.length;
	}

	/**
	 * verifie si on a au moins une piece!
	 * 
	 * @return true si on a aucune piece de complete
	 */
	public boolean isEmpty() {
		boolean vide = true;
		for (int i = 0; i < this.pieces.length && vide; i++) {
			vide = vide && !this.pieces[i].isComplete();
		}
		return vide;
	}

	/**
	 * teste si le torrent est complet ou non
	 * 
	 * @return true si toutes les pieces sont completes et verfifiees
	 */
	public boolean isComplete() {
		if (isComplete) {
			return true;
		} else {
			boolean complet = true;
			for (int i = 0; i < this.pieces.length && complet; i++) {
				complet = complet && this.pieces[i].isChecked();
			}
			this.isComplete = complet;
			return complet;
		}

	}

	/**
	 * Permet d'ajouter un peer au torrent
	 * 
	 * @param peer
	 *            le peer qu'on veut ajouter
	 * @return false si on l'as deja
	 */
	public boolean addPeer(Peer peer) {
		if (peerList.contains(peer)) {
			return false;
		} else {
			peerList.add(peer);
			System.out.println("Nouveau pair : " + peer);
			return true;
		}
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
	public boolean readFromFile() {
		// TODO gerer le multifile!
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
			writtenOnFile = true;
			return true;

		} else {
			System.out.println("File not found!");
			return false;
		}
	}

	/**
	 * calcule les limites des fichiers, debut(quelle piece et ou) et fin
	 * (quelle piece et ou) le debut et la fin sont compris dans le fichier!
	 */
	private void computeDelimiters() {
		int[] filesLength = metainfo.getFilesLength();
		if (metainfo.isMultifile()) {
			this.fileDelimiters = new int[this.metainfo.getFilesLength().length][4];
			// forme : (debut (inclus) [Piece][Indice];fin
			// (inclus)[Piece][Indice])

			fileDelimiters[0][0] = 0;
			fileDelimiters[0][1] = 0;
			for (int i = 0; i < filesLength.length - 1; i++) {

				fileDelimiters[i][3] = (fileDelimiters[i][0] + filesLength[i] - 1)
						% pieces[0].getSizeTab();
				fileDelimiters[i][2] = fileDelimiters[i][0]
						+ (fileDelimiters[i][0] + filesLength[i] - fileDelimiters[i][3])
						/ pieces[0].getSizeTab();

				fileDelimiters[i + 1][1] = (fileDelimiters[i][3] + 1)
						% pieces[0].getSizeTab();
				fileDelimiters[i + 1][0] = fileDelimiters[i][2];
				if (fileDelimiters[i + 1][1] == 0) {
					fileDelimiters[i + 1][0]++;
				}

			}
			fileDelimiters[filesLength.length - 1][2] = pieces.length - 1;
			fileDelimiters[filesLength.length - 1][3] = pieces[pieces.length - 1]
					.getSizeTab() - 1;
		} else {

			this.fileDelimiters = new int[1][4];

			fileDelimiters[0][0] = 0;
			fileDelimiters[0][1] = 0;
			fileDelimiters[0][0] = pieces.length - 1;
			fileDelimiters[0][1] = pieces[pieces.length - 1].getSizeTab() - 1;

		}

	}

	/**
	 * ecris les fichiers sur le disque grace au delimiteurss
	 * 
	 * @return true si tout a été ecris correctement
	 */
	public boolean writeToFile() {
		// TODO choisir le fichier de destination
		if (isComplete && !writtenOnFile) {
			FileOutputStream fileStream = null;
			ArrayList<String[]> filesPath = metainfo.getFilesPath();
			for (int i = 0; i < filesPath.size(); i++) {
				System.out.println("Fichier " + (i + 1) + " : ");
				System.out.println("Debut : ( " + fileDelimiters[i][0] + "; "
						+ fileDelimiters[i][1] + " )" + "  Fin : ( "
						+ fileDelimiters[i][2] + "; " + fileDelimiters[i][3]
						+ " )");

				String path = System.getProperty("user.home") + File.separator
						+ "Downloads" + File.separator + metainfo.getFileName();

				for (int j = 0; j < filesPath.get(i).length - 1; j++) {
					path = path + File.separator + filesPath.get(i)[j];
				}
				new File(path).mkdirs();
				File file = new File(path + File.separator
						+ filesPath.get(i)[filesPath.get(i).length - 1]);

				try {
					file.createNewFile();
					fileStream = new FileOutputStream(file);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

				for (int j = fileDelimiters[i][0]; j <= fileDelimiters[i][2]; j++) {

					System.out.println("Piece " + (j) + " ok!");

					byte[] currentData = pieces[j].getData();

					if (j == fileDelimiters[i][0]) {
						currentData = Arrays.copyOfRange(currentData,
								fileDelimiters[i][1], currentData.length);
					}
					if (j == fileDelimiters[i][2]) {
						currentData = Arrays.copyOfRange(currentData, 0,
								fileDelimiters[i][1] + 1);
					}

					try {
						fileStream.write(currentData);

					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}

				try {
					fileStream.flush();
					fileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

			}
			writtenOnFile = true;
			System.out.println("Fichiers ecrit dans "
					+ System.getProperty("user.home") + File.separator
					+ "Downloads");
			System.exit(0);
		}
		return false;
	}

	/**
	 * initialise le tableau des pieces a la bonne taille, et initialise chaque
	 * piece avec son index et sa somme de controle
	 */
	private void initPieces() {
		this.pieces = new Piece[(int) (Math.ceil(((double) this.metainfo
				.getSize())
				/ ((double) this.metainfo.getPieceLength())))];

		for (int i = 0; i < this.pieces.length; i++) {
			byte[] pieceHash = Arrays.copyOfRange(
					this.metainfo.getPiecesHash(), 20 * i, 20 * (i + 1));
			if (i == pieces.length - 1) {
				int length = this.metainfo.getSize()
						- ((pieces.length - 1) * this.metainfo.getPieceLength());
				pieces[i] = new Piece(i, length, pieceHash);
			} else {
				pieces[i] = new Piece(i, this.metainfo.getPieceLength(),
						pieceHash);
			}
		}

	}

	public int getNumPort() {
		return numPort;
	}

	public Piece[] getPieces() {
		return pieces;
	}

	public Metainfo getMetainfo() {
		return metainfo;
	}

	public ArrayList<TrackerInfo> getTrackers() {
		return trackers;
	}

	public ArrayList<Peer> getPeerList() {
		return peerList;
	}

	public PieceManager getPieceManager() {
		return pieceManager;
	}
}
