package http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		infoHash = BinaryURLEncoder.encode(bob.getSpecialMapDigest());
		peerId = PeerIDGenerator.generateID();
		port = 30000; // a changer
		left = 500; // a changer
		event = "started";

	}

	public byte[] get() {
		Socket socket = null;
		BufferedWriter request = null;
		BufferedReader bob = null;
		try {
			socket = new Socket(announce.getHost(), announce.getPort());
			request = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			request.write("GET " + announce.getPath() + "?info_hash="
					+ infoHash + "&peer_id=" + peerId + "&port=" + port
					+ "&compact=" + compact + "&numwant=" + numWant + "&left="
					+ left + "&event=" + event + " HTTP/1.0\n\r\n\r\n");
			request.flush();
			System.out.println("ok1");
			bob = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String blabla = "";
			while (blabla != null) {
				blabla = bob.readLine();
				System.out.println(blabla);
			}
		} catch (IOException e) {
		}
		return null;
	}
}
