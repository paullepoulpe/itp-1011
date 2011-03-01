/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.io.*;
import java.sql.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

public class Metainfo {
	public static void main(String[] args) {
		File file = new File("data/Plus44.torrent");
		BDecoder bob = null;
		BEValue dico = null, a = null;
		Map maHashTable = null, a0 = null, a2 =null;
		try {
			bob = new BDecoder(new FileInputStream(file));
			dico = bob.bdecodeMap();
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		try {
			maHashTable = dico.getMap();
			System.out.println(maHashTable);
			a = (BEValue) maHashTable.get("info");
			a2= ((BEValue) maHashTable.get("announce-list")).getMap();
			a0 = a.getMap();
		} catch (InvalidBEncodingException exc) {
			System.out.println("Probleme:" + exc.getMessage());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		String[] metaInfo = new String[8];
		metaInfo[0] = maHashTable.get("announce").toString();
		metaInfo[1] = maHashTable.get("created by").toString();
		metaInfo[2] = maHashTable.get("creation date").toString();
		metaInfo[3] = maHashTable.get("comment").toString();
		metaInfo[4] = a0.get("name").toString();
		metaInfo[5] = a0.get("pieces").toString();
		metaInfo[6] = a0.get("piece length").toString();
		metaInfo[7] = a0.get("length").toString();
		metaInfo[8] = a2.get("announce-list").toString();
		for (int i = 0; i < metaInfo.length; i++) {
			metaInfo[i] = metaInfo[i].substring(8, metaInfo[i].length() - 1);
		}
		Date date = new Date(Long.parseLong(metaInfo[2]) * 1000);
		metaInfo[2] = date.toString();
		metaInfo[0] = "Tracker : " + metaInfo[0];
		metaInfo[1] = "Author : " + metaInfo[1];
		metaInfo[2] = "Creation Date : " + metaInfo[2];
		metaInfo[3] = "\nComment : " + metaInfo[3];
		metaInfo[4] = "\nFilename: " + metaInfo[4];
		metaInfo[5] = "Pieces: " + metaInfo[5];
		metaInfo[6] = "PieceLength: " + metaInfo[6] + " Bytes";
		metaInfo[7] = "Size: " + metaInfo[7] + " Bytes";

		for (int i = 0; i < metaInfo.length; i++) {
			System.out.println(metaInfo[i]);
		}
	}
}
