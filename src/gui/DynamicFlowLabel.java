/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DynamicFlowLabel extends JLabel implements Runnable {
	private long instantAmount = 0;
	private long[] lastAmounts = new long[4];
	private int indexFlow = 0;
	private boolean finished = false;
	private long lastTimeUpdate = System.currentTimeMillis();
	private Graphic graph = new Graphic(new Dimension(400, 400), 100);

	public DynamicFlowLabel() {
		try {
			if (System.getProperty("os.name").equals("Linux")) {
				UIManager
						.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1]
								.getClassName());
			} else {
				UIManager
						.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3]
								.getClassName());
			}
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Thread(this).start();
	}

	public void add(long amount) {
		instantAmount += amount;
	}

	@Override
	public void run() {
		while (!finished) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastTimeUpdate > (long) 500) {
				long timeElapsed = currentTime - lastTimeUpdate;
				lastTimeUpdate = currentTime;
				int n = 0;
				lastAmounts[indexFlow] = instantAmount * 1000 / timeElapsed;
				instantAmount = 0;
				indexFlow = (indexFlow + 1) % lastAmounts.length;
				for (int i = 0; i < lastAmounts.length; i++) {
					n += lastAmounts[i];
				}
				n /= lastAmounts.length;
				graph.put(n);
				String s = "bytes/sec";
				switch ((int) (Math.log(n) / Math.log(2) / 10)) {
				case (0):
					s = n + " " + s;
					break;
				case (1):
					s = n / (1 << 10) + " K" + s;
					break;
				case (2):
					s = n / (1 << 20) + " M" + s;
					break;
				case (3):
					s = n / (1 << 30) + " G" + s;
					break;
				default:
					s = 0 + " " + s;

				}

				this.setText(s);
				this.revalidate();
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Graphic getGraph() {
		return graph;
	}

	@Override
	protected void finalize() throws Throwable {
		finished = true;
		super.finalize();
	}
}
