package torrent;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

/**
 * Cette classe permet de décoder le fichier "*.torrent" et de faciliter l'accès
 * aux donnés encodées dedans. Il sert de conteneur.
 * 
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class Metainfo {
	private byte[] infoHash; // hash du fichier Métainfo
	private String createdBy;
	private String comment;
	private Date creationDate;
	private String fileName;
	private byte[] piecesHash;
	private int pieceLength;
	private int size;
	private ArrayList<String> trackerList;

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
			this.infoHash = myDecoder.getSpecialMapDigest();

		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		try {
			dicoMap = dico.getMap();
			infoBEValue = dicoMap.get("info");
			infoMap = infoBEValue.getMap();

			this.createdBy = dicoMap.get("created by").getString();
			this.creationDate = new Date(
					dicoMap.get("creation date").getLong() * 1000);
			this.comment = dicoMap.get("comment").getString();
			this.fileName = infoMap.get("name").getString();
			this.piecesHash = infoMap.get("pieces").getBytes();
			this.pieceLength = infoMap.get("piece length").getInt();
			this.size = infoMap.get("length").getInt();

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
		return "Informations sur le torrent : " + "\n\nNom du fichier :\t"
				+ fileName + "\nAuteur :\t\t" + createdBy
				+ "\nDate de creation :\t" + creationDate
				+ "\n\nCommentaire:\t" + comment + "\n\nTaille d'une pièce :\t"
				+ pieceLength + " Bytes" + "\nTaille du fichier :\t" + size
				+ " Bytes" + "\n\nList des trackers : \n\n"
				+ printTrackerList();
	}

	/**
	 * 
	 * @returnla liste des trackers
	 */
	public ArrayList<String> getTrackerList() {
		return trackerList;
	}

	public byte[] getInfoHash() {
		return infoHash;
	}

	public int getSize() {
		return size;
	}
}
