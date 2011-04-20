/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class BarreCool extends JFrame implements Runnable {
	private boolean[] barres;
	private double rotation;

	public static void main(String[] args) {
		new Thread(new BarreCool()).run();
	}

	public BarreCool() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) d.getWidth(), (int) d.getHeight() - 40);
		this.setVisible(true);
		this.barres = new boolean[1000];
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2 = image.createGraphics();
		ImageIcon icon = new ImageIcon("blabla.jpg");
		super.paint(g2);
		g2.translate(20, this.getHeight() / 2 - 20);
		g2.rotate(rotation, (this.getWidth() - 40) / 2, 20);
		g.setColor(Color.BLACK);
		g2.drawRect(0, 0, this.getWidth() - 40, 40);
		g2.setColor(Color.CYAN.darker().darker());
		for (int i = 0; i < barres.length; i++) {
			if (barres[i]) {
				g2.fillRect(
						(int) Math.round((double) i / barres.length
								* (this.getWidth() - 41)) + 1,
						1,
						(int) Math.round(1.0 / barres.length
								* (this.getWidth() - 42)), 39);
			}
		}
		g2.drawImage(icon.getImage(), 0, 0, null);
		g.drawImage(image, 0, 0, null);

	}

	public void run() {
		while (true) {
			barres[(int) (Math.random() * barres.length)] = true;
			this.rotation = (rotation + 2 * Math.PI / 100) % (2 * Math.PI);
			repaint();
			try {
				Thread.sleep(1000 / 25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
