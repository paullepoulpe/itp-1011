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

		Torrent myTorrent = new Torrent(new File("data/kesako.torrent"));

		// OutputStream output = new OutputStream() {
		//
		// @Override
		// public void write(int b) throws IOException {
		// // TODO Auto-generated method stub
		//
		// }
		// };
		// System.setErr(new PrintStream(output));

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
