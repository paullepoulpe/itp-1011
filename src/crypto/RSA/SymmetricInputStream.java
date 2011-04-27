package crypto.RSA;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Cette classe est une flux d'entree a travers lequel le pair peut nous envoyer
 * des messages cryptes symetriquement
 * 
 * @author Damien, Maarten
 * 
 */
public class SymmetricInputStream extends InputStream {
	private KeyPair keyPair;
	private InputStream in;

	public SymmetricInputStream(BigInteger key, InputStream in) {
	}

	@Override
	public int read() throws IOException {
		return 0;
	}
}
