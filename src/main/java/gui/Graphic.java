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

/**
 * Cette classe est un {@link JPanel} qui dessine un graphique en se faisant
 * donner des points a dessiner.
 * 
 * @author Damien, Maarten
 * 
 */
public class Graphic extends JPanel {
	private long[] points;
	private int nbPoints;
	private long max = 1;
	private int pointeur = 0;

	public Graphic(Dimension d, int nbPoints) {
		setBackground(Color.BLACK);
		setSize(d);
		this.nbPoints = nbPoints;
		points = new long[nbPoints];
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
		String debit = s;
		long deb = points[(pointeur + nbPoints - 1) % nbPoints];
		switch ((int) (Math.log(max) / Math.log(2) / 10.0)) {
		case (0):
			s = max + " " + s;
			debit = deb + " " + debit;
			break;
		case (1):
			s = max / (1 << 10) + " K" + s;
			debit = deb / (1 << 10) + " K" + debit;
			break;
		case (2):
			s = max / (1 << 20) + " M" + s;
			debit = deb / (1 << 20) + " M" + debit;
			break;
		case (3):
			s = max / (1 << 30) + " G" + s;
			debit = deb / (1 << 30) + " G" + debit;
			break;
		default:
			s = 0 + " " + s;
			debit = 0 + " " + debit;

		}

		int haut = (int) Math.pow(2.0, (int) (Math.log((double) max) / Math
				.log(2.0)));
		String controle = "b/s";
		int hautAffiche = 0;

		switch ((int) (Math.log(haut * 3.0 / 2.0) / Math.log(2.0))) {
		case (14):
			hautAffiche = 16;
			controle = "K" + controle;
			break;
		case (15):
			hautAffiche = 32;
			controle = "K" + controle;
			break;
		case (16):
			hautAffiche = 64;
			controle = "K" + controle;
			break;
		case (17):
			hautAffiche = 128;
			controle = "K" + controle;
			break;
		case (18):
			hautAffiche = 256;
			controle = "K" + controle;
			break;
		case (19):
			hautAffiche = 512;
			controle = "K" + controle;
			break;
		case (20):
			hautAffiche = 1024;
			controle = "K" + controle;
			break;
		case (21):
			hautAffiche = 2;
			controle = "M" + controle;
			break;
		case (22):
			hautAffiche = 4;
			controle = "M" + controle;
			break;
		case (23):
			hautAffiche = 8;
			controle = "M" + controle;
			break;
		case (24):
			hautAffiche = 16;
			controle = "M" + controle;
			break;
		default:
		}

		g2.drawString("Max : " + s, 5, 15);
		g2.drawString(debit, w - (debit.length() * 6), 15);
		g2.drawString(hautAffiche + controle, 5,
				(int) (h - (h * haut * 4.0 / (max * 5.0))));
		g2.drawString(hautAffiche / 2 + controle, 5,
				(int) (h - (h * haut * 4.0 / (max * 10.0))));
		g2.setColor(Color.GRAY);
		g2.drawLine(0, (int) (h - (h * haut * 4.0 / (max * 5.0))), w,
				(int) (h - (h * haut * 4.0 / (max * 5.0))));
		g2.drawLine(0, (int) (h - (h * haut * 4.0 / (max * 10.0))), w,
				(int) (h - (h * haut * 4.0 / (max * 10.0))));

		g2.setColor(Color.GREEN);
		long lastPoint = points[pointeur];
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

	public void put(long l) {
		long valueDisappearing = points[pointeur];
		points[pointeur] = l;
		pointeur = (pointeur + 1) % nbPoints;
		if (l >= max) {
			max = l;
		} else if (valueDisappearing == max) {
			max = 1;
			for (long i : points) {
				if (i > max) {
					max = i;
				}
			}
		}
		repaint();
	}
}
