package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import torrent.*;

import java.io.*;
import java.util.ArrayList;

public class TorrentInfoTab extends JPanel {
	private String name;
	private Torrent torrent;

	public TorrentInfoTab(String name) {
		this.name = name;
		add(new JButton("TEST"));
		setBorder(new TitledBorder("INFO TAB"));
		torrent = ((MainFrame)getParent()).selectedTorrent();
		System.out.println(torrent.getMetainfo().getFileName());
	}
	public String getName() {
		return name;
	}
}

class TorrentInfoTabtest {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
//		tor.add(new Torrent(new File("data/glee.torrent")));
		tor.add(new Torrent(new File("data/BEP.torrent")));
		new Thread( new MainFrame(tor)).start();
	}
}