/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package test;

import http.HTTPGet;

import java.net.URL;

public class HTTPGetTest {
	public static void main(String[] args) {
		try {
			URL announce = new URL("http://icsinsrv1.epfl.ch:6969/");
			HTTPGet.get(announce);
		} catch (Throwable e) {
			System.out.println(e.getLocalizedMessage());
		}

	}
}
