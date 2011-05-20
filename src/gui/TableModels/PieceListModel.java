package gui.TableModels;

import gui.FunnyBar;

import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import torrent.piece.Piece;

public class PieceListModel extends AbstractTableModel {
	static final String[] colNames = { "Piece Number", "Download Progress",
			"Piece completeness", "Number of Blocs (total)",
			"Number of received blocs" };
	private Object[][] data;

	public PieceListModel(Component parent, Piece[] list) {
		data = new Object[list.length][colNames.length];
		for (int i = 0; i < list.length; i++) {
			Piece current = list[i];
			data[i][0] = current.getIndex();
			data[i][1] = current.getFunnyBar();			
			data[i][2] = current.getDownloadCompleteness();
			data[i][3] = current.getNbBlocs();
			data[i][4] = Math.floor(current.getDownloadCompleteness()
					* current.getNbBlocs() / 100);
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

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		return data[0][columnIndex].getClass();
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}
	public void setData(Piece[] list) {
		for (int i = 0; i < list.length; i++) {
			Piece current = list[i];
			data[i][0] = current.getIndex();
			data[i][1] = current.getFunnyBar();			
			data[i][2] = current.getDownloadCompleteness();
			data[i][3] = current.getNbBlocs();
			data[i][4] = Math.floor(current.getDownloadCompleteness()
					* current.getNbBlocs() / 100);
		}
		fireTableDataChanged();
	}
}
