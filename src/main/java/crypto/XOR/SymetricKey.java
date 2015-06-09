package crypto.XOR;

/**
 * Cette classe symbolise une cle de cryptage symetrique. Elle encapsule le
 * byte[] representant la cle
 * 
 * @author Damien, Maarten
 * 
 */
public class SymetricKey {
	private byte[] cle;
	private int index;

	public SymetricKey(byte[] cle) {
		this.cle = cle.clone();
		index = -1;
	}

	/**
	 * Permet de parcourir le tableau de bytes
	 * 
	 * @return le byte en cours
	 */
	public byte getNext() {
		index = (index + 1) % cle.length;
		return cle[index];
	}

	public byte[] getBytes() {
		return cle;
	}
}
