package crypto.RSA;

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
	private byte[] XORKey;
	private DataInputStream in;

	public SymmetricInputStream(byte[] key, DataInputStream in) {
		this.XORKey = key;
		this.in=in;
	}

	@Override
	public int read() throws IOException {
		int messageLength = in.readInt();
		int read = in.read();
		int key = (XORKey[3]<<12)+(XORKey[2]<<8)+(XORKey[1]<<4)+XORKey[0];
		int decrypt = read^key;
		return decrypt;
	}
}
