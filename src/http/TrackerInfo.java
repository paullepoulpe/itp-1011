/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package http;

public class TrackerInfo {
	public AnnounceInfo announce() {
		HTTPGet request = new HTTPGet("");
		AnnounceInfo info = new AnnounceInfo(request.get(null));
		return info;
	}
}
