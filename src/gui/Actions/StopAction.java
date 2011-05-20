package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import torrent.Torrent;

public class StopAction extends IconActions {

	public StopAction(Torrent t) {
		super(t,"Stop Downloading");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.stop();
	}
}
