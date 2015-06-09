/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package testGUI;

import java.util.LinkedList;

import gui.Graphic;

import javax.swing.JFrame;

public class TestGraphics {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(400, 400);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Graphic graph = new Graphic(f.getSize(), 100);
		f.add(graph);
		LinkedList<Integer> values = new LinkedList<Integer>();
		values.add(2);
		values.add(2);
		values.add(2);
		values.add(2);
		values.add(2);
		values.add(2);
		values.add(2);
		values.add(2);
		int i = 0;
		while (true) {
			values.removeLast();
			values.addFirst((int) (Math.random() * i) + 10000);
			int moyenne = (values.get(0) + values.get(1) + values.get(2)
					+ values.get(3) + values.get(4) + values.get(5)
					+ values.get(6) + values.get(7)) / 8;
			i = (i + 12345) % (1 << 24);
			graph.put(moyenne);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
