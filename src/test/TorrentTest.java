/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.io.File;

import torrent.Torrent;

public class TorrentTest extends Thread {
	public static void main(String[] args) {
		new TorrentTest().start();
	}

	public void run() {

		Torrent myTorrent = new Torrent(new File("data/G6.torrent"));

		while (true) {
			myTorrent.massAnnounce();
			try {
				sleep(240000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
