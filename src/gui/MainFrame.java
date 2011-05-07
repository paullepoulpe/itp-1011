package gui;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

import torrent.Torrent;

public class MainFrame extends JFrame {
	private ArrayList<Torrent> torrentz;
	private JPanel listeTorrent;
	private MenuBar menu;
	private JTabbedPane torrent;

	public MainFrame(/*ArrayList<Torrent> torrents*/) {
		setLayout(new BorderLayout());
		Container c = getContentPane();
		menu =new MenuBar();
		c.add(menu, BorderLayout.NORTH);
		
		
		
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
