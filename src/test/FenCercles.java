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
		MouseMotionListener {
	LinkedList<Cercle> cercles;

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
		c.setRayon((int) Math.round(Math.sqrt(Math.pow(e.getX() - c.x, 2.0)
				+ Math.pow(e.getY() - c.y, 2.0))));
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
			System.out.println("Double click");
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
		cercles.addLast(new Cercle(e.getX(), e.getY(), 0));

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = image.createGraphics();
		super.paint(g2);
		ListIterator<Cercle> iter = cercles.listIterator();
		while (iter.hasNext()) {
			Cercle i = iter.next();
			g2.setColor(Color.RED);
			g2.drawRect(i.x, i.y, 1, 1);
			g2.setColor(Color.BLACK);
			g2.drawOval(i.x - i.rayon, i.y - i.rayon, i.rayon * 2, i.rayon * 2);
			System.out.println("Dessine");
		}

		g.drawImage(image, 0, 0, null);

	}

	public static void main(String[] args) {
		new FenCercles();
	}

}

class Cercle {
	int x;
	int y;
	int rayon;

	public Cercle(int x, int y, int rayon) {
		this.x = x;
		this.y = y;
		this.rayon = rayon;

	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}
}
