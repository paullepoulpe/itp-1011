package gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import torrent.Torrent;

public class TorrentTable extends JPanel {
	private JTable table;
	private JPopupMenu popup;
	private ArrayList<Torrent> torrentlist;

	public TorrentTable(ArrayList<Torrent> t) {
		torrentlist = t;
		setLayout(new FlowLayout());
		table = constructTable();
		popup = new JPopupMenu();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					int i = table.getSelectedRow();
					popup  = new TorrentPopupMenu(torrentlist.get(i));
					popup.show(null, e.getXOnScreen(), e.getYOnScreen());
				}
			}
		});
		add(new JScrollPane(table));
	}

	public JTable constructTable() {
		removeAll();
		TorrentTableModel tm = new TorrentTableModel(torrentlist);
		table = new JTable(tm);
		table.setPreferredScrollableViewportSize(new Dimension(900, 100));
		table.getColumnModel().getColumn(0).setPreferredWidth(500);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.setDefaultRenderer(Component.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				return (Component) table.getValueAt(row, column);
			}
		});
		System.out.println("New table created");
		return table;
	}

	public int getSelectedRow() {
		return table.getSelectedRow();
	}
}

class TorrentTestTable {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
		tor.add(new Torrent(new File("data/G6.torrent")));
//		tor.add(new Torrent(new File("data/BEP.torrent")));
		TorrentTable tab = new TorrentTable(tor);
		JFrame fen = new JFrame("test");
		fen.getContentPane().add(tab);
		fen.pack();
		fen.setVisible(true);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}