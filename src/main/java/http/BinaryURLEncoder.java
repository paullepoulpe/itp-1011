package http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Formatter;

/**
 * Binary URL-encoding utilities for SHA-1 hashes of torrents
 * 
 * @author Bruno Didot
 * @author Ismail Amrani
 * @author Amos Wenger (ndd@rylliog.cz)
 */
public class BinaryURLEncoder {

	/** Hide the constructor - it's an utility class */
	private BinaryURLEncoder() {
	}

	/**
	 * Do a binary URL-encoding of a SHA-1 hash - used in requests to torrent
	 * trackers.
	 * 
	 * @param input
	 *            An array of (usually 20) bytes composing the SHA-1 hash
	 * @return a URL-encoded String representation of the hash
	 */
	public static String encode(byte[] input) {
		try {
			return URLEncoder.encode(new String(input, "ISO-8859-1"),
					"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			StringBuilder result = new StringBuilder();

			for (int i = 0; i < input.length; i++) {
				int val = input[i];
				// this seems to be the right way to do it - Java's SHA-1
				// checksum handles
				// unsigned vals in a weird way.
				if (val < 0)
					val += 256;

				if (('a' <= val && val <= 'z') || ('A' <= val && val <= 'Z')
						|| val == '.' || val == '-' || val == '_' || val == '~') {
					result.append((char) val);
				} else {
					// URL-encode characters that aren't in [A-Za-z\.\-_~]
					new Formatter(result).format("%%%02X", val);
				}
			}
			System.err
					.println("L'encodage url n'est pas supporté, l'encodage sera realisé grace aux classes fournie !");
			return result.toString();
		}
	}

}
