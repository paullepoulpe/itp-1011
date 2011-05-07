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
	private InputStream in;
	private KeyPair keyPair;

	public RSAInputStream(KeyPair keypair , InputStream in) {
		this.keyPair = keypair;
		this.in=in;
	}

	@Override
	public int read() throws IOException {
		BigInteger read = BigInteger.valueOf((in.read())&255);
		return keyPair.decrypt(read).intValue();
	}
	public void close() throws IOException{
		in.close();
	}
}
