package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.*;

import torrent.Torrent;

public class TorrentPopupMenu extends JPopupMenu {
	private JButton announce, stop;
	private Torrent t;

	public TorrentPopupMenu(Torrent torrent) {
		announce.setToolTipText("Torrent : "+torrent.getMetainfo().getFileName());
		this.t = torrent;
		stop = new JButton("Stop Downloading");
		announce = new JButton("Start Downloading");
		add(announce);
		add(stop);
		announce.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				t.massAnnounce();
			}
		});
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* TODO Auto-generated method stub
				 * Le torrent doit s'arrêter...
				 * t.stop();
				*/
				
			}
		})
	}
}