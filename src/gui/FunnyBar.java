/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class FunnyBar extends JPanel {
	private boolean[] barresDessinees;
	private int nbBarres;

	public FunnyBar(int nbBarres) {
		this.nbBarres = nbBarres;
		barresDessinees = new boolean[nbBarres];
	}

	public void add(int elementIndex) throws IndexOutOfBoundsException {
		if (elementIndex < nbBarres && elementIndex > -1) {
			barresDessinees[elementIndex] = true;
			repaint();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public void paint(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		double intervalle = (double) w / (double) nbBarres;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		super.paint(g2);
		g2.drawRect(0, 0, w, h);
		g2.setColor(Color.CYAN);
		for (int i = 0; i < nbBarres; i++) {
			if (barresDessinees[i]) {
				g2.fillRect((int) Math.ceil(i * intervalle), 1,
						(int) Math.ceil(intervalle), h - 1);
			}
		}
		g.drawImage(img, 0, 0, w, h, null);
	}
}
