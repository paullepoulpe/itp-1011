/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;

public class FenCercles extends JFrame implements MouseListener,
		MouseMotionListener, Runnable {
	LinkedList<Cercle> cercles;

	int taille;

	public FenCercles() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		cercles = new LinkedList<Cercle>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setVisible(true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Cercle c = cercles.getLast();
		c.setRayon((int) Math.round(Math.sqrt(Math
				.pow(e.getX() - c.getX(), 2.0)
				+ Math.pow(e.getY() - c.getY(), 2.0))));
		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			cercles.clear();
			repaint();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Cercle c = new Cercle(e.getX(), e.getY(), 0);
		c.start();
		cercles.addLast(c);

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(this.getWidth(), this
				.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = image.createGraphics();
		super.paint(g2);
		ListIterator<Cercle> iter = cercles.listIterator();
		while (iter.hasNext()) {
			Cercle i = iter.next();
			g2.setColor(Color.RED);
			g2.drawRect(i.getX(), i.getY(), 1, 1);
			g2.setColor(Color.BLACK);
			g2.drawOval(i.getX() - i.getRayon(), i.getY() - i.getRayon(), i
					.getRayon() * 2, i.getRayon() * 2);
			g2.setColor(i.getColor());
			g2.fillArc(i.getX() - i.getRayon(), i.getY() - i.getRayon(), i
					.getRayon() * 2, i.getRayon() * 2, 0, i.getAngle());
		}

		g.drawImage(image, 0, 0, null);

	}

	@Override
	public void run() {
		while (true) {
			repaint();
			try {
				// if (taille % 1000 == 0) {
				// Cercle c = new Cercle(this.getWidth() / 2,
				// this.getHeight() / 2, (this.getWidth() / 2)
				// - (taille / 20));
				// c.start();
				// cercles.addLast(c);
				// }
				// taille++;
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new Thread(new FenCercles()).start();
	}

}

class Cercle extends Thread {
	private int x;
	private int y;
	private int rayon;
	private int angle;
	private Color color;
	private boolean sens;
	private int sleepingTimeNanos;
	private int sleepingTimeMilis;

	public Cercle(int x, int y, int rayon) {
		this.x = x;
		this.y = y;
		this.rayon = rayon;
		this.angle = 0;
		this.color = new Color((int) (Math.random() * (1 << 24)));
		this.sleepingTimeNanos = 999999;
		sleepingTimeMilis = 30;

	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}

	public void run() {
		while (true) {
			if (!sens) {
				angle++;
			} else {
				angle--;
			}
			if (angle % 360 == 0) {
				sens = !sens;
				color = new Color((int) (Math.random() * (1 << 24)));
				sleepingTimeNanos = (int) (Math.sqrt(sleepingTimeNanos / 2)) - 5;
				if (sleepingTimeNanos < 0) {
					sleepingTimeMilis = (int) (sleepingTimeMilis / 2);
					sleepingTimeNanos = 999999;
				}
			}
			try {
				sleep(sleepingTimeMilis, sleepingTimeNanos);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRayon() {
		return rayon;
	}

	public int getAngle() {
		return angle;
	}

	public Color getColor() {
		return color;
	}
}
