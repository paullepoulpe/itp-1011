package crypto.RSA;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Classe nous permettant d'envoyer des donnees cryptes RSA.
 * 
 * @author Damien, Maarten
 * 
 */
public class RSAOutputStream extends OutputStream {
	private KeyPair keyPair;
	private DataOutputStream out;
	private int encryptedByteLength;

	public RSAOutputStream(KeyPair keyPair, DataOutputStream out) {
		this.keyPair = keyPair;
		this.out = out;
		encryptedByteLength = keyPair.getModLength() / 8 + 1;
		System.out.println("Nouveau RSAOutputStream!");
	}

	@Override
	public void write(int b) throws IOException {
		// System.out.println("Ecris :");
		// System.out.println("Avant encryption : " + (b & 0xff));
		byte[] envoye = keyPair.encrypt(BigInteger.valueOf(0xff & b))
				.toByteArray();
		// System.out.println("Apres encryption : " + Arrays.toString(envoye));

		out.write(new byte[encryptedByteLength - envoye.length]);

		// System.out.println((encryptedByteLength - envoye.length)
		// + " zeros avant");
		out.write(envoye);
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
