package gui;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JPanel{
	private JMenuBar barre;
	private JMenu file, help;
	private JMenuItem exit, about;
	public MenuBar() {
		String s = "TEST";
		barre = new JMenuBar();
		file = new JMenu(s);
		help = new JMenu(s);
		file.add(new AjouteTorrentAction());
		exit = new JMenuItem(s);
		about = new JMenuItem(s);
		file.add(exit);
		help.add(about);
		add(barre);
	}
}
