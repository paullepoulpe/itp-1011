package crypto.RSA;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

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
		System.out.println("Je Lis dans le rsaStream");
		byte[] bytes = new byte[keyPair.getModLength() / 8 + 1];
		// if (available() <= 0) {
		// return -1;
		// }
		System.out.println("Taille du tableau a lire: " + bytes.length);
		while (available() <= 0) {
		}
		in.readFully(bytes);
		System.out.println("Tableau lu : " + Arrays.toString(bytes));

		BigInteger read = new BigInteger(bytes);
		int n = keyPair.decrypt(read).intValue();
		if (n < 0 || n > 255) {
			System.err.println("InvalidByteRead");
		}
		System.out.println("Lu : " + n);
		return n & 0xff;
	}

	@Override
	public int available() throws IOException {
		return in.available() / (keyPair.getModLength() / 8 + 1);
	}

	@Override
	public void close() throws IOException {
		in.close();
		super.close();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int n = 0;
		boolean stop = false;
		for (int i = 0; i < b.length && !stop; i++) {
			if (available() > 0) {
				b[i] = (byte) (0xff & read());
				n++;
			} else {
				stop = true;
			}
		}
		return n;
	}

}
