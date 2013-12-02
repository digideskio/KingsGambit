package kingsgambit.model.event;

import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;

public class RetreatEvent implements GameEvent {

	public final Piece piece;
	public final Square from;
	public final Square to;
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}

	public RetreatEvent(Piece piece, Square from, Square to) {
		this.piece = piece;
		this.from = from;
		this.to = to;
	}
}
