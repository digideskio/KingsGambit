package kingsgambit.model.event;

import kingsgambit.model.piece.Piece;

public class PiecePlaceEvent implements GameEvent {

	public Piece getPiece() {
		return piece;
	}
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}
	
	public PiecePlaceEvent(Piece piece) {
		this.piece = piece;
	}
	
	private Piece piece;
}
