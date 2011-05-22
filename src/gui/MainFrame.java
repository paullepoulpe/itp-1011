package gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import torrent.InvalidFileException;
import torrent.Torrent;

public class MainFrame extends JFrame implements Runnable {
	private ArrayList<Torrent> torrentz;
	private TorrentTable tableTorrent;
	private MenuBar menu;
	private Container c;
	private TorrentTabPane torrentInfo;

	public MainFrame(ArrayList<Torrent> torrents) {
		super("DAART");
		requestFocusInWindow();
		this.torrentz = torrents;
		c = getContentPane();
		c.setLayout(new BorderLayout());
		setUI();
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
		c.add(tableTorrent, BorderLayout.SOUTH);

		torrentInfo = new TorrentTabPane();
		tableTorrent.getTable().addMouseListener(torrentInfo);
		c.add(torrentInfo, BorderLayout.CENTER);

		setSize(new Dimension(
				Toolkit.getDefaultToolkit().getScreenSize().width - 100,
				Toolkit.getDefaultToolkit().getScreenSize().height - 100));
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
		Torrent t = null;
		try {
			t = new Torrent(new File(chooser.getSelectedFile()
					.getAbsolutePath()));
		} catch (InvalidFileException e) {
			e.printStackTrace();
		}
		if (t != null && !torrentz.contains(t)) {
			remove(tableTorrent);
			torrentz.add(t);
			tableTorrent = new TorrentTable(torrentz);
			tableTorrent.getTable().addMouseListener(torrentInfo);
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
	}

	public void setUI() {
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
		setResizable(false);
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("src/gui/ico3.png"));
		if(SystemTray.isSupported()){
			Image image = Toolkit.getDefaultToolkit().getImage("src/gui/ico3.png");
			String tooltip = "DAART is running, as fast as lightning, your torrents are probably done downloading ;)";
			PopupMenu popup = new PopupMenu("DAART");
			MenuItem open = new MenuItem("Open DAART");
			open.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MainFrame.this.setExtendedState(NORMAL);
				}
			});
			TrayIcon icon = new TrayIcon(image, tooltip, popup);
			icon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
//					MainFrame.popup.show(icon, e.getX(), e.getY());
				}
				@Override
				public void mousePressed(MouseEvent e) {
					mouseReleased(e);
				}
			});
			try {
				SystemTray.getSystemTray().add(icon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	public Torrent selectedTorrent() {
		return torrentz.get(tableTorrent.getSelectedRow());
	}
}
