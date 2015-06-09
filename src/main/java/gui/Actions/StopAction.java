package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import torrent.Torrent;

/**
 * Action permettant de stopper le telechargement.
 * 
 * @author Damien, Maarten
 * 
 */
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

	/**
	 * Stoppe le {@link Torrent} en appelant la methode {@link Torrent#stop()}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (((JComponent) source).getParent() instanceof JToolBar) {
			JToolBar sourceToolBar = (JToolBar) ((JComponent) source)
					.getParent();
			JButton peerButton = (JButton) sourceToolBar.getComponent(0);
			if (peerButton.getText().equals("Start download")) {
				peerButton.getAction().setEnabled(true);
				this.setEnabled(false);
			}
		}
		super.stop();
	}
}
