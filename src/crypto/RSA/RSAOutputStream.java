package crypto.RSA;

import java.io.*;
import java.math.BigInteger;

/**
 * Classe nous permettant d'envoyer des donnees cryptes RSA.
 * 
 * @author Damien, Maarten
 * 
 */
public class RSAOutputStream extends OutputStream {
	private KeyPair keyPair;
	private BigInteger mod, eKey;
	private DataOutputStream out;

	public RSAOutputStream(KeyPair keyPair, DataOutputStream out) {
		this.keyPair = keyPair;
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(keyPair.encrypt(BigInteger.valueOf(b)).intValue());
	}

	public void flush() throws IOException {
		out.flush();
		super.flush();
	}

	public void close() throws IOException {
		out.close();
		super.close();
	}
}
