package kingsgambit.model.event;

import kingsgambit.model.Direction;
import kingsgambit.model.piece.Piece;

public class PieceTurnEvent implements GameEvent {
	public final Piece turner;
	public final Direction turnTowards;
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}
	
	public String toString() {
		return "TurnPieceEvent("+turner+"->"+turnTowards+")";
	}
	
	public PieceTurnEvent(Piece turner, Direction turnTowards) {
		this.turner = turner;
		this.turnTowards = turnTowards;
	}
}
