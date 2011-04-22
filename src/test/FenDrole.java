package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class FenDrole extends JFrame {
	int couleur = 0;

	public static void main(String[] args) {
		FenDrole f = new FenDrole();

		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 256; k++) {
					f.couleur = (i << 16) + (j << 8) + k;
					f.repaint();
				}
				for (int k = 255; k >= 0; k--) {
					f.couleur = (i << 16) + (j << 8) + k;
					f.repaint();
				}
			}
			for (int j = 255; j >= 0; j--) {
				for (int k = 0; k < 256; k++) {
					f.couleur = (i << 16) + (j << 8) + k;
					f.repaint();
				}
				for (int k = 255; k >= 0; k--) {
					f.couleur = (i << 16) + (j << 8) + k;
					f.repaint();
				}
			}
		}

	}

	public FenDrole() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	@Override
	public void paint(Graphics g) {
		BufferedImage image = new BufferedImage(this.getWidth(),
				this.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = image.createGraphics();
		super.paint(g2);
		g2.setTransform(AffineTransform.getScaleInstance(this.getWidth(),
				this.getHeight()));
		g2.setColor(new Color(couleur));
		g2.fillRect(0, 0, 2, 2);
		g.drawImage(image, 0, 0, null);

		// for (int i = 0; i < (1 << 24); i++) {
		// g.setColor(new Color(i));
		// int x = i % this.getWidth();
		// int y = ((i - x) / (this.getWidth())) % this.getHeight();
		// g.drawRect(x, y, 1, 1);
		// }
	}
}
