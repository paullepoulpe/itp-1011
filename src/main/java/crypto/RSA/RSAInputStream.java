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
	private int encryptedByteLentgh;

	public RSAInputStream(KeyPair keypair, DataInputStream in) {
		this.keyPair = keypair;
		this.in = in;
		encryptedByteLentgh = keyPair.getModLength() / 8 + 1;
		System.out.println("Nouveau RSAInputStream!");
	}

	@Override
	public int read() throws IOException {
		// System.out.println("Je Lis dans le rsaStream");
		byte[] bytes = new byte[encryptedByteLentgh];
		// System.out.println("Taille du tableau a lire: " + bytes.length);
		while (available() <= 0) {
		}
		in.readFully(bytes);
		// System.out.println("Tableau lu : " + Arrays.toString(bytes));

		BigInteger read = new BigInteger(bytes);
		int n = keyPair.decrypt(read).intValue();
		if (n < 0 || n > 255) {
			System.err.println("InvalidByteRead");
		}
		// System.out.println("Lu : " + n);
		return n & 0xff;
	}

	@Override
	public int available() throws IOException {
		// System.out.println(in.available());
		return in.available() / encryptedByteLentgh;
	}

	@Override
	public void close() throws IOException {
		in.close();
		super.close();
	}

}
