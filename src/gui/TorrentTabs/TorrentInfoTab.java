package gui.TorrentTabs;

import gui.DynamicFlowLabel;
import gui.FunnyBar;
import gui.Actions.OpenDirectoryAction;
import gui.Actions.PauseAction;
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
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import sun.rmi.runtime.NewThreadAction;
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
	private Box vBox1, vBox2;
	private JToolBar buttons;
	private JButton play, stop, openFolder, trackers, files;
	private JLabel name, size, downloadFolder, author, creationDate, comment;
	private JPanel fields0;
	FunnyBar funnyBar;

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

		DynamicFlowLabel upload = torrent.getUpload(), download = torrent
				.getDownload();
		JPanel debit = new JPanel(new GridLayout(1, 4));
		debit.add(new JLabel("Upload rate : "));
		debit.add(upload);
		debit.add(new JLabel("Download rate"));
		debit.add(download);
		fields0.add(debit, BorderLayout.SOUTH);
		vBox1.add(fields0);
		add(vBox1, BorderLayout.NORTH);

		JPanel fields1 = new JPanel(new BorderLayout());
		name = new JLabel("Torrent name: "
				+ torrent.getMetainfo().getFileName());
		size = new JLabel("Torrent size: "
				+ torrent.getMetainfo().getSizeString());
		fields1.add(name, BorderLayout.CENTER);
		fields1.add(size, BorderLayout.EAST);
		vBox2.add(fields1);

		JPanel fields1andahalf = new JPanel(new BorderLayout());
		downloadFolder = new JLabel("This torrent will be downloaded in : "
				+ torrent.getDownloadinFolder().getAbsolutePath());

		fields1andahalf.add(downloadFolder, BorderLayout.CENTER);
		vBox2.add(fields1andahalf);

		JPanel fields2 = new JPanel(new BorderLayout());
		author = new JLabel("Created by : "
				+ torrent.getMetainfo().getCreatedBy());
		creationDate = new JLabel("Creation date : "
				+ torrent.getMetainfo().getCreationDate());
		fields2.add(author, BorderLayout.CENTER);
		fields2.add(creationDate, BorderLayout.EAST);
		vBox2.add(fields2);

		JPanel fields3 = new JPanel(new BorderLayout());
		fields3.add(new JLabel("Number of pieces: "
				+ torrent.getMetainfo().getNbPieces()), BorderLayout.CENTER);
		fields3.add(new JLabel("Piece size: "
				+ torrent.getMetainfo().getPieceLengthString()),
				BorderLayout.EAST);
		vBox2.add(fields3);

		JPanel fields4 = new JPanel(new BorderLayout());
		comment = new JLabel("Comments: " + torrent.getMetainfo().getComment());
		fields4.add(comment);
		vBox2.add(fields4);

		JPanel fields5 = new JPanel(new GridLayout(0, 2));
		if (torrent.getMetainfo().isMultifile()) {
			files = new JButton("Show file list");
			fields5.add(files);
			files.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new FileListDialog(torrent).setVisible(true);
				}
			});
		}
		trackers = new JButton("Show tracker list");
		trackers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new TrackerListDialog(torrent).setVisible(true);
			}
		});
		fields5.add(trackers);
		vBox2.add(fields5);

		add(vBox2);

		buttons = new JToolBar("Torrent Controls", JToolBar.HORIZONTAL);
		buttons.setFloatable(false);
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

	class DynamicPercent extends JLabel implements Runnable {
		public DynamicPercent() {

			super(" "
					+ Math.floor(torrent.getPieceManager()
							.getDownloadedCompleteness() * 100) / 100 + " %");
			this.setPreferredSize(new Dimension(48, 14));
			this.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.revalidate();
				TorrentInfoTab.this.revalidate();
			}
		}

	}
}

class TorrentTabtest {
	public static void main(String[] args) {
		Torrent t = null;
		try {
			t = new Torrent(new File("data/BEP.torrent"));
		} catch (Exception e) {
		}
		JFrame fen = new JFrame("Test");
		TorrentInfoTab tab = new TorrentInfoTab(t);
		fen.getContentPane().add(tab);
		fen.pack();
		fen.setVisible(true);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}