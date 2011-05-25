package gui.TorrentTabs;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import torrent.Torrent;

/**
 * Classe qui contient les graphiques d'upload et download du torrent
 * selectionne.
 * 
 * @author Damien, Maarten
 * 
 */
public class GraphTab extends JPanel {
	private Torrent torrent;
	private Box vBox;

	public GraphTab(Torrent t) {
		torrent = t;
		setLayout(new BorderLayout());
		vBox = Box.createVerticalBox();
		vBox.add(new JLabel("Upload graph"));
		vBox.add(torrent.getUpload().getGraph(getSize()));
		vBox.add(new JLabel("Download graph"));
		vBox.add(torrent.getDownload().getGraph(getSize()));
		add(vBox, BorderLayout.CENTER);
	}
}
