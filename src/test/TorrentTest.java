/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.io.File;

import torrent.Torrent;

public class TorrentTest {
	public static void main(String[] args) {
		Torrent myTorrent = new Torrent(new File("data/unixsoft2.torrent"));
		myTorrent.massAnnounce();

	}
}
