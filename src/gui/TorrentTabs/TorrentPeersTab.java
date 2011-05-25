package gui.TorrentTabs;

import gui.TableModels.PeerTableModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;

import torrent.Torrent;
import torrent.peer.Peer;
import torrent.peer.PeerHandler;

/**
 * Onglet qui affiche la liste des {@link Peer}s actifs (avec meme leurs taux de
 * upload et download !)
 * 
 * @author Damien, Maarten
 * 
 */
public class TorrentPeersTab extends JPanel {
	private Torrent torrent;
	private JTable table;
	private PeerTableModel tm;
	private JScrollPane pane;

	public TorrentPeersTab(Torrent t) {
		this.torrent = t;

		tm = new PeerTableModel(torrent.getConnectedPeers());
		table = new JTable(tm);
		table.setPreferredScrollableViewportSize(new Dimension(Toolkit
				.getDefaultToolkit().getScreenSize().width - 150, Toolkit
				.getDefaultToolkit().getScreenSize().height - 500));
		table.setFillsViewportHeight(true);
		table.setDefaultRenderer(Component.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				return (Component) table.getValueAt(row, column);
			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setFillsViewportHeight(true);
		pane = new JScrollPane(table);
		add(pane);
		setBorder(new TitledBorder("Peer list"));

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(500);
						update();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Met a jour la liste des pairs, afin que le tableau reste realiste par
	 * rapport a ce qu'il se passe reellement
	 */
	private void update() {
		tm = new PeerTableModel(torrent.getConnectedPeers());
		table.setModel(tm);
		table.doLayout();
	}

}