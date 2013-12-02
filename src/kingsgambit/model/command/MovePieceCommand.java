package kingsgambit.model.command;

import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;

public class MovePieceCommand implements Command {
	public int getCost() {
		return cost;
	}
	
	public Square getOperativeSquare() {
		return destination;
	}

	public Square getDestination() {
		return destination;
	}
	
	public Square getSource() {
		return source;
	}

	public Piece getMover() {
		return mover;
	}
	
	public String toString() {
		return mover + " -> " + destination;
	}
	
	public void accept(CommandExecutor cex) {
		cex.execute(this);
	}

	public MovePieceCommand(Piece mover, Square destination, int cost) {
		this.mover = mover;
		this.source = mover.getPosition();
		this.destination = destination;
		this.cost = cost;
	}

	private Piece mover;
	private Square source;
	private Square destination;
	private int cost;
}
