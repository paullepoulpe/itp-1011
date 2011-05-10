package gui;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

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
			to[i][1] = new JProgressBar(0,100);
			((JProgressBar)to[i][1]).setValue((int)ti.getDownloadedCompleteness());
			to[i][2] = new JLabel(t.get(i).getMetainfo().getSize()+" Bytes");
			to[i][3] = ti.getUpload();
			to[i][4] = ti.getDownload();
			System.out.println("test "+i);
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

}
