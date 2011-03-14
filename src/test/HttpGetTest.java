package test;

import http.HTTPGet;

public class HttpGetTest {
	public static void main(String[] args) {
		HTTPGet bob = new HTTPGet("http://icsinsrv1.epfl.ch:6969/");
		bob.get();
	}
}
