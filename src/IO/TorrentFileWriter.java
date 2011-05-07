package IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import torrent.Metainfo;
import torrent.Torrent;
import torrent.piece.Piece;

public class TorrentFileWriter {
	private Torrent torrent;
	private int[][] fileDelimiters;
	private String[][] filesPath;
	private Piece[] pieces;
	private boolean[] piecesWritten;
	private boolean writtenOnFile;
	private JFileChooser choisir;
	private String dossier;

	public TorrentFileWriter(Torrent torrent, Metainfo metainfo) {
		this.torrent = torrent;
		pieces = torrent.getPieces();
		computeDelimiters(metainfo);
		computePaths(metainfo);
		piecesWritten = new boolean[pieces.length];
		dossier = choisirDossierDestination();

	}

	private boolean writePiece(int index) {
		if (piecesWritten[index] || !pieces[index].isChecked()) {

		}
		return false;

	}

	public boolean writeAll() {
		if (torrent.isComplete() && !writtenOnFile) {
			try {
				FileOutputStream fileStream = null;
				for (int i = 0; i < filesPath.length; i++) {
					System.out.println("Fichier " + (i + 1) + " : ");
					System.out.println("Debut : ( " + fileDelimiters[i][0]
							+ "; " + fileDelimiters[i][1] + " )" + "  Fin : ( "
							+ fileDelimiters[i][2] + "; "
							+ fileDelimiters[i][3] + " )");

					new File(dossier, filesPath[i][0]).mkdirs();
					File file = new File(dossier, filesPath[i][0]
							+ filesPath[i][1]);
					file.createNewFile();
					fileStream = new FileOutputStream(file);

					for (int j = fileDelimiters[i][0]; j <= fileDelimiters[i][2]; j++) {

						// System.out.println("Piece " + (j) + " ok!");

						byte[] currentData = pieces[j].getData();

						if (j == fileDelimiters[i][0]) {
							currentData = Arrays.copyOfRange(currentData,
									fileDelimiters[i][1], currentData.length);
						}
						if (j == fileDelimiters[i][2]) {
							currentData = Arrays.copyOfRange(currentData, 0,
									fileDelimiters[i][1] + 1);
						}

						fileStream.write(currentData);
					}
					fileStream.flush();
					fileStream.close();

				}

				writtenOnFile = true;
				System.out.println("Fichiers ecrit dans " + dossier);
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private boolean buildFiles() {
		return false;
	}

	/**
	 * calcule les limites des fichiers, debut(quelle piece et ou) et fin
	 * (quelle piece et ou) le debut et la fin sont compris dans le fichier!
	 */
	private void computeDelimiters(Metainfo metainfo) {
		int[] filesLength = metainfo.getFilesLength();

		if (metainfo.isMultifile()) {
			this.fileDelimiters = new int[metainfo.getFilesLength().length][4];
			/*
			 * forme : (debut (inclus) [Piece][Indice]; fin
			 * (inclus)[Piece][Indice])
			 */

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
			fileDelimiters[0][2] = pieces.length - 1;
			fileDelimiters[0][3] = pieces[pieces.length - 1].getSizeTab() - 1;

		}

	}

	private String choisirDossierDestination() {
		choisir = new JFileChooser();

		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
			choisir.updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}

		choisir.setDialogTitle("Choisir le dossier de Destination");
		choisir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choisir.showDialog(null, "Choisir");
		String dossier = null;
		try {
			dossier = choisir.getSelectedFile().getAbsolutePath();
		} catch (NullPointerException e) {
			System.err.println("Vous avez annulé le choix de dossier");
			System.exit(0);
		}
		return dossier;
	}

	public void updateUI() {
		this.choisir.updateUI();
	}

	private void computePaths(Metainfo metainfo) {
		if (metainfo.isMultifile()) {

			ArrayList<String[]> filesPathArray = metainfo.getFilesPath();
			filesPath = new String[filesPathArray.size()][2];
			// (chemin, nom du fichier)
			// on construit le chemin et le nom de chaque fichier
			for (int i = 0; i < filesPath.length; i++) {
				StringBuilder folder = new StringBuilder();
				for (int j = 0; j < filesPathArray.get(i).length - 1; j++) {
					folder.append(filesPathArray.get(i)[j] + File.separator);
				}
				filesPath[i][0] = folder.toString();
				filesPath[i][1] = filesPathArray.get(i)[filesPathArray.get(i).length - 1];
			}
		} else {
			filesPath = new String[1][2];
			filesPath[0][0] = "/";
			filesPath[0][1] = metainfo.getFileName();
		}
	}
}
