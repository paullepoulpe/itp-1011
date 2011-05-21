package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import torrent.Torrent;

public class StartAction extends IconActions {
	public StartAction() {
		super("Start download");
	}

	public StartAction(Torrent t) {
		super(t, "Start download");
		setEnabled(!isDownloading());
		if (!isDownloading()) {
			putValue(SHORT_DESCRIPTION,
					"Start downloading the selected torrent");
		} else {
			putValue(SHORT_DESCRIPTION, "This torrent is already downloading");
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Clicked on start");
		super.start();
	}
}
