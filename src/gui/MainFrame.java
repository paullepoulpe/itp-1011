package gui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import torrent.Torrent;

public class MainFrame extends JFrame implements Runnable {
	private ArrayList<Torrent> torrentz;
	private TorrentTable tableTorrent;
	private MenuBar menu;
	private Container c;
	private JTabbedPane torrentInfo;

	public MainFrame(ArrayList<Torrent> torrents) {
		super("MAARTENTORRENT ;P hihihi ^^");
		this.torrentz = torrents;
		c = getContentPane();
		c.setLayout(new BorderLayout());

		try {
			if (System.getProperty("os.name").equals("Linux")) {
				UIManager
						.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1]
								.getClassName());
			} else {
				UIManager
						.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
								.getClassName());
			}
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
		menu.getSettings().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new FenetreSettings(MainFrame.this).showDialog();
			}
		});
		c.add(menu, BorderLayout.NORTH);

		tableTorrent = new TorrentTable(torrentz);
		/*
		 * torrentInfo = new JTabbedPane(); TorrentInfoTab tInfo = new
		 * TorrentInfoTab("Info"); torrentInfo.add(tInfo.getName(), tInfo);
		 * c.add(torrentInfo, BorderLayout.CENTER);
		 */

		c.add(tableTorrent, BorderLayout.SOUTH);
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/gui/ico1.png"));

		setSize(new Dimension(800, 500));
		setVisible(true);
		if (torrentz.size() == 0)
			addTorrent();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(60000);
				validate();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		chooser.showDialog(this, "Add Torrent to downloadlist !");
		Torrent t = new Torrent(new File(chooser.getSelectedFile()
				.getAbsolutePath()));
		remove(tableTorrent);
		torrentz.add(t);
		tableTorrent = new TorrentTable(torrentz);
		c.add(tableTorrent, BorderLayout.SOUTH);
		if (JOptionPane.YES_OPTION == JOptionPane
				.showConfirmDialog(
						rootPane,
						"Do you want to start downloading immediately ?"
								+ "\nYou can choose to start downloading manually by right-clicking the torrent afterwards.",
						"Start downloading", JOptionPane.YES_NO_OPTION)) {
			t.massAnnounce();
		}
		validate();
		tableTorrent.revalidate();
	}

	public Torrent selectedTorrent() {
		return torrentz.get(tableTorrent.getSelectedRow());
	}
}
