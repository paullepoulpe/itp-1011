/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import javax.swing.JLabel;

public class DynamicFlowLabel extends JLabel implements Runnable {
	private int instantAmount = 0;
	private int[] lastAmounts = new int[4];
	private int indexFlow = 0;
	private boolean finished = false;

	public DynamicFlowLabel() {
		new Thread(this).start();
	}

	public void add(int amount) {
		instantAmount += amount;
	}

	@Override
	public void run() {
		while (!finished) {
			int n = 0;
			lastAmounts[indexFlow] = instantAmount;
			instantAmount = 0;
			indexFlow = (indexFlow + 1) % lastAmounts.length;
			for (int i = 0; i < lastAmounts.length; i++) {
				n += lastAmounts[i];
			}
			n /= lastAmounts.length;
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
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		finished = true;
		super.finalize();
	}
}
