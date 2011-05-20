package testGUI;

import gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import settings.GeneralSettings;
import torrent.Torrent;

public class MainFrameTest1 {
	public static void main(String[] args) {
		// GeneralSettings.readFromFile();
		JOptionPane
				.showMessageDialog(
						null,
						"La methode stop de Torrent n'arret pas le telechargement, mais sinon bravo pour l'ecriture en continu et la lecture a partir du fichier, c'est vraiment pire cool !!!!");
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		// tor.add(new Torrent(new File("data/LePetitPrince-local.torrent")));
		tor.add(new Torrent(new File("data/glee.torrent")));
		new Thread(new MainFrame(tor)).start();
	}
}