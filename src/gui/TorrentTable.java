package gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import torrent.Torrent;

public class TorrentTable extends JPanel{
	private Vector<TorrentVector> tor;
	private JTable table;

	public TorrentTable(ArrayList<Torrent> t) {
		table = new JTable(new TorrentTableModel(t));
		table.getColumnModel().getColumn(1).getCellRenderer().getTableCellRendererComponent(table,null, true, true, 0, 1);
		add(table);
	}

	public void addTorrent(Torrent t) {

	}
}

class TorrentVector extends Vector<Torrent> {
	private String name;
	private JProgressBar percent;
	private JLabel size;

	public TorrentVector(Torrent t) {
		name = t.getMetainfo().getFileName();
		percent = new JProgressBar(0, 100);
		percent.setValue((int) Math.floor(t.getDownloadedCompleteness() * 100) / 100);
		size = new JLabel(t.getMetainfo().getSize() + " Bytes");
	}
}

class TorrentTestTable {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		tor.add(new Torrent(new File("data/G6.torrent")));
		// tor.add(new Torrent(new File("data/BEP.torrent")));
		TorrentTable tab = new TorrentTable(tor);
		JFrame fen = new JFrame("test");
		fen.getContentPane().add(tab);
		fen.setVisible(true);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}