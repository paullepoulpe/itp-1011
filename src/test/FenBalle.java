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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

public class FenBalle extends JFrame implements Runnable, MouseListener,
		MouseMotionListener {
	LinkedList<Balle> baballes;

	public static void main(String[] args) {
		new Thread(new FenBalle()).start();
	}

	public FenBalle() {
		super("Baballe!!!");
		this.setSize(300, 400);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		Balle baballe = new Balle(new Vecteur(0, 0), new Vecteur(7, 7),
				Color.RED, 20);
		baballe.UpdateDim(this.getSize());
		baballes = new LinkedList<Balle>();
		baballes.addLast(baballe);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void run() {
		while (true) {
			synchronized (baballes) {

				Iterator<Balle> iter = baballes.iterator();
				while (iter.hasNext()) {
					Balle baballe = iter.next();
					baballe.updatePos();
					baballe.UpdateDim(this.getSize());
				}
			}
			repaint();
			Thread.yield();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		super.paint(g2);
		synchronized (baballes) {

			Iterator<Balle> iter = baballes.iterator();
			while (iter.hasNext()) {
				Balle baballe = iter.next();
				baballe.UpdateDim(this.getSize());
				g2.setColor(baballe.getCouleur());
				g2.fillOval(
						(int) baballe.getPosition().getX() - baballe.getRayon(),
						(int) baballe.getPosition().getY() - baballe.getRayon(),
						baballe.getRayon() * 2, baballe.getRayon() * 2);
			}
		}
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		synchronized (baballes) {
			Balle baballe = new Balle(new Vecteur(e.getX(), e.getY()),
					Vecteur.generateAlea(), Color.RED, 20);
			baballe.UpdateDim(getSize());
			baballes.addLast(baballe);

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

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		synchronized (baballes) {
			Balle baballe = new Balle(new Vecteur(e.getX(), e.getY()),
					Vecteur.generateAlea(), Color.RED, 20);
			baballe.UpdateDim(getSize());
			baballes.addLast(baballe);

		}

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}

class Balle {
	private Vecteur position;
	private Vecteur vitesse;
	private Color couleur;
	private int rayon;
	private Dimension d;

	public Balle(Vecteur position, Vecteur vitesse, Color couleur, int rayon) {
		this.position = position;
		this.vitesse = vitesse;
		this.rayon = rayon;
		this.couleur = couleur;
	}

	public void UpdateDim(Dimension d) {
		this.d = d;
	}

	public void updatePos() {
		position.add(vitesse);
		if (this.position.getX() + this.rayon > d.width) {
			position.setX(2 * d.width - 2 * this.rayon - this.position.getX());
			this.vitesse.setX(-this.vitesse.getX() * 0.99);
		} else if (this.position.getX() - this.rayon < 0) {
			position.setX(2 * rayon - this.position.getX());
			this.vitesse.setX(-this.vitesse.getX() * 0.99);
		}

		if (this.position.getY() + this.rayon > d.height) {
			position.setY(2 * d.height - 2 * this.rayon - this.position.getY());
			this.vitesse.setY(-this.vitesse.getY() * 0.9);
		} else if (this.position.getY() - this.rayon < 0) {
			position.setY(3 * rayon - this.position.getY());
			this.vitesse.setY(-this.vitesse.getY());
		}
		this.vitesse.setX(this.vitesse.getX() * 0.999);
		vitesse.add(new Vecteur(0, 2));

	}

	public Vecteur getPosition() {
		return position;
	}

	public int getRayon() {
		return rayon;
	}

	public Color getCouleur() {
		return couleur;
	}
}

class Vecteur {
	private double x;
	private double y;

	public Vecteur(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void add(Vecteur v) {
		this.x += v.x;
		this.y += v.y;
	}

	static Vecteur generateAlea() {
		double x = Math.random() * 4 + 4;
		double y = Math.random() * 4 + 4;
		x *= Math.signum(Math.random() - 0.5);
		y *= Math.signum(Math.random() - 0.5);
		return new Vecteur(x, y);
	}

	public boolean isNegli() {
		return (y < 0.1);
	}
}
