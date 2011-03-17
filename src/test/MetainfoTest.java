/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.io.*;
import torrent.Metainfo;

public class MetainfoTest {
	public static void main(String[] args) {
		File file = new File("data/LePetitPrince.torrent");
		Metainfo metainfo = new Metainfo(file);
		System.out.println(metainfo);
	}
}
