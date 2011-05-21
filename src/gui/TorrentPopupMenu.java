package gui;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import torrent.Torrent;
import gui.Actions.*;

public class TorrentPopupMenu extends JPopupMenu {
	private IconActions announce, stop, pause, openDirectory;
	private Torrent t;

	public TorrentPopupMenu(Torrent torrent) {
		this.t = torrent;
		stop = new StopAction(t);
		announce = new StartAction(t);
		openDirectory = new OpenDirectoryAction(t);
//		pause = new PauseAction(t);
		add(announce);
//		add(pause);
		add(stop);
		addSeparator();
		add(openDirectory);
		
	}
}