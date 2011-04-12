package torrent.messages;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ce message est un Thread qui - une fois demarre - envoie un tableau de 4
 * bytes nuls pour garder la connexion au pair active.
 * 
 * @author Damien Engels & Maarten Sap
 */
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
					}
					lastsent = System.currentTimeMillis();
				}
				yield();
				sleep(1000);
			} catch (InterruptedException e) {
				this.interrupt();
			} catch (IOException e) {
				this.interrupt();
			}
		}

	}

}
