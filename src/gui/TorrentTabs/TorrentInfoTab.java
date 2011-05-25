package gui.TorrentTabs;

import gui.FunnyBar;
import gui.Actions.OpenDirectoryAction;
import gui.Actions.StartAction;
import gui.Actions.StopAction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import torrent.Torrent;

/**
 * Cette classe est le paneau qui contient la plupart des informations presentes
 * dans le torrent, ainsi qu'un jolie barre de telechargement montrant
 * visuellement quelles pieces sont deja telechargees.
 * 
 * @author Damien, Maarten
 * 
 */
public class TorrentInfoTab extends JPanel {
	private Torrent torrent;
	private Box vBox1, vBox2;
	private JToolBar buttons;
	private JButton play, stop, openFolder, trackers, files;
	private JLabel name, size, downloadFolder, author, creationDate, comment,
			hash;
	private JPanel fields0;
	private FunnyBar funnyBar;

	public TorrentInfoTab(Torrent t) {
		torrent = t;
		setLayout(new BorderLayout());
		vBox1 = Box.createVerticalBox();
		vBox1.setBorder(new TitledBorder("Download Information"));
		vBox2 = Box.createVerticalBox();
		vBox2.setBorder(new TitledBorder("Torrent information"));
		funnyBar = torrent.getPieceManager().getFunnyBar();

		fields0 = new JPanel(new BorderLayout());
		fields0.setPreferredSize(new Dimension(this.getWidth(), 70));
		funnyBar.setParent(null);
		fields0.add(new JLabel("Download progress : "), BorderLayout.WEST);
		fields0.add(funnyBar, BorderLayout.CENTER);
		fields0.add(new DynamicPercent(), BorderLayout.EAST);
		vBox1.add(fields0);

		add(vBox1, BorderLayout.NORTH);

		JPanel fields = new JPanel(new GridLayout(0, 2));

		fields.add(new JLabel("Torrent name: "));
		name = new JLabel(torrent.getMetainfo().getFileName());
		fields.add(name);

		fields.add(new JLabel("Torrent size:"));
		size = new JLabel(torrent.getMetainfo().getSizeString());
		fields.add(size);

		fields.add(new JLabel("This torrent will be downloaded in :"));
		downloadFolder = new JLabel(torrent.getDownloadinFolder()
				.getAbsolutePath());
		fields.add(downloadFolder);

		fields.add(new JLabel("Created by :"));
		author = new JLabel(torrent.getMetainfo().getCreatedBy());
		fields.add(author);

		fields.add(new JLabel("Creation date :"));
		creationDate = new JLabel(torrent.getMetainfo().getCreationDate()
				.toGMTString());
		fields.add(creationDate);

		fields.add(new JLabel("Number of pieces:"));
		fields.add(new JLabel(torrent.getMetainfo().getNbPieces() + ""));

		fields.add(new JLabel("Piece size:"));
		fields.add(new JLabel(torrent.getMetainfo().getPieceLengthString()));

		fields.add(new JLabel("Hash du torrent:"));
		hash = new JLabel(torrent.getMetainfo().getInfoHash().toString());
		fields.add(hash);

		fields.add(new JLabel("Port du Torrent:"));
		fields.add(new JLabel(torrent.getNumPort() + ""));

		fields.add(new JLabel("Comments "));
		comment = new JLabel(torrent.getMetainfo().getComment());
		fields.add(comment);
		vBox2.add(fields);

		JPanel fields5 = new JPanel(new GridLayout(1, 0));
		if (torrent.getMetainfo().isMultifile()) {
			files = new JButton("Show file list");
			fields5.add(files);
			files.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileListDialog fileList = new FileListDialog(
							(JFrame) getTopLevelAncestor(), torrent);
					fileList.setLocationRelativeTo(getTopLevelAncestor());
					fileList.setVisible(true);
				}
			});
		}
		trackers = new JButton("Show tracker list");
		trackers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TrackerListDialog trackerList = new TrackerListDialog(
						(JFrame) getTopLevelAncestor(), torrent);
				trackerList.setLocationRelativeTo(getTopLevelAncestor());
				trackerList.setVisible(true);
			}
		});

		fields5.add(trackers);
		vBox2.add(Box.createGlue());
		vBox2.add(Box.createVerticalStrut(50));
		vBox2.add(Box.createGlue());
		vBox2.add(fields5);
		add(vBox2);

		buttons = new JToolBar("Torrent Controls", JToolBar.HORIZONTAL);
		buttons.setFloatable(false);
		play = new JButton(new StartAction(torrent));
		stop = new JButton(new StopAction(torrent));
		openFolder = new JButton(new OpenDirectoryAction(torrent));
		addButtons();
		add(buttons, BorderLayout.SOUTH);
	}

	/**
	 * Methode pour ajouter les boutons a la barre d'outils
	 */
	private void addButtons() {
		buttons.add(play);
		buttons.add(stop);
		buttons.add(openFolder);
	}

	/**
	 * {@link JLabel} dynamique qui lis le pourcentage de telechargement du
	 * torrent et l'affiche
	 * 
	 * @author Damien, Maarten
	 * 
	 */
	class DynamicPercent extends JLabel implements Runnable {
		public DynamicPercent() {

			super(" "
					+ Math.floor(torrent.getPieceManager()
							.getDownloadedCompleteness() * 100) / 100 + " %");
			this.setPreferredSize(new Dimension(60, 14));
			new Thread(this).start();
		}

		@Override
		public void run() {
			while (true) {
				if (torrent.getDownloadingStatus() != Torrent.STOPPED) {
					double p = torrent.getPieceManager()
							.getDownloadedCompleteness() * 100;
					setText(" " + Math.floor(p) / 100 + " %");
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				this.revalidate();
			}
		}

	}

}