package http;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import bencoding.BDecoder;
import torrent.peer.PeerIDGenerator;

public class HTTPGet {
	private URL announce;
	private String infoHash;
	private String peerId;
	private int port;
	private int left;
	private byte compact = 1;
	private int numWant = 50;
	private String event;
	private String trackerId;

	public HTTPGet(String urlAnnounce, File metainfo) {
		try {
			announce = new URL(urlAnnounce);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		BDecoder bob = null;

		try {
			bob = new BDecoder(new FileInputStream(metainfo));
			bob.bdecode();
			infoHash = BinaryURLEncoder.encode(bob.getSpecialMapDigest());
		} catch (FileNotFoundException e) {
			System.out.println("Probleme1: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Probleme4: " + e.getMessage());
		}

		peerId = PeerIDGenerator.generateID();
		port = 30000; // a changer
		left = 500; // a changer
		event = "started";

	}

	public byte[] get() {
		Socket socket = null;
		BufferedWriter request = null;
		// BufferedReader bob = null;
		InputStream recu = null;
		ByteArrayOutputStream reponse = null;
		try {
			int announcePort = 80;
			if (announce.getPort() != -1) {
				announcePort = announce.getPort();
			}
			socket = new Socket(announce.getHost(), announcePort);
			request = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			request.write("GET " + announce.getPath() + "?info_hash="
					+ infoHash + "&peer_id=" + peerId + "&port=" + port
					+ "&compact=" + compact + "&numwant=" + numWant + "&left="
					+ left + "&event=" + event + " HTTP/1.0\n\r\n\r\n");
			request.flush();
			recu = new BufferedInputStream(socket.getInputStream());
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

			// bob = new BufferedReader(new InputStreamReader(socket
			// .getInputStream()));
			// String blabla = "";
			// while (blabla != null) {
			// blabla = bob.readLine();
			// System.out.println(blabla);
			// }
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return reponse.toByteArray();
	}
}
