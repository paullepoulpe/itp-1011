/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

public class ChoixFichier {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
					.getClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFileChooser j = new JFileChooser(System.getProperty("user.home"));
		JFrame f = new JFrame();

		j.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {

				return ".torrent only";
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".torrent") || f.isDirectory()) {
					return true;
				}
				return false;
			}
		});
		int i = j.showDialog(f, "PATATE");
		System.out.println(i);
		System.out.println(j.getSelectedFile().toString());
	}
}
