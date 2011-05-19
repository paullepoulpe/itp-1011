package testGUI;

import gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

import settings.GeneralSettings;
import torrent.Torrent;

public class MainFrameTest1 {
	public static void main(String[] args) {
		GeneralSettings.readFromFile();
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		// tor.add(new Torrent(new File("data/LePetitPrince.torrent")));
		tor.add(new Torrent(new File("data/himym.torrent")));
		new Thread(new MainFrame(tor)).start();
	}
}
/*
 * methode getSelectedRow dand TorrentTable
 */
