package test;

import torrent.messages.*;

public class BitfieldTest {
	public static void main(String[] args) {
		byte[] bob = new byte[2];
		bob[0] = 1;
		bob[1] = -2;
		BitField myBF = new BitField(bob);
		boolean[] alice = myBF.getPosessedPieces();
		for (int i = 0; i < alice.length; i++) {
			System.out.println(alice[i]);
		}
	}
}
