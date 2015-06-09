package gui.TableModels;

import gui.TorrentTable;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import torrent.Torrent;

/**
 * Model pour {@link JTable} pour la {@link TorrentTable}, avec colonnes
 * "Filename", "Progress", "Size", "Upload", "Download"
 * 
 * @author Damien, Maarten
 * 
 */
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
			to[i][2] = ti.getMetainfo().getSizeString();
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

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return to[rowIndex][columnIndex];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			to[rowIndex][columnIndex] = aValue;
		} catch (ArrayIndexOutOfBoundsException e) {

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
