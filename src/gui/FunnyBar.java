/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class FunnyBar extends JPanel {
	private boolean[] barresDessinees;
	private int nbBarres;
	private Component parent = null;

	public FunnyBar(int nbBarres) {
		setBackground(Color.WHITE);
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
		// this.setSize(parent.getWidth() - 80, 60);
		setSize(50, 20);
		int w = getWidth();
		int h = getHeight();
		double intervalle = (double) w / (double) nbBarres;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		super.paint(g2);
		g2.drawRect(0, 0, w - 1, h - 1);
		g2.setColor(Color.CYAN.darker().darker());
		for (int i = 0; i < nbBarres; i++) {
			if (barresDessinees[i]) {
				int debut = (int) Math.ceil(i * intervalle) + 1;
				g2.fillRect(debut, 1, Math.min((int) Math.ceil(intervalle), w
						- debut - 1), h - 2);
			}
		}
		g.drawString("Test", 30, 10);
		g.drawImage(img, 0, 0, w, h, null);
	}

	public void removeAll() {
		barresDessinees = new boolean[nbBarres];
		repaint();
	}

	public void setParent(Component parent) {
		this.parent = parent;
	}

}
