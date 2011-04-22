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
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

public class FenMarrante extends JFrame implements Runnable, MouseListener,
		MouseMotionListener {
	LinkedList<Fourmi> fourmis;
	int posX;
	int posY;

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
		this.addMouseMotionListener(this);
	}

	@Override
	public void run() {
		repaint();
		Graphics g = this.getGraphics();

		while (true) {
			synchronized (fourmis) {
				Iterator<Fourmi> i = fourmis.iterator();
				while (i.hasNext()) {
					Fourmi f = i.next();
					g.setColor(f.getCouleur());
					g.drawRect(f.getX(), f.getY(), 1, 1);
					f.changePosition(posX, posY);
				}
			}
			Thread.yield();
			try {
				Thread.sleep(0, 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		posX = e.getX();
		posY = e.getY();

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

	public void changePosition(int posX, int posY) {
		int coteX = 1;
		int coteY = 1;
		if (Math.abs(posX - x) >= d.width / 2) {
			coteX = -1;
		}
		if (Math.abs(posY - y) >= d.height / 2) {
			coteY = -1;
		}

		int corrX = (int) (Math.random() * (Math.random() * 1.5 * coteX * Math
				.signum(posX - x)));
		int corrY = (int) (Math.random() * (Math.random() * 1.5 * coteY * Math
				.signum(posY - y)));
		x = (x + (int) (Math.random() * 3) - 1 + corrX + d.width) % d.width;
		y = (y + (int) (Math.random() * 3) - 1 + corrY + d.height) % d.height;
		this.couleur = new Color((couleur.getRGB() + 7) % (1 << 24));
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
