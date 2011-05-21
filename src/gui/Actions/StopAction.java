package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import torrent.Torrent;

public class StopAction extends IconActions {
	public StopAction() {
		super("Stop download");
	}

	public StopAction(Torrent t) {
		super(t, "Stop download");
		setEnabled(isDownloading());
		if (isDownloading()) {
			putValue(SHORT_DESCRIPTION, "Stop the selected downloading torrent");
		} else {
			putValue(SHORT_DESCRIPTION, "This torrent is not downloading");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.stop();
	}
}
