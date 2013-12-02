package kingsgambit.model.event;

import kingsgambit.model.piece.Piece;

public class DieEvent implements GameEvent {
	
	public final Piece piece;
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}

	public DieEvent(Piece piece) {
		this.piece = piece;
	}
}
