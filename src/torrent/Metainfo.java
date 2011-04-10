package torrent;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;
import torrent.tracker.*;

/**
 * Cette classe permet de décoder le fichier "*.torrent" et de faciliter l'accès
 * aux donnés encodées dedans. Il sert de conteneur.
 * 
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class Metainfo {
	private TorrentHash infoHash; // hash du fichier Métainfo
	private String createdBy;
	private String comment;
	private Date creationDate;
	private String fileName;
	private byte[] piecesHash;
	private int pieceLength;
	private int size;
	private ArrayList<String> trackerList;
	private boolean isMultifile;
	private ArrayList<String[]> FilesPath;
	private int[] filesLength;

	/**
	 * Ce constructeur initialise tous les paramètres en les extrayants du
	 * fichier file;
	 * 
	 * @param file
	 *            le fichier "*.torrent" qui contient les informations
	 */
	public Metainfo(File file) {
		BDecoder myDecoder = null;
		BEValue dico = null, infoBEValue = null;
		Map<String, BEValue> dicoMap = null, infoMap = null;

		try {
			myDecoder = new BDecoder(new FileInputStream(file));
			dico = myDecoder.bdecodeMap();
			this.infoHash = new TorrentHash(myDecoder.getSpecialMapDigest());

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		try {
			dicoMap = dico.getMap();
			infoBEValue = dicoMap.get("info");
			infoMap = infoBEValue.getMap();
			if (dicoMap.get("created by") != null) {
				this.createdBy = dicoMap.get("created by").getString();
			}

			if (dicoMap.get("creation date") != null) {
				this.creationDate = new Date(dicoMap.get("creation date")
						.getLong() * 1000);
			}
			if (dicoMap.get("comment") != null) {
				this.comment = dicoMap.get("comment").getString();
			}

			this.piecesHash = infoMap.get("pieces").getBytes();
			this.pieceLength = infoMap.get("piece length").getInt();
			this.fileName = infoMap.get("name").getString();

			if (infoMap.get("length") != null) {
				this.size = infoMap.get("length").getInt();
			} else {
				isMultifile = true;
				ArrayList<BEValue> files = (ArrayList<BEValue>) infoMap.get(
						"files").getList();
				filesLength = new int[files.size()];
				FilesPath = new ArrayList<String[]>();
				for (int i = 0; i < files.size(); i++) {
					filesLength[i] = files.get(i).getMap().get("length")
							.getInt();
					size += filesLength[i];
					ArrayList<BEValue> pathList = (ArrayList<BEValue>) files
							.get(i).getMap().get("path").getList();
					String[] path = new String[pathList.size()];
					for (int j = 0; j < path.length; j++) {
						path[j] = pathList.get(j).getString();
					}
					FilesPath.add(path);
				}
			}

			this.trackerList = new ArrayList<String>();

			if (dicoMap.get("announce-list") != (null)) {
				ArrayList<BEValue> announces = (ArrayList<BEValue>) dicoMap
						.get("announce-list").getList();
				for (int i = 0; i < announces.size(); i++) {
					for (int j = 0; j < announces.get(i).getList().size(); j++) {
						if (!announces.get(i).getList().get(j).getString()
								.substring(0, 3).equals("udp")) {
							this.trackerList.add(announces.get(i).getList()
									.get(j).getString());
						}
					}
				}
			} else {
				this.trackerList.add(dicoMap.get("announce").getString());
			}
		} catch (InvalidBEncodingException e) {
			System.out.println("Probleme1 : " + e.getMessage());
		}
	}

	/**
	 * Mets touts le nom de tous les trackers bout a bout dans une seule chaine
	 * de caractère
	 * 
	 * @return Une String qui comporte le nom de chaque tracker bout a bout,
	 *         séparés par des retours a la ligne
	 */
	private String printTrackerList() {
		String trackers = "";
		ListIterator<String> myIterator = trackerList.listIterator();
		while (myIterator.hasNext()) {
			trackers = trackers.concat("\t" + myIterator.next() + "\n");
		}
		return trackers;

	}

	/**
	 * Retourne une String (human readable) contenant toutes les informations
	 * relatives a cette classe
	 */
	public String toString() {
		String s = "Informations sur le torrent : ";
		if (!isMultifile) {
			s += "\n\nNom du fichier :\t" + fileName;
		}
		s += "\nAuteur :\t\t" + createdBy + "\nDate de creation :\t"
				+ creationDate + "\n\nCommentaire:\t" + comment
				+ "\n\nTaille d'une piece :\t" + pieceLength + " Bytes";
		if (!isMultifile) {
			s += "\nTaille du fichier :\t" + size + " Bytes";
		} else {
			s += "\nTaille totale des fichiers :\t" + size + " Bytes";
		}
		s += "\n\nList des trackers : \n\n" + printTrackerList();
		if (isMultifile) {
			s += "\n\nListe des fichiers :\n\n";
			for (int i = 0; i < FilesPath.size(); i++) {
				s += "\t" + fileName;
				for (int j = 0; j < FilesPath.get(i).length; j++) {
					s = s + File.separator + FilesPath.get(i)[j];
				}
				s = s + "\n";
			}

		} else {

		}
		return s;
	}

	public boolean isMultifile() {
		return isMultifile;
	}

	/**
	 * @return la liste des trackers
	 */
	public ArrayList<String> getTrackerList() {
		return trackerList;
	}

	/**
	 * Get InfoHash
	 * 
	 * @return le InfoHash
	 */
	public TorrentHash getInfoHash() {
		return infoHash;
	}

	public int getSize() {
		return size;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getPiecesHash() {
		return piecesHash;
	}

	public int getPieceLength() {
		return pieceLength;
	}

	public String getComment() {
		return comment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public int[] getFilesLength() {
		return filesLength;
	}

	public ArrayList<String[]> getFilesPath() {
		return FilesPath;
	}

}