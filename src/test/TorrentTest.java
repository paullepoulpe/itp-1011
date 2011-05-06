/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
			OutputStream output = new OutputStream() {

				@Override
				public void write(int b) throws IOException {
					// TODO Auto-generated method stub

				}
			};
			System.setErr(new PrintStream(output));

			Torrent myTorrent = new Torrent(new File("data/kesako.torrent"));

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
