/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Graphic extends JPanel {
	private int[] points;
	private int nbPoints;
	private int max = 1;
	private int pointeur = 0;

	public Graphic(Dimension d, int nbPoints) {
		setBackground(Color.BLACK);
		setSize(d);
		this.nbPoints = nbPoints;
		points = new int[nbPoints];
	}

	@Override
	public void paint(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		super.paint(g2);
		g2.drawRect(0, 0, w - 1, h - 1);
		g2.setColor(Color.GREEN);
		int lastPoint = points[pointeur];
		for (int i = 1; i < nbPoints; i++) {
			int x1 = w * (i - 1) / (nbPoints - 1);
			int x2 = w * i / (nbPoints - 1);
			int y1 = h - (h * lastPoint / max);
			int y2 = h - (h * points[(i + pointeur) % nbPoints] / max);
			g2.drawLine(x1, y1, x2, y2);
			lastPoint = points[(i + pointeur) % nbPoints];
		}
		String s = "bytes/sec";
		switch ((int) (Math.log(max) / Math.log(2) / 10)) {
		case (0):
			s = max + " " + s;
			break;
		case (1):
			s = max / (1 << 10) + " K" + s;
			break;
		case (2):
			s = max / (1 << 20) + " M" + s;
			break;
		case (3):
			s = max / (1 << 30) + " G" + s;
			break;
		default:
			s = 0 + " " + s;

		}
		g2.drawString("Max : " + s, 5, 15);
		g.drawImage(img, 0, 0, (int) w, (int) h, null);
	}

	public void put(int value) {
		int valueDisappearing = points[pointeur];
		points[pointeur] = value;
		pointeur = (pointeur + 1) % nbPoints;
		if (value >= max) {
			max = value;
		} else if (valueDisappearing == max) {
			max = 1;
			for (int i : points) {
				if (i > max) {
					max = i;
				}
			}
		}
		repaint();
	}
}
