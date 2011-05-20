package gui.TorrentTabs;

import gui.FunnyBar;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import torrent.*;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Cette classe est le paneau qui contient la plupart des informations présentes
 * dans le torrent, ainsi qu'un jolie barre de téléchargement montrant
 * visuellement quelles pieces sont deja telechargees.
 * 
 * @author Damien, Maarten
 * 
 */
public class TorrentInfoTab extends JPanel {
	private Torrent torrent;
	FunnyBar funnyBar;

	public TorrentInfoTab(Torrent t) {
		torrent = t;
		setLayout(new BorderLayout());

		funnyBar = torrent.getPieceManager().getFunnyBar();
		funnyBar.setParent(this);
		add(funnyBar, BorderLayout.NORTH);
		setBorder(new TitledBorder("Informations about "+torrent.getMetainfo().getFileName()));
	}

}

class TorrentInfoTabtest {
	public static void main(String[] args) {
		JFrame fen = new JFrame("Test TOrrentInfoTab");
		Torrent t = new Torrent(new File("data/glee.torrent"));
		TorrentInfoTab tab = new TorrentInfoTab(t);
		fen.getContentPane().add(tab);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setVisible(true);
	}
}