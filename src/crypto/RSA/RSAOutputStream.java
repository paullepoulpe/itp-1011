package crypto.RSA;

import java.io.DataOutputStream;
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

	@Override
	public void write(byte[] b) throws IOException {
	}

}
