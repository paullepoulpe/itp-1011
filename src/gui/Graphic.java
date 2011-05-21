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
		g2.setColor(Color.WHITE);
		String s = "bytes/sec";
		String m = "b/s";
		String debit = s;
		int deb = points[(pointeur + nbPoints - 1) % nbPoints];
		int haut = 0;
		switch ((int) (Math.log(max) / Math.log(2) / 10)) {
		case (0):
			s = max + " " + s;
			debit = deb + " " + debit;
			haut = (1 << 10);
			break;
		case (1):
			s = max / (1 << 10) + " K" + s;
			debit = deb / (1 << 10) + " K" + debit;
			haut = (1 << 20);
			m = "K" + m;
			break;
		case (2):
			s = max / (1 << 20) + " M" + s;
			debit = deb / (1 << 20) + " K" + debit;
			haut = (1 << 30);
			m = "M" + m;
			break;
		case (3):
			s = max / (1 << 30) + " G" + s;
			debit = deb / (1 << 30) + " K" + debit;
			haut = (1 << 40);
			m = "G" + m;
			break;
		default:
			s = 0 + " " + s;

		}
		g2.drawString("Max : " + s, 5, 15);
		if (haut != 0) {
			g2.drawString("500 " + m, 5,
					(int) (h - (h * haut * 4.0 / (max * 10.0))));
			g2.drawString("250 " + m, 5,
					(int) (h - (h * haut * 4.0 / (max * 20.0))));
			g2.drawString(debit, w - (debit.length() * 6), 15);
			g2.setColor(Color.GRAY);
			g2.drawLine(0, (int) (h - (h * haut * 4.0 / (max * 10.0))), w,
					(int) (h - (h * haut * 4.0 / (max * 10.0))));
			g2.drawLine(0, (int) (h - (h * haut * 4.0 / (max * 20.0))), w,
					(int) (h - (h * haut * 4.0 / (max * 20.0))));
		}
		g2.setColor(Color.GREEN);
		int lastPoint = points[pointeur];
		for (int i = 1; i < nbPoints; i++) {
			int x1 = (int) (w * (i - 1.0) / (nbPoints - 1.0));
			int x2 = (int) (w * i / (nbPoints - 1.0));
			int y1 = (int) (h - (h * lastPoint * 4.0 / (max * 5.0)));
			int y2 = (int) (h - (h * points[(i + pointeur) % nbPoints] * 4.0 / (max * 5.0)));
			g2.drawLine(x1, y1, x2, y2);
			lastPoint = points[(i + pointeur) % nbPoints];
		}

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
