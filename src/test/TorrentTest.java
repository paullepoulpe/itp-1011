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
		
			Torrent myTorrent = new Torrent(new File("data/glee.torrent"));

//			OutputStream output = new OutputStream() {
//
//				@Override
//				public void write(int b) throws IOException {
//					// TODO Auto-generated method stub
//
//				}
//			};
//			System.setErr(new PrintStream(output));




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
