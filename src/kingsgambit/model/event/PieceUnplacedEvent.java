package kingsgambit.model.event;

import kingsgambit.model.piece.Piece;

public class PieceUnplacedEvent implements GameEvent {

	public Piece getPiece() {
		return piece;
	}
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}
	
	public PieceUnplacedEvent(Piece piece) {
		this.piece = piece;
	}
	
	private Piece piece;
}
