package torrent.piece;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Piece {
	private byte[] data;
	private boolean[] receipt;
	private int sizeTab;
	private byte[] hash;
	private int index;
	private int nbBlocs;
	static public int BLOCK_SIZE = 1 << 14;

	/**
	 * Constructeur
	 * 
	 * @param index
	 *            Index de la piece
	 * @param sizeTab
	 *            Taille de la piece en bytes
	 * @param hash
	 *            Somme de contrôle SHA-1
	 */
	public Piece(int index, int sizeTab, byte[] hash) {
		/*
		 * if (sizeTab % BLOCK_SIZE != 0) { throw new
		 * IllegalArgumentException(); }
		 */
		this.index = index;
		this.sizeTab = sizeTab;
		this.hash = hash;
		this.data = new byte[sizeTab];
		this.nbBlocs = (int) Math.ceil((double) sizeTab / (double) BLOCK_SIZE);
		this.receipt = new boolean[nbBlocs];
	}

	/**
	 * Retourne les donnees si la piece est complete et null sinon
	 * 
	 * @return Tableau de bytes representant les donnees
	 */
	public byte[] getData() {
		if (this.check()) {
			return data;
		} else {
			return null;
		}

	}

	/**
	 * Calcule le pourcentage de completion de la Piece
	 * 
	 * @return Un entier qui correspond au pourcentage de completion
	 */
	public int getDownloadCompleteness() {
		int received = 0;
		for (int i = 0; i < nbBlocs; i++) {
			if (receipt[i]) {
				received++;
			}
		}
		return (received * 100) / nbBlocs;
	}

	/**
	 * Remplit un bloc a partir d'un certain indice begin.
	 * 
	 * @param begin
	 *            L'indice du premier byte du bloc.
	 * 
	 * @param bloc
	 *            Un tableau de bytes representant les donnees du bloc.
	 */
	public void feed(int begin, byte[] bloc) {
		/*
		 * if (bloc.length != BLOCK_SIZE || begin % BLOCK_SIZE != 0) { throw new
		 * IllegalArgumentException(); }
		 */
		receipt[begin / BLOCK_SIZE] = true;
		for (int i = 0; i < bloc.length; i++, begin++) {
			this.data[begin] = bloc[i];
		}
		if (this.isComplete()) {
			this.check();
		}

	}

	/**
	 * Teste si la piece est complete.
	 * 
	 * @return True si tous les blocs ont etes recus, false sinon.
	 */
	public boolean isComplete() {
		for (int i = 0; i < nbBlocs; i++) {
			if (!this.receipt[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Teste si la Piece est correcte apres avoir teste si elle est complete Si
	 * la piece n'est pas correcte elle est remise a zero!
	 * 
	 * @return True si la piece est correcte, false sinon.
	 */
	public boolean check() {
		if (this.isComplete()) {
			MessageDigest shaDigest = null;
			try {
				shaDigest = MessageDigest.getInstance("SHA");

			} catch (NoSuchAlgorithmException e) {
				System.out.println("No SHA support in this VM.");
			}

			byte[] testSHA = shaDigest.digest(this.data);
			for (int i = 0; i < hash.length; i++) {
				if (testSHA[i] != this.hash[i]) {
					this.reset();
					return false;
				}
			}
			return true;
		}
		this.reset();
		return false;
	}

	/**
	 * Remet a zero la piece
	 */
	private void reset() {
		for (int i = 0; i < this.nbBlocs; i++) {
			this.receipt[i] = false;
		}

	}
}
