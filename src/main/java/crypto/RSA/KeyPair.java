package crypto.RSA;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {
	private BigInteger mod, eKey, dKey;
	private int modLength;

	public KeyPair(BigInteger mod, BigInteger eKey, BigInteger dKey,
			int modLength) {
		if (modLength % 8 != 0) {
			throw new IllegalArgumentException();
		} else {
			this.mod = mod;
			this.eKey = eKey;
			this.dKey = dKey;
			this.modLength = modLength;
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

	public int getModLength() {
		return modLength;
	}

	public BigInteger getEKey() {
		return eKey;
	}

	public BigInteger getMod() {
		return mod;
	}

}
