package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import torrent.Torrent;

public class StartAction extends IconActions {

	public StartAction(Torrent t) {
		super(t, "Start Downloading");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.start();
	}
}
