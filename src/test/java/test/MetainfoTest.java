/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.io.*;

import torrent.InvalidFileException;
import torrent.Metainfo;

public class MetainfoTest {
	public static void main(String[] args) {
		File file = new File("data/mariacarree.torrent");
		Metainfo metainfo=null;
		try {
			metainfo = new Metainfo(file);
		} catch (InvalidFileException e) {
			e.printStackTrace();
		}
		
		System.out.println(file);
		System.out.println(metainfo.isMultifile());
	}
}
