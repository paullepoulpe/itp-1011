package http;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Cette classe sert uniquement a faire la requete HTTPGet. Elle utilise une
 * {@link HashMap} pour envoyer celle-ci
 * 
 * @author Damien Engels et Maarten Sap
 * 
 */
public class HTTPGet {
	private HashMap<String, String> query;
	private URL announce;

	/**
	 * Initialise l'adresse a laquelle il faut faire l'annonce a l'aide
	 * d'urlAnnounce.
	 * 
	 * @param urlAnnounce
	 *            Une string representant l'url a laquelle faire l'annonce
	 */
	public HTTPGet(String urlAnnounce) {
		this(urlAnnounce, new HashMap<String, String>());
	}

	/**
	 * Initialise l'adresse a laquelle il faut faire l'annonce a l'aide
	 * d'urlAnnounce et la liste de parametres a l'aide de query!
	 * 
	 * @param urlAnnounce
	 *            Une string representant l'url a laquelle faire l'annonce
	 * 
	 * @param query
	 *            La liste des parametres de la requete
	 */
	public HTTPGet(String urlAnnounce, HashMap<String, String> query) {
		try {
			announce = new URL(urlAnnounce);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		this.query = (HashMap<String, String>) query.clone();

	}

	/**
	 * Permet d'ajouter un parametre a la requete
	 * 
	 * @param key
	 *            Le nom du parametre
	 * @param value
	 *            La valeur du parametre
	 */
	public void add(String key, String value) {
		query.put(key, value);
	}

	/**
	 * Cette methode fait la requete au serveur et renvoye la reponse sous la
	 * forme d'un tableau de bytes
	 * 
	 * @return Un tableau de bytes qui represente la reponse du serveur
	 * 
	 * @throws FailureReasonExeption
	 *             Renvoie une exception si la requete echoue
	 */
	public byte[] get() throws FailureReasonExeption {
		Socket socket = null;
		BufferedWriter envoi = null;
		InputStream recu = null;
		ByteArrayOutputStream reponse = null;
		try {
			int announcePort = 80;
			if (announce.getPort() != -1) {
				announcePort = announce.getPort();
			}

			socket = new Socket(announce.getHost(), announcePort);
			envoi = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
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
			throw new FailureReasonExeption(e.getLocalizedMessage());
		}
		return reponse.toByteArray();
	}
}