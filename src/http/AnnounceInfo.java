package http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

/**
 * Cette classe est un conteneur, qui decode la réponse d'une requete httpget
 * et la met sous forme lisible
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class AnnounceInfo {
	private int interval;
	private int minInterval;
	private String failureReason;
	private String trackerId;
	private int complete;
	private int incomplete;
	private byte[] peers;

	/**
	 * Recupere la reponse d'une requete httpget et la met sous forme lisible
	 * 
	 * @param data
	 *            un tableau de byte qui correspond a la reponse du tracker
	 */
	public AnnounceInfo(byte[] data) {
		BDecoder decodeur = null;
		BEValue decodValue = null;
		Map<String, BEValue> dico = null;

		try {
			decodeur = new BDecoder(new ByteArrayInputStream(data));
			decodValue = decodeur.bdecodeMap();
			dico = decodValue.getMap();
			this.interval = ((BEValue) dico.get("interval")).getInt();
			this.minInterval = ((BEValue) dico.get("min interval")).getInt();
			if (dico.get("failure reason") != null) {
				this.failureReason = dico.get("failure reason").getString();
			} else {
				this.failureReason = "none";
			}
			if (dico.get("tracker id") != null) {
				this.trackerId = dico.get("tracker id").getString();
			} else {
				this.trackerId = "none";
			}
			this.complete = dico.get("complete").getInt();
			this.incomplete = dico.get("incomplete").getInt();
			this.peers = dico.get("peers").getBytes();
		} catch (InvalidBEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @return la liste des peers associes a ce tracker sous forme compacte
	 */
	public byte[] getPeers() {
		return peers;
	}

	/**
	 * Imprime de maniere lisible(human readable) les informations concernant
	 * cette classe
	 */
	public String toString() {
		return "Interval: " + interval + "\n" + "Min interval: " + minInterval
				+ "\n" + "Failure reason: " + failureReason + "\n"
				+ "TrackerId: " + trackerId + "\n" + "Complete: " + complete
				+ "\n" + "Incomplete: " + incomplete;
	}
}
