/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.io.File;

import settings.GeneralSettings;
import torrent.InvalidFileException;
import torrent.Torrent;

public class TorrentTest extends Thread {
	public static void main(String[] args) {
		// GeneralSettings.readFromFile();
		new TorrentTest().start();
	}

	public void run() {
		Torrent myTorrent;
		try {
			myTorrent = new Torrent(
					new File("data/LePetitPrince-local.torrent"));
			myTorrent.massAnnounce();
		} catch (InvalidFileException e1) {
			e1.printStackTrace();
		}

		// while (true) {
		// myTorrent.massAnnounce();
		// try {
		// sleep(240000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

	}
}
