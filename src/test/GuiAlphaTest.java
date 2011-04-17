package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import torrent.Torrent;
import torrent.peer.Peer;
/**
 * Ceci est un brouillon !!!!!!!!!
 * 
 * @author Utilisateur
 *
 */
class GuiAlpha extends JFrame implements Runnable, ActionListener {
	private Torrent myTorrent;
	private JTabbedPane tabs;
	private JPanel tabInfo, tabTracker;
	private JTextArea info, peers;

	public GuiAlpha() {
		setPreferredSize(new Dimension(1280, 720));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		myTorrent = new Torrent(new File("data/Friday_RB.torrent"));
		this.setTitle("Torrent : " + myTorrent.getMetainfo().getFileName());
		tabs = new JTabbedPane();
		tabInfo = new JPanel();
		tabTracker = new JPanel();
		tabInfo.setLayout(new BorderLayout());
		info = new JTextArea(myTorrent.getMetainfo().toString());
		peers = new JTextArea(10, 30);
		peers.setSize(this.getSize());
		peers.append("TEST POUR VOIR SI QQCH S'AFFICHE");
		tabs.setTabPlacement(JTabbedPane.LEFT);
		JScrollPane bob = new JScrollPane(peers);
		bob.setSize(300, 300);
		peers.setSize(300, 300);
		bob.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tabTracker.add(bob);
		tabInfo.add(info, BorderLayout.CENTER);
		JButton bouton = new JButton("Announce !");
		bouton.addActionListener(this);
		tabInfo.add(bouton, BorderLayout.SOUTH);
		tabs.addTab("Info", tabInfo);
		tabs.addTab("Tracker", tabTracker);
		getContentPane().add(tabs);
		pack();
		setVisible(true);
	}

	public void run() {
		myTorrent.massAnnounce();
		ArrayList<Peer> peerList = myTorrent.getPeerList();
		while (true) {
			try {
				Thread.sleep(3000);
				for (Peer i : peerList) {
					// int j=0;
					// tracker.append("\n blabla numero "+j++);
					peers.append(i.toString());
				}
				Thread.sleep(240000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myTorrent.massAnnounce();

	}
}

public class GuiAlphaTest {
	public static void main(String[] args) {
		GuiAlpha fen = new GuiAlpha();
		Thread t = new Thread(fen);
		t.start();
	}
}