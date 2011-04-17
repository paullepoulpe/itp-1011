package test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import torrent.Torrent;

class GuiAlpha extends JFrame implements Runnable{
	private Torrent myTorrent;
	private JTextArea info;
	public GuiAlpha() {
		setPreferredSize(new Dimension(1280,720));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
		myTorrent = new Torrent(new File("data/Friday_RB.torrent"));
		info = new JTextArea(myTorrent.getMetainfo().toString());
		getContentPane().add(info);
		pack();
	}

	public void run() {
		while (true) {
			myTorrent.massAnnounce();
			try {
				Thread.sleep(240000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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