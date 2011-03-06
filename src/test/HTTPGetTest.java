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
			URL announce = new URL("http://127.0.0.1:6969/announce");
			HTTPGet.get(announce);
		} catch (Throwable e) {
			System.out.println(e.getLocalizedMessage());
		}

	}
}
