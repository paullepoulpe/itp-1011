package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

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
	public void send(DataOutputStream output) {
		try {
			output.write(9 + bloc.length);
			output.writeByte(7);
			output.write(pieceIndex);
			output.write(blocIndex);
			output.write(bloc);
			System.out.println("Sent SendBlock");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
