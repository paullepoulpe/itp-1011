package crypto.RSA;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * Cette classe est un flux de sortie qui nous permet d'envoyer des messages
 * cryptes symetriquement a notre pair.
 * 
 * @author Damien, Maarten
 * 
 */
public class SymmetricOutputStream extends OutputStream {
	private KeyPair keyPair;
	private OutputStream out;

	public SymmetricOutputStream(BigInteger key, OutputStream out) {
	}

	@Override
	public void write(int b) throws IOException {
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
	}
}
