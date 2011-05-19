/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package testGUI;

import gui.FunnyBar;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFunnyBar {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(600, 100);
		FunnyBar fb = new FunnyBar(20, f.getSize(), f);
		f.setVisible(true);
		f.add(fb);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < 20; i++) {
			fb.add(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
