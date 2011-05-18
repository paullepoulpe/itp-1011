package crypto.XOR;

import java.io.*;
import java.math.BigInteger;

/**
 * Cette classe est une flux d'entree a travers lequel le pair peut nous envoyer
 * des messages cryptes symetriquement
 * 
 * @author Damien, Maarten
 * 
 */
public class SymmetricInputStream extends InputStream {
	private SymetricKey XORKey;
	private DataInputStream in;

	public SymmetricInputStream(SymetricKey key, DataInputStream in) {
		System.out.println("Nouveau XORInputStream!");
		this.XORKey = key;
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		System.out.println("Je lis dans le symStream");
		int n = in.read();
		if (n < 0 || n > 255) {
			return -1;
		} else {
			int b = (n & 0xff) ^ XORKey.getNext();
			System.out.println("Lu : " + b);
			return b;

		}
	}

	@Override
	public void close() throws IOException {
		in.close();
		super.close();
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}
}
