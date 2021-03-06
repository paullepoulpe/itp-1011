package gui.Actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.*;

import torrent.Torrent;

/**
 * Action qui permet de demarrer le telechargement d'un {@link Torrent}.
 * 
 * @author Damien, Maarten
 * 
 */
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

	/**
	 * Demarre le telechargement en appelant la methode
	 * {@link Torrent#massAnnounce()}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (((JComponent) source).getParent() instanceof JToolBar) {
			JToolBar sourceToolBar = (JToolBar) ((JComponent) source)
					.getParent();
			JButton peerButton = (JButton) sourceToolBar.getComponent(1);
			if (peerButton.getText().equals("Stop download")) {
				peerButton.getAction().setEnabled(true);
				this.setEnabled(false);
			}
		}
		super.start();
	}
}
