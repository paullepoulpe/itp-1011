package http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

import com.sun.xml.internal.ws.wsdl.writer.document.Port;

public class HTTPGet {
	private URL infoHash;
	private URL peerId;
	private int port;
	private byte compact;
	private int numWant;
	private int left;
	private String event;
	private String trackerId;

	public HTTPGet(String urlAnnounce) {
		URL announce = null;
		try {
			announce = new URL(urlAnnounce);
			this.port = announce.getPort();

		} catch (Throwable e) {
			System.out.println("Probleme : " + e.getLocalizedMessage());
		}

	}

	public static byte[] get(URL announce) {
		Socket socket = null;
		BufferedWriter alice = null;
		BufferedReader bob = null;
		try {
			socket = new Socket(announce.getHost(), announce.getPort());
			alice = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			alice.write(announce.getPath());
			alice.flush();
			bob = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			System.out.println(bob.readLine());

		} catch (Throwable e) {
			System.out.println("Probleme : " + e.getLocalizedMessage());
		}
		return null;
	}
}
