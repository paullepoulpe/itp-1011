package crypto.RSA;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * Classe nous permettant d'envoyer des donnees cryptes RSA.
 * 
 * @author Damien, Maarten
 * 
 */
public class RSAOutputStream extends OutputStream {
	private KeyPair keyPair;
	private OutputStream out;

	public RSAOutputStream(BigInteger key, OutputStream out) {
	}

	@Override
	public void write(int b) throws IOException {

	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
	}

}
