package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import settings.CryptoSettings;

import crypto.KeyGenerator;
import crypto.XOR.SymetricKey;

/**
 * Cette classe represente un message d'echange de cle symetrique avec le pair.
 * 
 * @author Damien, Maarten
 * 
 */
public class SendSymmetricKey extends Message {
	private SymetricKey XORKey;
	private int id;

	public SendSymmetricKey() {
		this.XORKey = KeyGenerator
				.generateSymmetricKey(CryptoSettings.SymetricKeySize);
		this.id = ID.sendSymmetricKey.ordinal();
		;
	}

	public SendSymmetricKey(DataInputStream in) throws IOException {
		int messageLength = in.readInt();
		this.id = in.readByte();
		byte[] key = new byte[messageLength - 1];
		in.readFully(key);
		this.XORKey = new SymetricKey(key);

	}

	@Override
	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {
		output.writeInt(XORKey.getBytes().length + 1);
		output.writeByte(id);
		output.write(XORKey.getBytes());
		;
		output.flush();
	}

	public SymetricKey getXORKey() {
		return XORKey;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return id + " : " + Arrays.toString(XORKey.getBytes());
	}
}
