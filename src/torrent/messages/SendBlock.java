package torrent.messages;

public class SendBlock extends Message {
	private int pieceIndex, blocIndex;
	private byte[] bloc;

	public SendBlock(int pieceInd, int blocInd, byte[] bloc) {
		this.pieceIndex = pieceInd;
		this.blocIndex = blocInd;
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
}
