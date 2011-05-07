package gui;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

import torrent.Torrent;

public class MainFrame extends JFrame {
	private ArrayList<Torrent> torrentz;
	private JPanel listeTorrent;
	private JTabbedPane torrent;

	public MainFrame() {
		setLayout(new BorderLayout());
		
		
		
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
