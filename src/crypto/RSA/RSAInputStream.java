package crypto.RSA;

import java.io.*;
import java.math.BigInteger;

/**
 * Cette classe represente le canal securise a travers lequel le pair pourra
 * nous envoyer sa cle symetrique.
 * 
 * @author Damien, Maarten
 * 
 */
public class RSAInputStream extends InputStream {
	private DataInputStream in;
	private KeyPair keyPair;

	public RSAInputStream(KeyPair keypair, DataInputStream in) {
		this.keyPair = keypair;
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		byte[] bytes = new byte[keyPair.getModLength() / 8 + 1];
		if (in.available() < bytes.length) {
			return -1;
		}
		in.readFully(bytes);
		BigInteger read = new BigInteger(bytes);
		int n = keyPair.decrypt(read).intValue();
		if (n < 0 || n > 255) {
			throw new IOException();
		}
		return n;
	}

	public void close() throws IOException {
		in.close();
	}

}
