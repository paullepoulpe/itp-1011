package torrent.messages;

import java.io.IOException;
import java.io.OutputStream;

public class KeepAlive extends Thread {
	long lastsent;
	OutputStream out;
	byte[] b;

	public KeepAlive(OutputStream out) {
		this.out = out;
		b = new byte[4];
	}

	@Override
	public void run() {
		while (true) {
			long time = System.currentTimeMillis();
			try {
				if ((time - lastsent) > 100000) {
					synchronized (out) {
						out.write(b);
						System.out.println("Sent KeepAlive");
					}
					lastsent = System.currentTimeMillis();
				}
				yield();
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
