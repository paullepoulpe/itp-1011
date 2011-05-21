package gui.Actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;

import torrent.Torrent;

public class OpenDirectoryAction extends IconActions {
	public OpenDirectoryAction() {
		super("Open downloading folder");
	}

	public OpenDirectoryAction(Torrent t) {
		super(t, "Open downloading folder");
		setEnabled(Desktop.isDesktopSupported());
		if (Desktop.isDesktopSupported()) {
			putValue(SHORT_DESCRIPTION, "Open the downloading folder");
		} else {
			putValue(SHORT_DESCRIPTION,
					"Can't open the downloading folder, desktop is not supported on this machine!");
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			Desktop.getDesktop().open(getDownloadFile());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
