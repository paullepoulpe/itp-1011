/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package http;

import java.awt.List;
import java.util.HashMap;
import java.util.Map;

import bencoding.BDecoder;
import bencoding.BEValue;
import bencoding.InvalidBEncodingException;

import torrent.peer.*;

public class AnnounceInfo {
	private int interval;
	private int minInterval;
	private String failureReason;
	private String TrackerId;
	private int complete;
	private int incomplete;
	private List peers;

	public List getPeers() {
		return peers;
	}

	public AnnounceInfo(byte[] data) {
		// recupere les info de la reponse de HTTPget et les mets sous forme
		// lisible
		BEValue decodeur = null;
		Map dico = null;

		decodeur = new BEValue(data);
		try {
			dico = decodeur.getMap();
			this.interval = ((BEValue) dico.get("interval")).getInt();
			this.minInterval = ((BEValue) dico.get("min interval")).getInt();
			this.failureReason = ((BEValue) dico.get("failure reason"))
					.getString();
			this.complete = ((BEValue) dico.get("complete")).getInt();
			this.incomplete = ((BEValue) dico.get("incomplete")).getInt();
			this.peers = (List) ((BEValue) dico.get("peers")).getList();
		} catch (InvalidBEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	public String toString() {
		return "interval: " + interval + "\n" + "minInterval: " + minInterval
				+ "\n" + "failureReason: " + failureReason + "\n"
				+ "TrackerId: " + TrackerId + "\n" + "complet: " + complete
				+ "\n" + "incomplete: " + incomplete + "\n" + "peers: " + peers
				+ "\n";
	}
}
