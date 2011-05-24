package gui.TorrentTabs;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import torrent.Torrent;

public class FileListDialog extends JDialog {
	private Torrent t;
	private JList list;

	public FileListDialog(JFrame owner, Torrent torrent) {
		super(owner, true);
		this.t = torrent;
		setTitle("File list");
		setLayout(new BorderLayout());
		list = new JList(listFiles());	
		add(new JLabel("File list for torrent " + t.getMetainfo().getFileName()),
				BorderLayout.NORTH);
		add(new JScrollPane(list), BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}

	private String[] listFiles() {
		ArrayList<String[]> FilesPath = t.getMetainfo().getFilesPath();
		String[] s = new String[FilesPath.size()];
		for (int i = 0; i < FilesPath.size(); i++) {
			String si= "";
			for (int j = 0; j < FilesPath.get(i).length; j++) {
				si = FilesPath.get(i)[j];
			}
			s[i]=si;
		}
		return s;
	}
}