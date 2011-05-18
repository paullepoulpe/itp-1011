package crypto.XOR;

public class SymetricKey {
	private byte[] cle;
	private int index;

	public SymetricKey(byte[] cle) {
		this.cle = cle.clone();
		index = -1;
	}

	public byte getNext() {
		index = (index + 1) % cle.length;
		return cle[index];
	}

	public byte[] getBytes() {
		return cle;
	}
}
