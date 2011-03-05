/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package http;

import java.awt.List;

import torrent.peer.*;

public class AnnounceInfo {
	private int interval;
	private int minInterval;
	private String failureReason;
	private List peers;

	public List getPeers() {
		return peers;
	}

	public AnnounceInfo(byte[] data) {

	}
}
