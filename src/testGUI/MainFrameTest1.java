package testGUI;

import gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

import torrent.Torrent;

public class MainFrameTest1 {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		tor.add(new Torrent(new File("data/glee.torrent")));
//		tor.add(new Torrent(new File("data/BEP.torrent")));
		new Thread( new MainFrame(tor)).start();
	}
}
/*
 * methode getSelectedRow dand TorrentTable
 */
