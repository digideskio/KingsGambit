package kingsgambit.model.event;

import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;

public class PieceMoveEvent implements GameEvent {
	public final Piece mover;
	public final Square from;
	public final Square to;
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}

	public String toString() {
		return "PieceMoveEvent("+mover+"->"+to+")";
	}
	
	
	public PieceMoveEvent(Piece mover, Square from, Square to) {
		this.mover = mover;
		this.from = from;
		this.to = to;
	}
}
