package gui;

import gui.Actions.*;
import gui.TorrentTabs.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;

import torrent.Torrent;

public class TorrentTabPane extends JTabbedPane implements MouseListener {
	private JPanel info, peers, pieces, graph;
	private Torrent currentTorrent;

	public TorrentTabPane() {
		setTabPlacement(BOTTOM);
		info = new JPanel() {
			public void paintComponent(java.awt.Graphics g) {
				g.translate(getSize().width / 4, 200);
				g.setFont(new Font(g.getFont().getFontName(), g.getFont()
						.getStyle(), 20));
				g
						.drawString(
								"Start downloading by clicking on the start button\n or by right-clicking the torrent. ",
								0, 0);
			}
		};
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
		try {
			int i = getSelectedIndex();
			Torrent newTorrent = ((TorrentTable) ((JTable) e.getSource())
					.getParent().getParent().getParent()).getSelectedTorrent();
			if (!newTorrent.equals(currentTorrent)) {
				currentTorrent = newTorrent;
				contructAllTabs();
			}
			setSelectedIndex(i);
		} catch (ArrayIndexOutOfBoundsException exc) {

		}
	}

	private void contructAllTabs() {
		removeAll();
		info = new TorrentInfoTab(currentTorrent);
		peers = new TorrentPeersTab(currentTorrent);
		pieces = new TorrentPiecesTab(currentTorrent);
		graph = new GraphTab(currentTorrent);
		addTabs();
	}

	private void addTabs() {
		add("Information", info);
		add("Peer List", peers);
		add("Pieces", pieces);
		add("Graph", graph);
	}
}