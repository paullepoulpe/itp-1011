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

	// private String infoHash;
	// private static final String PEER_ID = PeerIDGenerator.generateID();
	// private int port;
	// private int left;
	// private byte compact = 1;
	// private int numWant = 50;
	// private String event;
	// private String trackerId;

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

// /**
// * Fait la requete et récupère la réponse du trakcer sous un tableau de
// byte
// *
// * @return Un tableau de byte qui représente la réponse du tracker a
// partir
// * du double retour a la ligne
// */
// public byte[] get() {
//
// System.out
// .println("Envoi de la requete au tracker " + announce + "...");
// Socket socket = null;
// BufferedWriter request = null;
// InputStream recu = null;
// ByteArrayOutputStream reponse = null;
// try {
// int announcePort = 80;
// if (announce.getPort() != -1) {
// announcePort = announce.getPort();
// }
// socket = new Socket(announce.getHost(), announcePort);
// request = new BufferedWriter(new OutputStreamWriter(socket
// .getOutputStream()));
// request.write("GET " + announce.getPath() + "?info_hash="
// + infoHash + trackerId + "&peer_id=" + PEER_ID + "&port="
// + port + "&compact=" + compact + "&numwant=" + numWant
// + "&left=" + left + "&event=" + event
// + " HTTP/1.0\n\r\n\r\n");
// request.flush();
// recu = new BufferedInputStream(socket.getInputStream());
//
// System.out.println("Reponse du tracker ...");
//
// byte[] retourLigne = "\r\n".getBytes();
// while (true) {
// if (recu.read() == retourLigne[0]) {
// if (recu.read() == retourLigne[1]) {
// if (recu.read() == retourLigne[0]) {
// if (recu.read() == retourLigne[1]) {
// break;
// }
// }
// }
// }
// }
// reponse = new ByteArrayOutputStream();
// int lecture = recu.read();
// while (lecture != -1) {
// reponse.write(lecture);
// lecture = recu.read();
// }
// } catch (IOException e) {
// System.out.println(e.getLocalizedMessage());
// }
// return reponse.toByteArray();
// }
//	
// /**
// * Initialise tous les paramètres necessaires a la requete http
// *
// * @param urlAnnounce
// * String contenant l'url du tracker
// * @param infoHash
// * une signature digitale du fichier Metainfo produite par
// * l'algorithme SHA1. Cette signature sur 20 bytes doit être
// * url-encodée
// * @param left
// * le nombres de bytes que le client doit encore télécharger
// * (codé en base 10 ASCII)
// * @param trackerId
// * si le tracker a renvoyé un trackerid lors d’une précédente
// * requête, il doit être renvoyé ici (par mesure de sécurité)
// * @param event
// * soit “started”, “stopped”, ou “completed”. La première requête
// * doit inclure l’évènement “started”
// * @param port
// * numéro du port sur lequel le client accepte des connexions de
// * pairs
// */
// public HTTPGet(String urlAnnounce, byte[] infoHash, int left,
// String trackerId, String event, int port) {
// try {
// announce = new URL(urlAnnounce);
// } catch (MalformedURLException e) {
// System.out.println(e.getMessage());
// }
// this.infoHash = BinaryURLEncoder.encode(infoHash);
// this.port = port;
// this.left = left;
// this.event = event;
// if (!trackerId.equals("<?>")) {
// this.trackerId = "&trackerid=" + trackerId;
// } else {
// this.trackerId = "";
// }
//
// }

