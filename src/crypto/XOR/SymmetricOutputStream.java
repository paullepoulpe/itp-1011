package crypto.XOR;

import java.io.*;
import java.math.BigInteger;

/**
 * Cette classe est un flux de sortie qui nous permet d'envoyer des messages
 * cryptes symetriquement a notre pair.
 * 
 * @author Damien, Maarten
 * 
 */
public class SymmetricOutputStream extends OutputStream {
	private SymetricKey XORKey;
	private DataOutputStream out;

	public SymmetricOutputStream(SymetricKey key, DataOutputStream out) {
		System.out.println("Nouveau XOROutputStream!");
		this.out = out;
		this.XORKey = key;
	}

	@Override
	public void write(int b) throws IOException {
		System.out.println("J'ecris dans le symStream");
		byte n = (byte) ((b & 0xff) ^ XORKey.getNext());
		System.out.println("Ecris : " + b);
		out.writeByte(n);
	}

	@Override
	public void flush() throws IOException {
		out.flush();
		super.flush();
	}

	@Override
	public void close() throws IOException {
		out.close();
		super.close();
	}
}
