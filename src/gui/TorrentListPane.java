package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.*;

import torrent.*;

public class TorrentListPane extends JList {
	private JPopupMenu menu;
	private ArrayList<Torrent> torrents;
	private JMenuItem start;

	public TorrentListPane(ArrayList<Torrent> list) {
		torrents = list;
		String[] listNames = new String[torrents.size()];
		for (int i = 0; i < listNames.length; i++) {
			listNames[i] = torrents.get(i).getMetainfo().getFileName();
		}
		setListData(listNames);
		menu = new JPopupMenu("POPUP");
		start = new JMenuItem("Start Downloading");
		menu.add(start);
		start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getSource()==start){
					menu.setVisible(false);
					JOptionPane.showMessageDialog(null, "MAssAnnounce appelé");
					torrents.get(getSelectedIndex()).massAnnounce();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int index = getSelectedIndex();
				if (e.isPopupTrigger())
					menu.show(null, e.getXOnScreen(), e.getYOnScreen());
				revalidate();
			}
		});
	}

	public void setListData(ArrayList<Torrent> l) {
		String[] t = new String[l.size()];
		for (int i = 0; i < t.length; i++) {
			t[i] = l.get(i).getMetainfo().getFileName();
		}
		super.setListData(t);
	}
}
