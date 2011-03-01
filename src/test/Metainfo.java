/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.io.*;
import java.sql.Date;
import java.util.Hashtable;
import java.util.Map;

import bencoding.BDecoder;
import bencoding.BEValue;

public class Metainfo {
	public static void main(String[] args) {
		File file = new File("data/LePetitPrince.torrent");
		BDecoder bob = null;
		BEValue dico = null;
		Map maHashTable = null;
		try {
			bob = new BDecoder(new FileInputStream(file));
			dico = bob.bdecodeMap();
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

		try {
			maHashTable = dico.getMap();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		// String a = maHashTable.get("info").toString();
		String b = maHashTable.get("announce").toString();
		String c = maHashTable.get("created by").toString();
		String d = maHashTable.get("creation date").toString();
		String e = maHashTable.get("comment").toString();

		b = b.substring(8, b.length() - 1);
		c = c.substring(8, c.length() - 1);
		d = d.substring(8, d.length() - 1);
		e = e.substring(8, e.length() - 1);
		Date date = new Date(Long.parseLong(d)*1000);

		System.out.println(b + "\n" + c + "\n" + date + "\n" + e);
	}
}
