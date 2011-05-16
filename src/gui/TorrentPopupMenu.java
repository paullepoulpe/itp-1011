package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;

import torrent.Torrent;

public class TorrentPopupMenu extends JPopupMenu {
	private JMenuItem announce, stop;
	private Torrent t;

	public TorrentPopupMenu(Torrent torrent) {
		this.t = torrent;
		stop = new JMenuItem("Stop Downloading");
		announce = new JMenuItem("Start Downloading");
		announce.setToolTipText("Torrent : "
				+ torrent.getMetainfo().getFileName());
		add(announce);
		add(stop);
		announce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.massAnnounce();
				announce.setEnabled(false);
				setVisible(false);
				
			}
		});
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * TODO Auto-generated method stub Le torrent doit s'arrêter...
				 * t.stop();
				 */
				JOptionPane.showMessageDialog(null,
						"DOIT IMPLEMENTER LA METHODE STOP DANS TORRENT!!!!!");
			}
		});
	}
}