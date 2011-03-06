package http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
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
	private int numWant;
	private String event;
	private String trackerId;

	public HTTPGet() {
		//a faire: initialiser tous les parametres, la plupart sont par default, a regarder dans le cours, 
		//infoHash a l'air complique (voir digestInputStream dans BDecoder)
		//perrId et TrackerId, je sais pas comment faire
	}

	public byte[] get() {
		// a faire: créer un socket pour se connecter au tracker
		//creer un streamwriter sur ce socket(voir cours)
		//ecrire la requete sur le socket en suivant le modèle donnée dans le cours
		//recuperer la reponse avec un inputStream (?sous forme de tableau de bytes??)
		return null;
	}
}
