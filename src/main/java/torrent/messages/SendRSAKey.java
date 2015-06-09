package torrent.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import crypto.RSA.KeyPair;

/**
 * Cette classe est un message d'echange de cle publique RSA. Il decode les
 * donnes recues et les encapsule dans un KeyPair
 * 
 * @author Damien, Maarten
 * 
 */
public class SendRSAKey extends Message {
	private BigInteger mod, eKey;
	private int id, modLength;
	private KeyPair keyPair;

	public SendRSAKey(KeyPair key) {
		id = ID.sendRSAkey.ordinal();
		eKey = key.getEKey();
		modLength = key.getModLength();
		mod = key.getMod();

	}

	public SendRSAKey(DataInputStream in) throws IOException {
		int messageLength = in.readInt();
		this.id = in.readByte();
		modLength = in.readInt();

		int n = in.readInt();
		byte[] eBytes = new byte[n];
		in.readFully(eBytes);
		eKey = new BigInteger(eBytes);

		int m = in.readInt();
		byte[] modBytes = new byte[m];
		in.readFully(modBytes);
		mod = new BigInteger(modBytes);

		keyPair = new KeyPair(mod, eKey, null, modLength);
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {
		int n = eKey.toByteArray().length;
		int m = mod.toByteArray().length;
		output.writeInt(1 + 4 + 4 + 4 + n + m);
		output.writeByte(id);
		output.writeInt(modLength);
		output.writeInt(n);
		output.write(eKey.toByteArray());
		output.writeInt(m);
		output.write(mod.toByteArray());
		output.flush();
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Mod = " + mod.toString() + ", eKey = " + eKey.toString()
				+ " , N = " + modLength + " , eKeylength = "
				+ eKey.toByteArray().length + " , modlength = "
				+ mod.toByteArray().length;
	}
}
