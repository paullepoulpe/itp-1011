package settings;

import crypto.RSA.KeyPair;

public class PeerSettings {
	private int privateRSAModLength = -1;
	private int xorKeyLength = -1;

	public PeerSettings() {
		privateRSAModLength = 128;
		xorKeyLength = 128;
	}

	public int getPrivateRSAModLength() {
		return privateRSAModLength;
	}

	public int getXorKeyLength() {
		return xorKeyLength;
	}
}
