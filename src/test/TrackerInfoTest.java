package test;

import java.io.File;

import http.TrackerInfo;

public class TrackerInfoTest {
	public static void main(String[] args) {
		TrackerInfo bob = new TrackerInfo(
				/* "http://tracker.thepiratebay.org/announce" */"http://0.0.0.0:6969/");
		bob.announce(new File("data/LePetitPrince.torrent"));
		System.out.println(bob);
	}
}
