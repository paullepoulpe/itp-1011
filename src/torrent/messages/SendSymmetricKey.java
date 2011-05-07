package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import crypto.RSA.KeyGenerator;

/**
 * Cette classe represente un message d'echange de cle symetrique avec le pair.
 * 
 * @author Damien, Maarten
 * 
 */
public class SendSymmetricKey extends Message {
	private byte[] XORKey;
	private int id;

	public SendSymmetricKey() {
		this.XORKey = KeyGenerator.generateSymmetricKey(128);
		this.id = 12;
	}

	public SendSymmetricKey(DataInputStream in) throws IOException {
		int messageLength = in.readInt();
		this.id = in.read();
		this.XORKey = new byte[messageLength - 1];
		in.read(XORKey);
	}

	@Override
	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {
		output.write(XORKey.length + 1);
		output.write(id);
		output.write(XORKey);
	}

	public byte[] getXORKey() {
		return XORKey;
	}
	public int getId() {
		return id;
	}
}
