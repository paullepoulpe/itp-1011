package crypto.RSA;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {
	private BigInteger mod, eKey, dKey;
	private int kLength;

	public KeyPair(BigInteger mod, BigInteger eKey, BigInteger dKey, int kLength) {
		if (kLength % 8 != 0) {
			throw new IllegalArgumentException();
		} else {
			this.mod = mod;
			this.eKey = eKey;
			this.dKey = dKey;
			this.kLength = kLength;
		}
	}

	/**
	 * Chiffre un message!
	 * 
	 * @param message
	 *            Le message a chiffrer
	 * @return Le cryptogramme correspondant
	 */
	public BigInteger encrypt(BigInteger message) {
		return message.modPow(eKey, mod);

	}

	/**
	 * Dechiffre un message!
	 * 
	 * @param cryptogramm
	 *            Le cryptogramme a dechiffrer
	 * @return Le message en clair correspondant
	 */
	public BigInteger decrypt(BigInteger cryptogramm) {
		return cryptogramm.modPow(dKey, mod);
	}

}
