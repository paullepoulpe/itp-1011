package gui;

import gui.TorrentTabs.GraphTab;
import gui.TorrentTabs.TorrentInfoTab;
import gui.TorrentTabs.TorrentPeersTab;
import gui.TorrentTabs.TorrentPiecesTab;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import torrent.Torrent;

/**
 * Cette classe contient les differents onglets qui affichent les informations
 * sur le torrent selectionne. Elle ecoute les clicks sur un des torrents de la
 * {@link JTable} de la {@link TorrentTable} et cree des onglets par rapport a
 * ce torrent.
 * 
 * @author Damien, Maarten
 * 
 */
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