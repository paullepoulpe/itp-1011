package testGUI;

import gui.MainFrame;
import gui.MenuBar;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import src.gui.*;

public class MainFrameTest1 {
	public static void main(String[] args) {
		MainFrame fen  =new MainFrame();
//		fen.add(new JButton("Test"));
		fen.add(new MenuBar());
		
	}
}
