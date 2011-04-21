package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class FenDrole extends JFrame {
	public static void main(String[] args) {
		new FenDrole();
	}

	public FenDrole() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 256; j++) {
				g.setColor(new Color((j << 16) + (i << 8)));
				g.drawRect((int) Math.round((i / 256.0) * this.getWidth()),
						(int) Math.round((j / 256.0) * this.getHeight()),
						(int) Math.ceil((5 / 256.0) * this.getWidth()),
						(int) Math.ceil((5 / 256.0) * this.getWidth()));

			}
		}

		// for (int i = 0; i < (1 << 24); i++) {
		// g.setColor(new Color(i));
		// int x = i % this.getWidth();
		// int y = ((i - x) / (this.getWidth())) % this.getHeight();
		// g.drawRect(x, y, 1, 1);
		// }
	}
}
