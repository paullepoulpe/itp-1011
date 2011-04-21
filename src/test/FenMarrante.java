/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */
package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

public class FenMarrante extends JFrame implements Runnable, MouseListener {
	LinkedList<Fourmi> fourmis;

	public static void main(String[] args) {
		new Thread(new FenMarrante()).start();
	}

	public FenMarrante() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, (int) d.getWidth(), (int) d.getHeight());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.fourmis = new LinkedList<Fourmi>();
		this.addMouseListener(this);
	}

	@Override
	public void run() {

		Graphics g = this.getGraphics();

		while (true) {
			synchronized (fourmis) {
				Iterator<Fourmi> i = fourmis.iterator();
				while (i.hasNext()) {
					Fourmi f = i.next();
					g.setColor(f.getCouleur());
					g.drawRect(f.getX(), f.getY(), 1, 1);
					f.changePosition();
				}
			}

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Fourmi f = new Fourmi(e.getX(), e.getY(), this.getSize());
		f.start();
		synchronized (fourmis) {
			fourmis.addLast(f);
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g) {
		Iterator<Fourmi> i = fourmis.iterator();
		while (i.hasNext()) {
			i.next().setD(this.getSize());
		}
	}
}

class Fourmi extends Thread {
	private Color couleur;
	private int x;
	private int y;
	private Dimension d;

	public Fourmi(int x, int y, Dimension d) {
		this.x = x;
		this.y = y;
		this.couleur = new Color((int) (Math.random() * (1 << 24)));
		this.d = d;
	}

	public void changePosition() {
		x = (x + (int) (Math.random() * 3) - 1 + d.width) % d.width;
		y = (y + (int) (Math.random() * 3) - 1 + d.height) % d.height;
		this.couleur = new Color((couleur.getRGB() + 1) % (1 << 24));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public void run() {
		// while (true) {
		// changePosition();
		// try {
		// sleep(0, 1);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

	}

	public void setD(Dimension d) {
		this.d = d;
	}

	public Color getCouleur() {
		return couleur;
	}
}
