package gui.Actions;

import java.awt.event.ActionEvent;

import torrent.Torrent;

public class PauseAction extends IconActions {

	public PauseAction(Torrent t) {
		super(t, "Pause Downloading");
	}

	public void actionPerformed(ActionEvent arg0) {
		super.pause();
	}

}
