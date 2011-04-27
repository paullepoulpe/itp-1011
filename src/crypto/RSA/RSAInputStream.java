package crypto.RSA;

import java.io.IOException;
import java.io.InputStream;

/**
 * Cette classe represente le canal securise a travers lequel le pair pourra
 * nous envoyer sa cle symetrique.
 * 
 * @author MAARTEN
 * 
 */
public class RSAInputStream extends InputStream {
	public RSAInputStream() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int read(byte[] arg0) throws IOException {
		// TODO Auto-generated method stub
		return super.read(arg0);
	}

}
