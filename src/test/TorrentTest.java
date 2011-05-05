/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import torrent.Torrent;

public class TorrentTest extends Thread {
	public static void main(String[] args) {
		new TorrentTest().start();
	}

	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1]
					.getClassName());
			Torrent myTorrent = new Torrent(new File("data/optic.torrent"));

			while (true) {
				myTorrent.massAnnounce();
				try {
					sleep(240000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
