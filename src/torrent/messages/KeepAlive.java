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
	boolean finished = false;
	byte[] b = new byte[4];

	public KeepAlive(OutputStream out) {
		this.out = out;
	}

	@Override
	public void run() {
		while (!finished) {
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
				finished = true;
			} catch (IOException e) {
				finished = true;
			}
		}

	}

}
