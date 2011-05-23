package gui.TorrentTabs;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import torrent.Torrent;

public class GraphTab extends JPanel {
	private Torrent torrent;
	private Box vBox;

	public GraphTab(Torrent t) {
		torrent = t;
		setLayout(new BorderLayout());
		vBox = Box.createVerticalBox();
		vBox.add(torrent.getUpload().getGraph(getSize()));
		vBox.add(Box.createVerticalStrut(10));
		vBox.add(torrent.getDownload().getGraph(getSize()));
		add(vBox, BorderLayout.CENTER);
	}
}
