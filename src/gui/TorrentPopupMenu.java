package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;

import torrent.Torrent;

public class TorrentPopupMenu extends JPopupMenu {
	private JButton announce;
	private Torrent t;

	public TorrentPopupMenu(Torrent torrent) {
		this.t = torrent;
		announce = new JButton("Start Downloading");
		add(announce);
		announce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.massAnnounce();
			}
		});
	}
}

class TEstTorrentPOpupMenu {
	public static void main(String[] args) {
		JFrame fen = new JFrame("test");
		fen.getContentPane().add(
				new TorrentPopupMenu(new Torrent(new File("data/G6.torrent"))));
		fen.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
		}
		});
		fen.setVisible(true);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}