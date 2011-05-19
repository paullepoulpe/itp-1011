package crypto.XOR;

import java.io.*;

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
		int n = ((b & 0xff) ^ (XORKey.getNext() & 0xff));
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
