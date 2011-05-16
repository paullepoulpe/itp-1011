package settings;

import crypto.RSA.KeyPair;

public class PeerSettings {
	private int privateRSAModLength = -1;
	private int xorKeyLength = -1;

	public PeerSettings() {

	}

	public int getPrivateRSAModLength() {
		return privateRSAModLength;
	}
}
