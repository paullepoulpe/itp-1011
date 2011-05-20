package gui;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import torrent.Torrent;
import gui.Actions.*;

public class TorrentPopupMenu extends JPopupMenu {
	private JMenuItem announce, stop, pause;
	private Torrent t;

	public TorrentPopupMenu(Torrent torrent) {
		this.t = torrent;
		stop = new JMenuItem(new StopAction(t));
		announce = new JMenuItem(new StartAction(t));
		pause = new JMenuItem(new PauseAction(t));
		announce.setToolTipText("Start torrent : "
				+ torrent.getMetainfo().getFileName());
		add(announce);
		add(pause);
		add(stop);
	}
}