package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import torrent.Torrent;
import torrent.peer.Peer;

class GuiAlpha extends JFrame implements Runnable, ActionListener {
	private Torrent myTorrent;
	private JTabbedPane tabs;
	private JPanel tabInfo, tabTracker, completeness;
	private JTextArea info;
	private JList peers;
	private JMenuBar menuBar;
	private JMenu menuFile, menuHelp;
	private JMenuItem exit;
	private JLabel percent;

	public GuiAlpha() {
		setPreferredSize(new Dimension(1280, 720));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		myTorrent = new Torrent(new File("data/mariacarree.torrent"));
		this.setTitle("Torrent : " + myTorrent.getMetainfo().getFileName());
		setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuHelp = new JMenu("Help");
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		menuFile.add(exit);
		menuBar.add(menuFile);
		menuBar.add(menuHelp);

		completeness = new JPanel();
		completeness.setLayout(new BorderLayout());
		tabs = new JTabbedPane();
		tabInfo = new JPanel();
		tabTracker = new JPanel();
		tabInfo.setLayout(new BorderLayout());
		info = new JTextArea(30, 50);
		info.setText(myTorrent.getMetainfo().toString());
		String[] in = { "Liste de peers:", "" };
		peers = new JList(in);
		// peers.append("TEST POUR VOIR SI QQCH S'AFFICHE");
		tabs.setTabPlacement(JTabbedPane.LEFT);
		JScrollPane bob = new JScrollPane(peers);
		bob.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane jane = new JScrollPane(info);
		jane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tabTracker.add(bob);
		percent = new JLabel("Download completeness: 0.0 % ");
		peers.setVisibleRowCount(30);
		tabInfo.add(jane, BorderLayout.CENTER);
		tabs.addTab("Info", tabInfo);
		tabs.addTab("Peers", tabTracker);

		completeness.add(percent, BorderLayout.WEST);
		completeness.add(new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(Color.black);
				g.drawRoundRect(0, 0, this.getWidth() - 1,
						this.getHeight() - 1, 1, 1);
				g.setColor(Color.cyan.darker());
				g.fillRoundRect(1, 1,
						(int) ((myTorrent.getDownloadedCompleteness() * (this
								.getWidth() - 2)) / 100.0),
						this.getHeight() - 2, 1, 1);
			}
		}, BorderLayout.CENTER);
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
			SwingUtilities.updateComponentTreeUI(tabs);
			SwingUtilities.updateComponentTreeUI(menuBar);
			SwingUtilities.updateComponentTreeUI(completeness);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		getContentPane().add(menuBar, BorderLayout.NORTH);
		getContentPane().add(completeness, BorderLayout.SOUTH);
		getContentPane().add(tabs, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	public void run() {
		myTorrent.massAnnounce();
		ArrayList<Peer> peerList = myTorrent.getPeerList();
		double percentage = 0;
		try {
			Thread.sleep(3000);
			while (true) {
				Thread.sleep(100);
				percentage = Math
						.floor(myTorrent.getDownloadedCompleteness() * 10) / 10;
				percent.setText("Download completeness: " + percentage + " % ");
				String[] peers = new String[peerList.size()];
				for (int i = 0; i < peerList.size(); i++) {
					peers[i] = peerList.get(i).toString();
				}
				this.peers.setFixedCellWidth(tabs.getWidth() - 100);
				this.peers.setListData(peers);
				completeness.repaint();

				// myTorrent.massAnnounce();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exit) {
			if (JOptionPane
					.showConfirmDialog(this,
							"Are you sure you want to exit? Your download will be cancelled!") == JOptionPane.OK_OPTION)
				System.exit(0);
		}
	}
}

public class GuiAlphaTest {
	public static void main(String[] args) {
		GuiAlpha fen = new GuiAlpha();
		Thread t = new Thread(fen);
		t.start();
	}
}