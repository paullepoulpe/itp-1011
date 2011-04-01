package http;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import sun.awt.geom.AreaOp.AddOp;
import torrent.peer.PeerIDGenerator;

/**
 * Cette classe sert uniquement a faire la requete http
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class HTTPGet {
	private HashMap<String, String> query;
	private URL announce;

	public HTTPGet(String urlAnnounce) {
		query = new HashMap<String, String>();
		try {
			announce = new URL(urlAnnounce);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	// a recoder pour meilleure encapsulation
	public HTTPGet(String urlAnnounce, HashMap<String, String> query) {
		try {
			announce = new URL(urlAnnounce);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.query = query;
	}

	public void add(String key, String value) {
		query.put(key, value);
	}

	public byte[] get() {
		Socket socket = null;
		BufferedWriter envoi = null;
		InputStream recu = null;
		ByteArrayOutputStream reponse = null;
		String requete = "";
		try {
			int announcePort = 80;
			if (announce.getPort() != -1) {
				announcePort = announce.getPort();
			}

			socket = new Socket(announce.getHost(), announcePort);
			envoi = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			recu = new BufferedInputStream(socket.getInputStream());

			Iterator<Entry<String, String>> entries = query.entrySet()
					.iterator();

			envoi.write("GET " + announce.getPath());

			if (entries.hasNext()) {
				Entry<String, String> param = entries.next();
				envoi.write("?" + param.getKey() + "=" + param.getValue());
			}
			while (entries.hasNext()) {
				Entry<String, String> param = entries.next();
				envoi.write("&" + param.getKey() + "=" + param.getValue());
			}

			envoi.write(" HTTP/1.0\n\r\n\r\n");
			envoi.flush();

			byte[] retourLigne = "\r\n".getBytes();
			while (true) {
				if (recu.read() == retourLigne[0]) {
					if (recu.read() == retourLigne[1]) {
						if (recu.read() == retourLigne[0]) {
							if (recu.read() == retourLigne[1]) {
								break;
							}
						}
					}
				}
			}
			reponse = new ByteArrayOutputStream();
			int lecture = recu.read();
			while (lecture != -1) {
				reponse.write(lecture);
				lecture = recu.read();
			}
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return reponse.toByteArray();
	}
}