package gui;

import javax.swing.*;

import settings.GeneralSettings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import gui.Actions.*;

/**
 * Classe qui encapsule la barre de menu de notre programme. Contient trois
 * menus: File, Options, Exit.
 * 
 * @author Damien, Maarten
 * 
 */
public class MenuBar extends JMenuBar {
	private JMenu file, options, help;
	private JMenuItem add, openDownloads, exit, settings;

	public MenuBar() {
		file = new JMenu("File");
		options = new JMenu("Options");
		help = new JMenu("Help");
		add = new JMenuItem("Add Torrent");
		openDownloads = new JMenuItem("Open default download folder");
		exit = new JMenuItem("Exit");
		settings = new JMenuItem("Preferences");
		file.add(add);
		file.add(openDownloads);
		openDownloads.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(
								GeneralSettings.DOWNLOADING_FOLDER);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
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
		options.add(settings);
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

	public JMenuItem getSettings() {
		return settings;
	}
}
