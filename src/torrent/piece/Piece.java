package torrent.piece;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import torrent.messages.Request;
import torrent.peer.PeerHandler;

public class Piece {
	private byte[] data;
	private boolean[] receipt;
	private int sizeTab;
	private byte[] hash;
	private int index;
	private int nbBlocs;
	private boolean isChecked = false;
	static public int BLOCK_SIZE = 1 << 14;
	private ArrayList<ArrayList<PeerHandler>> peerHandlers;

	/**
	 * Constructeur
	 * 
	 * @param index
	 *            Index de la piece
	 * @param sizeTab
	 *            Taille de la piece en bytes
	 * @param hash
	 *            Somme de controle SHA-1
	 */
	public Piece(int index, int sizeTab, byte[] hash) {
		this.index = index;
		this.sizeTab = sizeTab;
		this.hash = hash;
		this.data = new byte[sizeTab];
		this.nbBlocs = (int) Math.ceil((double) sizeTab / (double) BLOCK_SIZE);
		this.receipt = new boolean[nbBlocs];
		this.peerHandlers = new ArrayList<ArrayList<PeerHandler>>(nbBlocs);
		for (int i = 0; i < nbBlocs; i++) {
			peerHandlers.add(new ArrayList<PeerHandler>());
		}
	}

	/**
	 * Retourne les donnees si la piece est complete et null sinon
	 * 
	 * @return Tableau de bytes representant les donnees
	 */
	public byte[] getData() {
		if (isChecked) {
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
	public double getDownloadCompleteness() {
		int received = 0;
		for (int i = 0; i < nbBlocs; i++) {
			if (receipt[i]) {
				received++;
			}
		}
		return ((double) received * 100) / nbBlocs;
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
		if (!this.isChecked) {
			/*
			 * le debut doit etre un multiple de BLOCK_SIZE et doit etre contenu
			 * dans le tableau de bytes
			 */
			if (begin % BLOCK_SIZE != 0 || begin >= this.sizeTab || begin < 0) {
				throw new IllegalArgumentException(
						"Mauvais index de début de bloc");
			}
			/*
			 * la taille du bloc doit etre egale a BLOCK_SIZE exepte pour le
			 * dernier bloc et le dernier bloc doit tenir dans le tableau
			 */
			if ((bloc.length != BLOCK_SIZE)) {
				if ((begin / BLOCK_SIZE) != receipt.length - 1) {
					throw new IllegalArgumentException(
							"Le bloc a la mauvaise taille");
				} else if (bloc.length != this.sizeTab - begin) {
					throw new IllegalArgumentException(
							"Le dernier bloc a la mauvaise taille");
				}

			}

			for (int i = 0; i < peerHandlers.get(begin / BLOCK_SIZE).size(); i++) {
				synchronized (peerHandlers.get(begin / BLOCK_SIZE).get(i)) {
					peerHandlers
							.get(begin / BLOCK_SIZE)
							.get(i)
							.removeRequest(
									new Request(index, begin, bloc.length));
				}

			}

			peerHandlers.get(begin / BLOCK_SIZE).clear();

			receipt[begin / BLOCK_SIZE] = true;

			for (int i = 0; i < bloc.length; i++, begin++) {
				this.data[begin] = bloc[i];
			}

			if (this.isComplete()) {
				this.check();
			}
		}

	}

	/**
	 * Teste si la piece est complete.
	 * 
	 * @return True si tous les blocs ont etes recus, false sinon.
	 */
	public boolean isComplete() {
		if (isChecked) {
			return true;
		}
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
			this.isChecked = true;
			System.out.println("Piece " + index
					+ " recue completement !!!!!!!!!!!");
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

	public int getSizeTab() {
		return sizeTab;
	}

	public void setData(byte[] data) {
		if (data.length != this.data.length) {
			System.out.println("problème de taille pour data");
		} else {
			this.data = data;
			for (int i = 0; i < this.receipt.length; i++) {
				this.receipt[i] = true;
			}
			this.check();
		}

	}

	public int getIndex() {
		return index;
	}

	public byte[] getBlock(int begin) {
		byte[] bloc = new byte[BLOCK_SIZE];
		for (int i = 0; i < bloc.length; i++) {
			bloc[i] = data[i + begin];
		}
		return bloc;
	}

	public Request getBlockOfInterest(PeerHandler peerHandler) {
		int blocIndex = 0;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < peerHandlers.size(); i++) {
			if (min > peerHandlers.get(i).size() && !receipt[i]) {
				blocIndex = i;
				min = peerHandlers.get(i).size();
			}
		}
		int blocSize = BLOCK_SIZE;
		if (blocIndex == nbBlocs - 1) {
			blocSize = sizeTab - (blocIndex * BLOCK_SIZE);
		}
		Request requete = new Request(this.index, blocIndex * BLOCK_SIZE,
				blocSize);
		peerHandlers.get(blocIndex).add(peerHandler);

		return requete;
	}

	public int getNbDemandes() {
		int nbDemandes = 0;
		for (int i = 0; i < peerHandlers.size(); i++) {
			nbDemandes += peerHandlers.get(i).size();
		}
		return nbDemandes;
	}
}
