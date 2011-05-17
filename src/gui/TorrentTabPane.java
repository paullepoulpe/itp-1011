package gui;

import java.awt.event.*;

import javax.swing.*;

import torrent.Torrent;

public class TorrentTabPane extends JTabbedPane implements MouseListener {
	private JPanel info, peers, pieces;
	private Torrent currentTorrent;

	public TorrentTabPane() {
		setTabPlacement(BOTTOM);
		info = new JPanel();
		peers = new JPanel();
		pieces = new JPanel();
		addTabs();
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
		removeAll();
		info = new TorrentInfoTab(currentTorrent);
//		peers = new TorrentPeersTab(currentTorrent);
		pieces = new TorrentPiecesTab(currentTorrent);
		addTabs();
	}
	private void addTabs(){
		add("Information", info);
		add("Peer List", peers);
		add("Pieces", pieces);
	}
}
