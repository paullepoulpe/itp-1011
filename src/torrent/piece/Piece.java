package torrent.piece;

import gui.FunnyBar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import torrent.messages.Request;
import torrent.peer.PeerHandler;

/**
 * Cette classe est (obviously) la classe contnenant une piece d'un torrent.
 * Elle implemente diverses methodes qui traitent du nombre de demandes sur un
 * bloc, d'une alimentation des donnees, etc.
 * 
 * @author Damien Engels, Maarten Sap
 * 
 */
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
	private FunnyBar funnyBar;

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
		funnyBar = new FunnyBar(sizeTab);
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
		if (isChecked) {
			return 100;
		}
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
			funnyBar.add(begin / BLOCK_SIZE);
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

				peerHandlers.get(begin / BLOCK_SIZE).get(i)
						.removeRequest(new Request(index, begin, bloc.length));

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
	private boolean isComplete() {
		if (isChecked) {
			return true;
		} else {
			for (int i = 0; i < nbBlocs; i++) {
				if (!this.receipt[i]) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Teste si la Piece est correcte apres avoir teste si elle est complete Si
	 * la piece n'est pas correcte elle est remise a zero! Elle change en meme
	 * temps la valeur de boolean isChecked, qui devient prend la valeur de
	 * retour de cette methode.
	 * 
	 * @return True si la piece est correcte, false sinon.
	 */
	private boolean check() {

		if (this.isComplete()) {
			MessageDigest shaDigest = null;
			try {
				shaDigest = MessageDigest.getInstance("SHA");

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
			} catch (NoSuchAlgorithmException e) {
				System.out.println("No SHA support in this VM.");
				return false;
			}
		} else {
			this.reset();
			return false;
		}

	}

	/**
	 * Remet a zero la piece
	 */
	private void reset() {
		funnyBar.removeAll();
		for (int i = 0; i < this.nbBlocs; i++) {
			this.receipt[i] = false;
		}

	}

	public int getSizeTab() {
		return sizeTab;
	}

	/**
	 * Cette methode permet de mettre les donnees data dans notre piece
	 * 
	 * @param data
	 *            : le tableau de bytes contenant nos donnees
	 */
	public void setData(byte[] data) {
		if (data.length != this.data.length) {
			System.out.println("probleme de taille pour data");
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

	/**
	 * 
	 * @param begin
	 *            : l'indice du bloc voulu
	 * @return le bloc qu'on veut sous forme de byte[]
	 */
	public byte[] getBlock(int begin) {
		byte[] bloc = new byte[BLOCK_SIZE];
		for (int i = 0; i < bloc.length; i++) {
			bloc[i] = data[i + begin];
		}
		return bloc;
	}

	/**
	 * Prepare le Request a envoyer au pair. On regarde parmi la liste de
	 * peerHandlers par bloc (d'ou la liste de liste de peerHandlers) combien il
	 * y a de requetes courantes sur ce bloc. On cherche le bloc qui en a le
	 * moins, et on demande celui-la dans la Request.
	 * 
	 * @param peerHandler
	 *            : on l'ajoute a la liste de PeerHandlers qui demandent ce
	 *            bloc.
	 * @return la requete a ajouter a la queue (Request)
	 */
	public Request getBlockOfInterest(PeerHandler peerHandler) {
		int blocIndex = 0;
		int min = Integer.MAX_VALUE;
		int length = peerHandlers.size();
		for (int i = 0; i < length; i++) {
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

	/**
	 * 
	 * @return le nombre de demandes sur la piece
	 */
	public int getNbDemandes() {
		int nbDemandes = 0;
		for (int i = 0; i < peerHandlers.size(); i++) {
			nbDemandes += peerHandlers.get(i).size();
		}
		return nbDemandes;
	}

	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public String toString() {
		return index + "";
	}

	public FunnyBar getFunnyBar() {
		return funnyBar;
	}
	public int getNbBlocs() {
		return nbBlocs;
	}
}
