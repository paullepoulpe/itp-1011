package http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;

import bencoding.BDecoder;
import bencoding.BEValue;

import com.sun.xml.internal.ws.wsdl.writer.document.Port;

public class HTTPGet {
	private String infoHash;
	private String peerId;
	private int port;
	private int left;
	private byte compact;
	private Socket socket;
	private int numWant;
	private String event;
	private String trackerId;
	private String query;

	public HTTPGet(String URLAnnounce) {
		URL tracker = null;
		String path, domain;
		int serverPort;
		try {
			tracker = new URL(URLAnnounce);
		} catch (MalformedURLException f) {
			System.out
					.println("L'url fournie en paramètre n'est pas dans un bon format.");
		}
		path = tracker.getPath();
		domain = tracker.getHost();
		serverPort = tracker.getPort();
		try {
			socket = new Socket(domain, serverPort);
		} catch (UnknownHostException e) {
		} catch (IOException f) {
		}
		infoHash = BinaryURLEncoder.encode(infoHash.getBytes());
//		pour la methode qui suit, je suis pas sur que si un des parametres est null, cela marche quand meme
//		il faudrait mettre des if() etc, mais ca m'a l'air vraiment long pour un truc aussi petit.
		query = "GET" + path + "?info_hash=" + this.infoHash + "&peer_id="
				+ this.peerId + "&port=" + port + "&left=" + this.left
				+ "&compact=" + this.compact + "&event="+this.event+"HTTP/1.0\n\r\n\r\n";

		// a faire: initialiser tous les parametres, la plupart sont par
		// default, a regarder dans le cours,
		// infoHash a l'air complique (voir digestInputStream dans BDecoder)
		// perrId et TrackerId, je sais pas comment faire
	}

	public byte[] get() {
		BufferedWriter request= null;
		try{
			request = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			request.write(this.query);
		}catch(IOException e){}
		return null;
	}
}
