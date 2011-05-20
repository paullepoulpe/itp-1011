/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class FunnyBar extends JPanel {
	private byte[] barresDessinees;
	private int nbBarres = 0;
	private Component parent;

	public FunnyBar(int nbBarres, Dimension initialDim) {
		this(nbBarres, initialDim, null);
	}

	public FunnyBar(int nbBarres, Dimension initialDim, Component parent) {
		setBackground(Color.WHITE);
		this.nbBarres = nbBarres;
		barresDessinees = new byte[(int) Math.ceil((double) nbBarres / 8.0)];
		this.parent = parent;
	}

	public synchronized void add(int elementIndex)
			throws IndexOutOfBoundsException {
		if (elementIndex < nbBarres && elementIndex > -1) {
			barresDessinees[elementIndex / 8] |= (byte) (1 << (7 - elementIndex % 8));
			repaint();
		} else {
			System.err.println("elementIndex:" + elementIndex);
			throw new IndexOutOfBoundsException("elementIndex:" + elementIndex);
		}
	}

	@Override
	public void paint(Graphics g) {
		if (parent != null) {
			this.setSize(parent.getWidth()-10, 30);
		}
		int w = getWidth();
		int h = getHeight();
		double intervalle = (double) w / (double) nbBarres;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		super.paint(g2);
		g2.drawRect(0, 0, w - 1, h - 1);
		g2.setColor(Color.CYAN.darker());
		for (int i = 0; i < nbBarres; i++) {
			if ((barresDessinees[i / 8] & (byte) (1 << (7 - i % 8))) != 0) {
				int debut = (int) Math.ceil(i * intervalle) + 1;
				g2.fillRect(debut, 1,
						Math.min((int) Math.ceil(intervalle), w - debut - 1),
						h - 2);
			}
		}
		g.drawImage(img, 0, 0, w, h, null);
	}

	public void removeAll() {
		barresDessinees = new byte[(int) Math.ceil((double) nbBarres / 8.0)];
		repaint();
	}

	public void setParent(Component parent) {
		this.parent = parent;
	}

}
