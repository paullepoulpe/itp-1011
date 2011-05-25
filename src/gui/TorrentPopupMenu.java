package gui;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import torrent.Torrent;
import gui.Actions.*;

public class TorrentPopupMenu extends JPopupMenu {
	private IconActions announce, stop, pause, openDirectory, delete;
	private Torrent t;
	private MainFrame mf;

	public TorrentPopupMenu(Torrent torrent/*, MainFrame mf*/) {
		this.t = torrent;
		stop = new StopAction(t);
		announce = new StartAction(t);
		openDirectory = new OpenDirectoryAction(t);
		delete = new DeleteAction(t);
		add(announce);
		add(stop);
		add(delete);
		addSeparator();
		add(openDirectory);

	}
}