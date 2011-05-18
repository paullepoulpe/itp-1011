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
	private byte[] XORKey;
	private DataOutputStream out;

	public SymmetricOutputStream(byte[] key, DataOutputStream out) {
		this.out = out;
		this.XORKey = key;
	}

	@Override
	public void write(int b) throws IOException {

	}
}
