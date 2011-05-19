package crypto.XOR;

import java.io.*;

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
		int n = in.readByte();
		int b = (n & 0xff) ^ (XORKey.getNext() & 0xff);
		return b & 0xff;

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
