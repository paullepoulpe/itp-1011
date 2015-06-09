package gui.TorrentTabs;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import torrent.Torrent;

/**
 * Boite de dialogue qui affiche les trackers sous forme de liste.
 * 
 * @author Damien, Maarten
 * 
 */
public class TrackerListDialog extends JDialog {
	private Torrent t;
	private JList list;

	public TrackerListDialog(JFrame owner, Torrent torrent) {
		super(owner, true);
		setLayout(new BorderLayout());
		setTitle("Tracker list");
		this.t = torrent;
		add(new JLabel("Tracker list for torrent "
				+ t.getMetainfo().getFileName()), BorderLayout.NORTH);
		list = new JList(t.getMetainfo().getTrackerList().toArray());
		add(new JScrollPane(list), BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}
}