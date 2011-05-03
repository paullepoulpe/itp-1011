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

	public RSAInputStream(InputStream in) {
		this.in=in;
		
	}

	@Override
	public int read() throws IOException {
		return 0;
	}

}
