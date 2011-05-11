package gui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import torrent.Torrent;

public class TorrentTableModel extends AbstractTableModel {
	private Object[][] to;
	static final String[] colNames = { "Filename", "Progress", "Size",
			"Upload", "Download" };

	public TorrentTableModel(ArrayList<Torrent> t) {
		to = new Object[t.size()][colNames.length];
		for (int i = 0; i < t.size(); i++) {
			Torrent ti = t.get(i);
			to[i][0] = ti.getMetainfo().getFileName();
			to[i][1] = new JProgressBar(0, 100);
			((JProgressBar) to[i][1]).setValue((int) ti
					.getDownloadedCompleteness());
			to[i][2] = new JLabel(t.get(i).getMetainfo().getSize() + " Bytes");
			to[i][3] = ti.getUpload();
			to[i][4] = ti.getDownload();
//			System.out.println("test " + i);
		}

	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return to.length;
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return to[rowIndex][columnIndex];
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	class TorrentProgressBar extends JLabel implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (column == 1) {
				JProgressBar prog = new JProgressBar(0, 100);
				prog.setValue((Integer) value);
				return prog;
			}
			if (column == 2) {
				return new JLabel((String) value);
			}
			return null;
		}

	}
}
