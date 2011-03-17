/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package torrent;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.net.MalformedURLException;
import java.net.URL;

import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

/**
 * Cette classe permet de décoder le fichier "*.torrent" et de faciliter l'accès
 * aux donnés encodées dedans. Il sert de conteneur.
 * 
 * 
 * @author Damien et Maarten
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
			System.out.println("Probleme : " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Probleme : " + e.getMessage());
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
		for (int i = 0; i < trackerList.size(); i++) {
			trackers = trackers.concat("\t" + trackerList.get(i) + "\n");
		}
		return trackers;

	}

	/**
	 * Retourne une String (human readable) contenant toutes les informations
	 * relatives a cette classe
	 */
	public String toString() {
		return "Informations sur le torrent : " + "\n\nNom du fichier :\t"
				+ fileName + "\nAuteur :\t" + createdBy
				+ "\nDate de création :\t" + creationDate
				+ "\n\nCommentaire:\t" + comment
				+ "\n\n Taille d'une pièce :\t" + pieceLength
				+ "\n Taille du fichier :\t" + size
				+ "\n\nList des trackers : \n" + printTrackerList();
	}
}
