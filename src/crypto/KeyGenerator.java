package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import crypto.RSA.*;
import crypto.XOR.SymetricKey;

/**
 * Cette classe s'occupe de generer des cles pseudo-aleatoires pour cryptage RSA
 * et symetrique
 * 
 * @author Damien, Maarten
 * 
 */
public class KeyGenerator {

	/**
	 * Genere une paire de cle (chiffrement, dechifrement) RSA aleatoire!
	 * 
	 * @param modLength
	 *            Longueur de la cle en bits
	 * @return Une instance de {@link KeyPair} qui represente notre paire de
	 *         cles!
	 */
	public static KeyPair generateRSAKeyPair(int modLength) {
		BigInteger phi;
		BigInteger eKey;
		BigInteger mod;
		BigInteger p;
		BigInteger q;
		do {
			p = BigInteger.probablePrime(modLength / 2, new Random());
			/*
			 * Verifie que p et q sont differents pour avoir un calcul de phi
			 * correct
			 */
			do {
				q = BigInteger.probablePrime(modLength / 2, new Random());
			} while (p.equals(q));
			mod = p.multiply(q);
			phi = p.subtract(BigInteger.valueOf(1)).multiply(
					q.subtract(BigInteger.valueOf(1)));
			eKey = BigInteger.valueOf(65537);
		} while (!phi.gcd(eKey).equals(BigInteger.valueOf(1)));
		BigInteger dKey = eKey.modInverse(phi);
		return new KeyPair(mod, eKey, dKey, modLength);
	}

	/**
	 * Cette methode genere aleatoirement une cle symmetrique de longueur
	 * specifiee en argument
	 * 
	 * @param length
	 *            la longueur de la cle
	 * @return Une cle sous forme de byte[] de longueur length
	 */
	public static SymetricKey generateSymmetricKey(int length) {
		SecureRandom rd = new SecureRandom();
		byte[] key = new byte[length];
		rd.nextBytes(key);
		return new SymetricKey(key);
	}
}
