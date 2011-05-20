package gui;

import gui.TorrentTabs.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;

import torrent.Torrent;

public class TorrentTabPane extends JPanel implements MouseListener {
	private JPanel info, peers, pieces;
	private JTabbedPane tabs;
	private JToolBar buttons;
	private JButton play, stop;
	private Torrent currentTorrent;

	public TorrentTabPane() {
		tabs = new JTabbedPane(JTabbedPane.BOTTOM);
		info = new JPanel(){
			public void paintComponent(java.awt.Graphics g) {
				g.translate(getSize().width/4, 200);
				g.setFont(new Font(g.getFont().getFontName(),g.getFont().getStyle() ,20));
				g.drawString("Start downloading by clicking on the play button\n or by right-clicking the torrent. ", 0, 0);
			}
		};
		peers = new JPanel();
		pieces = new JPanel();
		addTabs();
		setLayout(new BorderLayout());
		add(tabs, BorderLayout.CENTER);
		
		buttons = new JToolBar("Torrent Controls", JToolBar.HORIZONTAL);
		
		add(buttons, BorderLayout.SOUTH);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		currentTorrent = ((TorrentTable) ((JTable) e.getSource()).getParent()
				.getParent().getParent()).getSelectedTorrent();
		contructAllTabs();
	}

	private void contructAllTabs() {
		tabs.removeAll();
		info = new TorrentInfoTab(currentTorrent);
		peers = new TorrentPeersTab(currentTorrent);
		pieces = new TorrentPiecesTab(currentTorrent);
		addTabs();
	}

	private void addTabs() {
		tabs.add("Information", info);
		tabs.add("Peer List", peers);
		tabs.add("Pieces", pieces);
	}
}
