package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Cette classe est la classe la plus importante du projet, car c'est en fait le
 * message contenant les donnees d'un bloc!
 * 
 * @author Damien, Maarten
 * 
 */
public class SendBlock extends Message {
	private int pieceIndex, blocIndex;
	private byte[] bloc;

	public SendBlock(int pieceIndex, int blocIndex, byte[] bloc) {
		this.pieceIndex = pieceIndex;
		this.blocIndex = blocIndex;
		this.bloc = bloc;
	}

	public int getPieceIndex() {
		return pieceIndex;
	}

	public int getBlocIndex() {
		return blocIndex;
	}

	public byte[] getBloc() {
		return bloc;
	}

	public void accept(MessageVisitor v) {
		v.visit(this);
	}

	@Override
	public void send(DataOutputStream output) throws IOException {
		try {
			output.writeInt(9 + bloc.length);
			output.writeByte(7);
			output.writeInt(pieceIndex);
			output.writeInt(blocIndex);
			output.write(bloc);
			System.out.println("Sent SendBlock");
		} catch (IOException e) {
			throw e;
		}

	}

	public boolean equals(SendBlock otherSendBlock) {
		return otherSendBlock.pieceIndex == this.pieceIndex
				&& otherSendBlock.blocIndex == this.blocIndex
				&& Arrays.equals(otherSendBlock.bloc, this.bloc);
	}
}
