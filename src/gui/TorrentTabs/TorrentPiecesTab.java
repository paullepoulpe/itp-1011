package gui.TorrentTabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import gui.TableModels.PieceListModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;

import torrent.Torrent;

public class TorrentPiecesTab extends JPanel {
	private Torrent torrent;
	private JTable pieceTable;

	private PieceListModel tm;

	public TorrentPiecesTab(Torrent t) {
		setBorder(new TitledBorder("Downloading Pieces"));
		torrent = t;
		if (t.getPieceManager().getCurrentPieces().length == 0) {
			System.out.println("getCurrentPieces si empty!");
			add(new JLabel("there are "
					+ torrent.getPieceManager().getNbPieces() + " pieces"));
		} else {
			tm = new PieceListModel(this, torrent.getPieceManager()
					.getCurrentPieces());
			pieceTable = new JTable(tm);
			pieceTable.setDefaultRenderer(Component.class,
					new TableCellRenderer() {
						@Override
						public Component getTableCellRendererComponent(
								JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column) {
							return (Component) table.getValueAt(row, column);
						}
					});
			JScrollPane pane = new JScrollPane(pieceTable);
			pieceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			pieceTable.setPreferredScrollableViewportSize(new Dimension(Toolkit
					.getDefaultToolkit().getScreenSize().width - 150, Toolkit
					.getDefaultToolkit().getScreenSize().height - 500));
			add(pane);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(500);
						tm.setData(torrent.getPieceManager().getCurrentPieces());
						tm.fireTableDataChanged();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}

class test {
	public static void main(String[] args) {
		Torrent t = new Torrent(new File("data/glee.torrent"));
		t.massAnnounce();
		JFrame fen = new JFrame();
		JOptionPane.showConfirmDialog(fen, "Lalalala");
		fen.getContentPane().add(new TorrentPiecesTab(t));
		fen.pack();
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setVisible(true);
	}
}