package gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import torrent.peer.*;

public class PeerTableModel extends AbstractTableModel {
	private Object[][] data;
	static final String[] colNames = { "Peer IP", "Port" /*, "Upload", "Download"*/};
	public PeerTableModel(ArrayList<Peer> peers) {
		data = new Object[peers.size()][colNames.length];
		for (int i=0;i<data.length;i++){
			Peer current = peers.get(i);
			data[i][0] = current.getIpAdress().toString();
			data[i][1] = current.getPort();
//			data[i][2] = current.getUpload();
//			data[i][3] = current.getDownload();
		}
	}
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	public String getColumnName(int col) {
		return colNames[col];
	}
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
	}

}
