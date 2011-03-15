package test;

import java.io.File;

import http.AnnounceInfo;
import http.HTTPGet;

public class HttpGetTest {
	public static void main(String[] args) {
		HTTPGet bob = new HTTPGet(
				"http://0.0.0.0:6969"/* "http://icsinsrv1.epfl.ch:6969/" */,
				new File("data/LePetitPrince.torrent"));
		byte[] reponse = bob.get();
		for (int i = 0; i < reponse.length; i++) {
			System.out.println(reponse[i]);
		}
		AnnounceInfo patrick = new AnnounceInfo(reponse);
	}
}
