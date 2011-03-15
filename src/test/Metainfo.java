/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.net.URL;

import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

public class Metainfo {
	public static void main(String[] args) {
		File file = new File("data/katyperry.torrent");
		BDecoder bob = null;
		BEValue dico = null, infoBEValue = null;
		Map maHashTable = null, infoMap = null, a2 = null;
		Object[] mInfo = new Object[9];
		ArrayList<BEValue> announceList = null;
		String aList = "";
		try {
			bob = new BDecoder(new FileInputStream(file));
			dico = bob.bdecodeMap();
			//System.out.println(dico);
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		try {
			maHashTable = dico.getMap();
			infoBEValue = (BEValue) maHashTable.get("info");
			infoMap = infoBEValue.getMap();
			mInfo[0] = ((BEValue) maHashTable.get("announce")).getString();
			mInfo[1] = ((BEValue) maHashTable.get("created by")).getString();
			mInfo[2] = ((BEValue) maHashTable.get("creation date")).getLong();
			mInfo[3] = ((BEValue) maHashTable.get("comment")).getString();
			mInfo[4] = ((BEValue) infoMap.get("name")).getString();
			mInfo[5] = ((BEValue) infoMap.get("pieces")).getBytes().length;
			mInfo[6] = ((BEValue) infoMap.get("piece length")).getLong();
			mInfo[7] = ((BEValue) infoMap.get("length")).getInt();
			if (maHashTable.get("announce-list") != (null)) {
				mInfo[8] = ((BEValue) maHashTable.get("announce-list"))
						.getList();
				announceList = (ArrayList<BEValue>) mInfo[8];
				for (int i = 0; i < announceList.size(); i++) {
					for (int j = 0; j < announceList.get(i).getList().size(); j++) {
						aList = aList
								+ "\t\t"
								+ announceList.get(i).getList().get(j)
										.getString() + "\n";
					}
				}
				mInfo[8] = aList;
			} else {
				mInfo[8] = "";
			}

		} catch (InvalidBEncodingException exc) {
			System.out.println("Probleme:" + exc.getMessage());
		} catch (Exception e) {
			System.out.println("Probleme: " + e.getLocalizedMessage());
		}
		String[] metaInfo = new String[9];
		Date date = new Date(((Long) mInfo[2]) * 1000);
		mInfo[2] = date.toString();
		mInfo[0] = "Tracker : \t" + mInfo[0];
		mInfo[1] = "Author : \t" + mInfo[1];
		mInfo[2] = "Creation Date : " + mInfo[2];
		mInfo[3] = "\nComment : \t" + mInfo[3];
		mInfo[4] = "\nFilename: \t" + mInfo[4];
		mInfo[5] = "SHA length: \t" + mInfo[5] + " Bytes";
		mInfo[6] = "PieceLength: \t" + mInfo[6] + " Bytes";
		mInfo[7] = "Size: \t\t" + mInfo[7] + " Bytes";
		if (!mInfo[8].equals("")) {
			mInfo[8] = "Tracker list: \n" + mInfo[8];
		}

		for (int i = 0; i < mInfo.length; i++) {
			System.out.println(mInfo[i]);
		}
	}
}
