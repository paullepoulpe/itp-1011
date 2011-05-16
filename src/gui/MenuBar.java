package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {
	private JMenu file, options, help;
	private JMenuItem add, exit;

	public MenuBar() {
		super();
		file = new JMenu("File");
		options = new JMenu("Options");
		options.add(new JMenuItem("Nothing Yet... try again later ;)"));
		help = new JMenu("Help");
		add = new JMenuItem("Add Torrent");
		exit = new JMenuItem("Exit");
		file.add(add);
		file.addSeparator();
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane
						.showConfirmDialog(
								null,
								"Are you sure you want to exit? All downloads will stop.",
								"Confirm exit",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		file.add(exit);
		help.add(new AboutAction());
		add(file);
		add(options);
		add(help);
	}

	public JMenu getFile() {
		return file;
	}

	public JMenu getHelp() {
		return help;
	}

	public JMenuItem getAdd() {
		return add;
	}

	public JMenuItem getExit() {
		return exit;
	}

}
