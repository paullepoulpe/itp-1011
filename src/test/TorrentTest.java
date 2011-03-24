/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.io.File;

import torrent.Torrent;

public class TorrentTest {
	public static void main(String[] args) {

		Torrent myTorrent = new Torrent(new File("data/LePetitPrince.torrent"));
		myTorrent.massAnnounce();
		System.out.println("read from file: " + myTorrent.readFromFile());
		System.out.println("is complete: " + myTorrent.isComplete());
		System.out.println("written: " + myTorrent.writeToFile());

	}
}
