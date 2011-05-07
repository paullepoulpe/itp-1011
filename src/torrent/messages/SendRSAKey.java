package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.swing.JOptionPane;

import crypto.RSA.KeyPair;

/**
 * Cette classe est un message d'echange de cle publique RSA. Il decode les
 * donnes recues et les encapsule dans un KeyPair
 * 
 * @author Damien, Maarten
 * 
 */
public class SendRSAKey extends Message {
	private BigInteger N, eKey;
	private int id, kLength, Nlength, eKeyLength;
	private KeyPair keyPair;

	public SendRSAKey() {
		this.id=11;
	}

	public SendRSAKey(DataInputStream in) throws IOException {
		int messageLength = in.readInt();
		this.id = in.read();
		this.kLength = in.readInt();
		this.eKeyLength = in.readInt();
		this.Nlength = in.readInt();
		byte[] eBytes = new byte[eKeyLength];
		byte[] nBytes = new byte[Nlength];
		in.read(eBytes);
		in.read(nBytes);
		eKey = new BigInteger(eBytes);
		N = new BigInteger(nBytes);
		keyPair = new KeyPair(N, eKey, null, kLength);
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {
		output.write(1 + 4 + 4 + 4 + kLength + eKeyLength);
		output.write(id);
		output.writeInt(kLength);
		output.writeInt(eKeyLength);
		output.writeInt(Nlength);
		output.write(eKey.toByteArray());
		output.write(N.toByteArray());
		JOptionPane
				.showMessageDialog(
						null,
						"Envoye notre cle publique RSA!!! (Desactiver ce message dans la classe SendRSAKey ;P");
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}
	public int getId() {
		return id;
	}
}
