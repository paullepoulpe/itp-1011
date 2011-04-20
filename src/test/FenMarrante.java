/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class FenMarrante extends JFrame implements Runnable {
	public static void main(String[] args) {
		new Thread(new FenMarrante()).start();
	}

	public FenMarrante() {

	}

	@Override
	public void run() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, (int) d.getWidth(), (int) d.getHeight());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		Graphics g = this.getGraphics();
		int Width = (int) d.getWidth();
		int Height = (int) d.getHeight();
		int[] X = new int[10000];
		X[0] = Width / 2;
		int[] Y = new int[10000];
		Y[0] = Height / 2;
		int index = 0;

		while (true) {
			int x = X[index];
			int y = Y[index];
			g.fillRect(X[index], Y[index], 1, 1);
			index = (index + 1) % 10000;
			g.clearRect(X[index], Y[index], 1, 1);
			X[index] = (x + (int) (Math.random() * 2.9) - 1 + Width) % Width;
			Y[index] = (y + (int) (Math.random() * 3) - 1 + Height) % Height;
			try {
				Thread.sleep(0, 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
