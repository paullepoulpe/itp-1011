package gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.io.File;

import torrent.Torrent;

public class TorrentTable extends JPanel {
	private JTable table;
	private ArrayList<Torrent> torrentlist;

	public TorrentTable(ArrayList<Torrent> t) {
		torrentlist = t;
		setLayout(new FlowLayout());
		table = constructTable();
		add(new JScrollPane(table));
	}

	public void addTorrent(Torrent t) {
		torrentlist.add(t);
		constructTable();
	}
	public JTable constructTable(){
		removeAll();
		TorrentTableModel tm = new TorrentTableModel(torrentlist);
		table = new JTable(tm);
		table.setPreferredScrollableViewportSize(new Dimension(800, 100));
		table.getColumnModel().getColumn(0).setPreferredWidth(500);
		table.setDefaultRenderer(Component.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				return (Component) table.getValueAt(row, column);
			}
		});
		return table;
	}
}

class TorrentTestTable {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		tor.add(new Torrent(new File("data/G6.torrent")));
		tor.add(new Torrent(new File("data/BEP.torrent")));
		TorrentTable tab = new TorrentTable(tor);
		JFrame fen = new JFrame("test");
		JScrollPane bob = new JScrollPane(tab);
//		fen.getContentPane().add(tab);
		fen.getContentPane().add(bob);
		fen.pack();
		fen.setVisible(true);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}