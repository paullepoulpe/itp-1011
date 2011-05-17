package gui.TorrentTabs;

import gui.TableModels.PeerTableModel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import torrent.Torrent;
import torrent.peer.Peer;

public class TorrentPeersTab extends JPanel {
	private Torrent torrent;
	private JTable table;
	public TorrentPeersTab(Torrent t) {
		this.torrent = t;
		
/*		PeerTableModel tm = new PeerTableModel(Liste de pairs);*/
		table = new JTable(/*tm*/);

		add(new JScrollPane(table));
		setBorder(new TitledBorder("Peer list"));
	}
	public TorrentPeersTab (ArrayList<Peer> p){
		PeerTableModel tm = new PeerTableModel(p);
		table = new JTable(tm);
		add(new JScrollPane(table));
	}
}
class TorrentPeersTabTest {
	public static void main(String[] args) {
		JFrame fen = new JFrame();
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Peer p1 = null;
		Peer p2 = null;
		try {
			p1 = new Peer(InetAddress.getLocalHost(), 1133);
			p2 = new Peer(InetAddress.getLocalHost(), 1000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ArrayList<Peer> list = new ArrayList<Peer>();
		list.add(p1);
		list.add(p2);
		TorrentPeersTab tab = new TorrentPeersTab(list);
		fen.getContentPane().add(tab);
		fen.setVisible(true);
	}
}