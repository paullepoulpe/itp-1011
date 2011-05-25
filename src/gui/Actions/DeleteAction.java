package gui.Actions;

import gui.MainFrame;
import gui.TorrentPopupMenu;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;

import torrent.Torrent;

public class DeleteAction extends IconActions {
	public DeleteAction() {
		super("Delete Torrent");
	}

	public DeleteAction(Torrent t) {
		super(t, "Delete Torrent");
		putValue(SHORT_DESCRIPTION, "Delete this torrent!");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MainFrame mf = (MainFrame) ((JComponent) ((TorrentPopupMenu) ((JComponent) e
				.getSource()).getParent()).getInvoker()).getTopLevelAncestor();
		int n = JOptionPane.showConfirmDialog(mf,
				"Do you really want to delete this torrent ?", "Caution!",
				JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			super.delete(mf);
		}
	}
}
