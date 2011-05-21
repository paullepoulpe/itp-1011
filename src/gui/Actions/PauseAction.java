package gui.Actions;

import java.awt.event.ActionEvent;

import torrent.Torrent;

public class PauseAction extends IconActions {
	public PauseAction() {
		super("Pause download");
	}

	public PauseAction(Torrent t) {
		super(t, "Pause download");
		putValue(SHORT_DESCRIPTION, "Pause/Unpause the selected Torrent");
	}

	public void actionPerformed(ActionEvent arg0) {
		super.pause();
	}

}
