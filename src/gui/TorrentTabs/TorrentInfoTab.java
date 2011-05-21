package gui.TorrentTabs;

import gui.FunnyBar;
import gui.Actions.OpenDirectoryAction;
import gui.Actions.PauseAction;
import gui.Actions.StartAction;
import gui.Actions.StopAction;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import torrent.Torrent;

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
	// private GridBagConstraints c;
	private Box vBox;
	private JToolBar buttons;
	private JButton play, pause, stop, openFolder, trackers, files;
	private JLabel name, size, downloadFolder, author, creationDate, comment;
	FunnyBar funnyBar;

	public TorrentInfoTab(Torrent t) {
		torrent = t;
		setBorder(new TitledBorder("Information about "
				+ torrent.getMetainfo().getFileName()));
		setLayout(new BorderLayout());
		vBox = Box.createVerticalBox();
		// setLayout(new GridBagLayout());
		funnyBar = torrent.getPieceManager().getFunnyBar();
		funnyBar.setParent(vBox);
		// add(funnyBar, BorderLayout.NORTH);new JScrollPane
		vBox.add(funnyBar);
		vBox.add(Box.createVerticalStrut(20));
		JPanel fields1 = new JPanel(new BorderLayout());
		name = new JLabel("Torrent name: "
				+ torrent.getMetainfo().getFileName());
		size = new JLabel("Torrent size: "
				+ torrent.getMetainfo().getSizeString());
		fields1.add(name, BorderLayout.CENTER);
		fields1.add(size, BorderLayout.EAST);
		vBox.add(fields1);
		
		JPanel fields1andahalf  =new JPanel(new BorderLayout());
		downloadFolder = new JLabel("This torrent will be downloaded in : "+"A IMPLEMENTER LA METHODE TORRENT.GETDOWNLOADINGFOLDER");
		files = new JButton("Show file list");
		if(torrent.getMetainfo().isMultifile())
			fields1andahalf.add(files, BorderLayout.EAST);
		fields1andahalf.add(downloadFolder, BorderLayout.CENTER);
		vBox.add(fields1andahalf);

		JPanel fields2 = new JPanel(new BorderLayout());
		author = new JLabel("Created by : "
				+ torrent.getMetainfo().getCreatedBy());
		creationDate = new JLabel("Creation date"
				+ torrent.getMetainfo().getCreationDate());
		fields2.add(author, BorderLayout.CENTER);
		fields2.add(creationDate, BorderLayout.EAST);
		vBox.add(fields2);

		JPanel fields3 = new JPanel(new BorderLayout());
		fields3.add(new JLabel("Number of pieces: "
				+ torrent.getMetainfo().getNbPieces()), BorderLayout.CENTER);
		fields3.add(new JLabel("Piece size: "
				+ torrent.getMetainfo().getPieceLengthString()),
				BorderLayout.EAST);
		vBox.add(fields3);

		JPanel fields4 = new JPanel(new BorderLayout());
		comment = new JLabel("Comments: " + torrent.getMetainfo().getComment());
		fields4.add(comment);
		vBox.add(fields4);

		trackers = new JButton("Show tracker list");
		vBox.add(trackers);
		
		add(new JScrollPane(vBox));
		
		buttons = new JToolBar("Torrent Controls", JToolBar.HORIZONTAL);
		add(buttons, BorderLayout.SOUTH);
		play = new JButton(new StartAction(torrent));
		stop = new JButton(new StopAction(torrent));
		openFolder = new JButton(new OpenDirectoryAction(torrent));
		addButtons();
	}

	private void addButtons() {
		buttons.add(play);
		buttons.add(stop);
		buttons.add(openFolder);
	}
	/*
	 * private void setGridbagconstraints(int x, int y, int larg, int haut, int
	 * px, int py) { c.gridx = x; c.gridy = y; c.gridwidth = larg; c.gridheight
	 * = haut; c.weightx = px; c.weighty = py; c.fill = GridBagConstraints.BOTH;
	 * }
	 */
}

class TorrentTabtest {
	public static void main(String[] args) {
		Torrent t = new Torrent(new File("data/BEP.torrent"));
		JFrame fen = new JFrame("Test");
		fen.getContentPane().add(new TorrentInfoTab(t));
		fen.pack();
		fen.setVisible(true);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}