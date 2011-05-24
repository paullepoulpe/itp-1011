package testGUI;

import gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

import torrent.Torrent;

public class MainFrameTest1 {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		try {
			tor.add(new Torrent(new File("data/eminem.torrent")));
			// tor.add(new Torrent(new File("data/TBBTS04E24.torrent")));
		} catch (Exception e) {
		}
		new MainFrame(tor);
	}
}