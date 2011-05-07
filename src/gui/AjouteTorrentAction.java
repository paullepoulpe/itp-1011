package gui;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Cette action doit ajouter un torrent a la liste des torrents a telecharger
 * @author MAARTEN
 *
 */
public class AjouteTorrentAction extends AbstractAction {
	private String torrentFile;
	private static boolean SPACE_FULL;
	

	public AjouteTorrentAction() {
		super("Add Torrent");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SPACE_FULL) {
			JOptionPane
					.showMessageDialog(
							null,
							"There are too many torrents downloading!" +
							"\nPlease try again when some downloads are finished, or cancel some downloads.",
							"Too many downloads", JOptionPane.ERROR_MESSAGE);
		} else {
			JFileChooser chooser = new JFileChooser();
			try {
				UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
				chooser.updateUI();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			FileNameExtensionFilter ext = new FileNameExtensionFilter(
					"*.torrent Files", "torrent");
			chooser.setFileFilter(ext);
			chooser.setCurrentDirectory(new File(System.getProperty("user.home")+File.separator+"Downloads"));
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			chooser.showDialog(null, "Start downloading !");
			torrentFile = chooser.getSelectedFile().getAbsolutePath();
		}
	}

	public String getTorrentFile() {
		return torrentFile;
	}
	public static void setSPACE_FULL(boolean sPACE_FULL) {
		SPACE_FULL = sPACE_FULL;
	}
}
