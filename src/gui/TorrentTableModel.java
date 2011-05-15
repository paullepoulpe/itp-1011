package gui;

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

public class TorrentTableModel extends AbstractTableModel implements
		PropertyChangeListener {
	private Object[][] to;
	static final String[] colNames = { "Filename", "Progress", "Size",
			"Upload", "Download" };

	public TorrentTableModel(ArrayList<Torrent> t) {
		to = new Object[t.size()][colNames.length];
		for (int i = 0; i < t.size(); i++) {
			Torrent ti = t.get(i);
			to[i][0] = ti.getMetainfo().getFileName();
			to[i][1] = ti.getProgressBar();
			// UpdateProgressBar updater = new UpdateProgressBar(ti, i);
			// updater.addPropertyChangeListener(this);
			// updater.execute();
			to[i][2] = t.get(i).getMetainfo().getSize() + " Bytes";
			to[i][3] = ti.getUpload();
			to[i][4] = ti.getDownload();
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
		System.err.println("Value read in TableModel at row " + rowIndex
				+ " col " + columnIndex);
		return to[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		System.err.println("Value set in TableModel at row " + rowIndex
				+ " col " + columnIndex);
		to[rowIndex][columnIndex] = aValue;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		for (int i = 0; i < to.length; i++) {
			System.err.println("NEW VALUE @ " + System.currentTimeMillis()
					+ " --> " + evt.getNewValue());
			// int progress = (Integer) evt.getNewValue();
			// ((JProgressBar) to[i][1]).setValue(progress);

		}
	}
}
