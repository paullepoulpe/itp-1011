/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Hypnotic extends JFrame implements Runnable {
	private double rotation;

	public static void main(String[] args) {
		new Thread(new Hypnotic()).start();
	}

	public Hypnotic() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = image.createGraphics();
		super.paint(g2);
		g2.translate(this.getWidth() / 2, this.getHeight() / 2);
		g2.rotate(rotation);
		g2.setColor(Color.BLACK);
		for (double i = 0; i < 10000; i = i + 1) {
			int x = (int) Math.round(i / Math.log(i) * 0.5
					* Math.cos(i / 100.0));
			int y = (int) Math.round(i / Math.log(i) * 0.5
					* Math.sin(i / 100.0));
			g2.fillRect(x, y, 1, 1);
		}
		g2.rotate(rotation * 2);
		g2.setColor(Color.BLUE);
		for (int i = 0; i < 10000; i++) {
			int x = (int) Math.round(i / Math.log(i) * 0.5
					* Math.cos(i / 100.0));
			int y = (int) Math.round(i / Math.log(i) * 0.5
					* Math.sin(i / 100.0));
			g2.fillRect(x, y, 1, 1);
		}
		g2.rotate(rotation * 3);
		g2.setColor(Color.RED);
		for (int i = 0; i < 10000; i++) {
			int x = (int) Math.round(i / Math.log(i) * 0.5
					* Math.cos(i / 100.0));
			int y = (int) Math.round(i / Math.log(i) * 0.5
					* Math.sin(i / 100.0));
			g2.fillRect(x, y, 1, 1);
		}
		g2.rotate(rotation * 4);
		g2.setColor(Color.CYAN);
		for (int i = 0; i < 10000; i++) {
			int x = (int) Math.round(i / Math.log(i) * 0.5
					* Math.cos(i / 100.0));
			int y = (int) Math.round(i / Math.log(i) * 0.5
					* Math.sin(i / 100.0));
			g2.fillRect(x, y, 1, 1);
		}
		g.drawImage(image, 0, 0, null);

		g.drawImage(image, 0, 0, null);

	}

	@Override
	public void run() {
		while (true) {
			rotation = (rotation - 2 * Math.PI / 100) % (2 * Math.PI);
			repaint();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
