package http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

/**
 * Cette classe est un conteneur, qui decode la reponse d'une requete httpget et
 * la met sous forme lisible
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class AnnounceInfo {
	private int interval;
	private int minInterval;
	private String trackerId;
	private int complete;
	private int incomplete;
	private byte[] peers;

	/**
	 * Recupere la reponse d'une requete httpget et la met sous forme lisible
	 * 
	 * @param data
	 *            un tableau de byte qui correspond a la reponse du tracker
	 * 
	 * @throws FailureReasonExeption
	 *             Leve une exception si le parametre "failure reason" est
	 *             present dans la reponse du tracker
	 */
	public AnnounceInfo(byte[] data) throws FailureReasonExeption {
		BDecoder decodeur = null;
		BEValue decodValue = null;
		Map<String, BEValue> dico = null;

		try {
			decodeur = new BDecoder(new ByteArrayInputStream(data));
			decodValue = decodeur.bdecodeMap();
			dico = decodValue.getMap();

			if (dico.get("failure reason") != null) {
				String failureReason = dico.get("failure reason").getString();
				throw new FailureReasonExeption(failureReason);
			} else {
				if (dico.get("tracker id") != null) {
					this.trackerId = dico.get("tracker id").getString();
				} else {
					this.trackerId = "none";
				}
				this.interval = dico.get("interval").getInt();
				if (dico.get("min interval") != null) {
					this.minInterval = dico.get("min interval").getInt();
				}
				this.complete = dico.get("complete").getInt();
				this.incomplete = dico.get("incomplete").getInt();
				this.peers = dico.get("peers").getBytes();
			}
		} catch (InvalidBEncodingException e) {
			throw new FailureReasonExeption(e.getLocalizedMessage());
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
				+ "\n" + "TrackerId: " + trackerId + "\n" + "Complete: "
				+ complete + "\n" + "Incomplete: " + incomplete;
	}
}
