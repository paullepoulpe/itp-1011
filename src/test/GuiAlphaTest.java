package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import torrent.Torrent;
import torrent.peer.Peer;

class GuiAlpha extends JFrame implements Runnable {
	private Torrent myTorrent;
	private JTabbedPane tabs;
	private JPanel tabInfo, tabTracker;
	private JTextArea info,peers;

	public GuiAlpha() {
		setPreferredSize(new Dimension(1280, 720));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		myTorrent = new Torrent(new File("data/G6.torrent"));
		this.setTitle("Torrent : "+myTorrent.getMetainfo().getFileName());
		tabs = new JTabbedPane();
		tabInfo = new JPanel();
		tabTracker = new JPanel();
		tabInfo.setLayout(new BorderLayout());
		info = new JTextArea(30, 50);
		info.setText(myTorrent.getMetainfo().toString());
		peers = new JTextArea(10,30);
//		peers.append("TEST POUR VOIR SI QQCH S'AFFICHE");
		tabs.setTabPlacement(JTabbedPane.LEFT);
		JScrollPane bob = new JScrollPane(peers);
		bob.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane jane = new JScrollPane(info);
		jane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tabTracker.add(bob);
		tabInfo.add(jane,BorderLayout.CENTER);
		tabs.addTab("Info", tabInfo);
		tabs.addTab("Peers", tabTracker);
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
			SwingUtilities.updateComponentTreeUI(tabs);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
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
				for (Peer i : peerList){
//					int j=0;
//					tracker.append("\n blabla numero "+j++);
					peers.append(i.toString());
				}
				Thread.sleep(240000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			myTorrent.massAnnounce();
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