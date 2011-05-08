package testGUI;

import gui.MainFrame;
import gui.MenuBar;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;

import src.gui.*;
import torrent.Torrent;

public class MainFrameTest1 {
	public static void main(String[] args) {
		ArrayList<Torrent> tor = new ArrayList<Torrent>();
//		tor.add(new Torrent(new File("data/G6.torrent")));
//		tor.add(new Torrent(new File("data/BEP.torrent")));
		MainFrame fen = new MainFrame(tor);
	}
}
