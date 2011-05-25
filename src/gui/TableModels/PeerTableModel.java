package gui.TableModels;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import torrent.peer.*;

/**
 * Model de table pour la liste des {@link Peer}s actif. Colonnes "Peer IP",
 * "Port", "Upload", "Download".
 * 
 * @author Damien, Maarten
 * 
 */
public class PeerTableModel extends AbstractTableModel {
	private Object[][] data;
	static final String[] colNames = { "Peer IP", "Port", "Upload", "Download", "ID" };

	public PeerTableModel(ArrayList<PeerHandler> peers) {
		data = new Object[peers.size()][colNames.length];
		for (int i = 0; i < data.length; i++) {
			PeerHandler current = peers.get(i);
			Peer currentPeer = current.getPeer();
			data[i][0] = currentPeer.getIpAdress().toString().substring(1);
			data[i][1] = currentPeer.getPort() + "";
			data[i][2] = current.getUpload();
			data[i][3] = current.getDownload();
			data[i][4] = current.getId();
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
	public Class getColumnClass(int columnIndex) {
		return data[0][columnIndex].getClass();
	}
}
