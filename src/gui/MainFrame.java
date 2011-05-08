package gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import torrent.Torrent;

public class MainFrame extends JFrame {
	private ArrayList<Torrent> torrentz;
	private TorrentListPane listeTorrent;
	private MenuBar menu;
	private JTabbedPane torrent;

	public MainFrame(ArrayList<Torrent> torrents) {
		this.torrentz = torrents;
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		menu = new MenuBar();
		menu.getAdd().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addTorrent();
			}
		});
		c.add(menu, BorderLayout.NORTH);

		listeTorrent = new TorrentListPane(torrentz);
		c.add(listeTorrent, BorderLayout.CENTER);
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/gui/ico2.png"));
		setSize(new Dimension(800, 500));
		if(torrentz.size()==0)
			addTorrent();
		setVisible(true);
	}

	public void addTorrent() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter ext = new FileNameExtensionFilter(
				"*.torrent Files", "torrent");
		chooser.setFileFilter(ext);
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")
				+ File.separator + "Downloads"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.showDialog(null, "Start downloading !");
		Torrent t = new Torrent(new File(chooser.getSelectedFile()
				.getAbsolutePath()));
		this.torrentz.add(t);
		if (JOptionPane.YES_OPTION == JOptionPane
				.showConfirmDialog(
						rootPane,
						"Do you want to start downloading immediately ?"
								+ "\nYou can choose to start downloading manually by right-clicking the torrent afterwards.",
						"Start downloading", JOptionPane.YES_NO_OPTION)) {
			t.massAnnounce();
		}
		listeTorrent.setListData(torrentz);
	}
}