package gui;

import javax.swing.*;

import torrent.Torrent;

public class TorrentPiecesTab extends JPanel {
	private Torrent torrent;
	private JTable pieceTable;

	public TorrentPiecesTab(Torrent t) {
		torrent = t;
		add(new JButton("TEST : there are "+torrent.getPieceManager().getNbPieces()+" pieces"));
	}
}
