package crypto.RSA;

import java.io.IOException;
import java.io.InputStream;
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
	public RSAInputStream(BigInteger key, InputStream in) {
	}

	@Override
	public int read() throws IOException {
		return 0;
	}
	@Override
	public int read(byte[] arg0) throws IOException {
		return super.read(arg0);
	}

}
