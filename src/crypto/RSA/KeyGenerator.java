package crypto.RSA;

import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {

	/**
	 * Genere une paire de cle (chiffrement, dechifrement) RSA aleatoire!
	 * 
	 * @param kLength
	 *            Longueur de la cle en bits
	 * @return Une instance de {@link KeyPair} qui represente notre paire de
	 *         cles!
	 */
	public static KeyPair generateRSAKeyPair(int kLength) {
		BigInteger phi;
		BigInteger eKey;
		BigInteger mod;
		BigInteger p;
		BigInteger q;
		do {
			p = BigInteger.probablePrime(kLength / 2, new Random());
			/*
			 * Verifie que p et q sont differents pour avoir un calcul de phi
			 * correct
			 */
			do {
				q = BigInteger.probablePrime(kLength / 2, new Random());
			} while (p.equals(q));
			mod = p.multiply(q);
			phi = p.subtract(BigInteger.valueOf(1)).multiply(
					q.subtract(BigInteger.valueOf(1)));
			eKey = BigInteger.valueOf(65537);
		} while (!phi.gcd(eKey).equals(BigInteger.valueOf(1)));
		BigInteger dKey = eKey.modInverse(phi);
		return new KeyPair(mod, eKey, dKey, kLength);
	}
}
