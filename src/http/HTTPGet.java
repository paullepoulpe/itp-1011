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
//		BufferedReader bob = null;
		InputStream bobet = null;
		ByteArrayOutputStream reponse = null;
		try {
			socket = new Socket(announce.getHost(), announce.getPort());
			request = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream()));
			request.write("GET " + announce.getPath() + "?info_hash="
					+ infoHash + "&peer_id=" + peerId + "&port=" + port
					+ "&compact=" + compact + "&numwant=" + numWant + "&left="
					+ left + "&event=" + event + " HTTP/1.0\n\r\n\r\n");
			request.flush();
			bobet = new BufferedInputStream(socket.getInputStream());
			byte[] lecture = new byte[1];
			byte[] retourLigne = "\r\n".getBytes();
			while (true) {
				bobet.read(lecture);
				if (lecture[0] == retourLigne[0]) {
					bobet.read(lecture);
					if (lecture[0] == retourLigne[1]) {
						bobet.read(lecture);
						if (lecture[0] == retourLigne[0]) {
							bobet.read(lecture);
							if (lecture[0] == retourLigne[1]) {
								break;
							}
						}
					}
				}
			}
			reponse = new ByteArrayOutputStream();
			bobet.read(lecture);
			while(lecture[0]!=0){
				reponse.write(lecture);
				bobet.read(lecture);
			}
			
			

//			bob = new BufferedReader(new InputStreamReader(socket
//					.getInputStream()));
//			String blabla = "";
//			while (blabla != null) {
//				blabla = bob.readLine();
//				System.out.println(blabla);
//			}
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return reponse.toByteArray();
	}
}
