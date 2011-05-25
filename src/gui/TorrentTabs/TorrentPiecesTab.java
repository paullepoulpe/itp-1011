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
import torrent.piece.Piece;

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
			pieceTable.setFillsViewportHeight(true);

			add(pane);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(500);
						Piece[] data = torrent.getPieceManager()
								.getCurrentPieces();
						if (data != null) {
							tm.setData(data);
						}
					} catch (InterruptedException e) {
					} catch (NullPointerException e) {
					}
				}
			}
		}).start();
	}
}