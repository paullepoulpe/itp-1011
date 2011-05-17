package gui.TableModels;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;
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
			to[i][1] = ti.getProgressBar();
			to[i][2] = t.get(i).getMetainfo().getSizeString();
			to[i][3] = /* ti.getUpload() */456;
			to[i][4] = /* ti.getDownload() */234;
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

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return to[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			to[rowIndex][columnIndex] = aValue;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Tried updating value at "
					+ rowIndex + " " + columnIndex + "which is object : "
					+ to[rowIndex][columnIndex]);
		}
	}

	public String getColumnName(int col) {
		return colNames[col];
	}
	@Override
	public Class getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}
}
