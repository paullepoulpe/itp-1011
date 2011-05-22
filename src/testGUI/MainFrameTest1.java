package testGUI;

import gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import settings.GeneralSettings;
import torrent.Torrent;

public class MainFrameTest1 {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		try {
//			tor.add(new Torrent(new File("data/LePetitPrince-local.torrent")));
			tor.add(new Torrent(new File("data/ju.torrent")));
		} catch (Exception e) {
		}
		new Thread(new MainFrame(tor)).start();
	}
}