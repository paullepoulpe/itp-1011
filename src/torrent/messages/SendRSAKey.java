package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Cette classe est un message d'echange de cle publique RSA
 * 
 * @author Damien, Maarten
 * 
 */
public class SendRSAKey extends Message {
	private BigInteger N, eKey;
	private int kLength, Nlength, eKeyLength;

	public SendRSAKey() {
		
	}
	public SendRSAKey(byte[] data){
		
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {
		output.writeInt(1);
		output.writeInt(kLength);
		output.writeInt(eKeyLength);
		output.writeInt(Nlength);
		output.write(eKey.toByteArray());
		output.write(N.toByteArray());
	}
}
