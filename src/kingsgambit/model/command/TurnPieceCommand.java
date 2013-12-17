package kingsgambit.model.command;

import kingsgambit.model.Direction;
import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;

public class TurnPieceCommand implements Command {
	public int getCost() {
		return mover.getMovesPerTurn();
	}
	
	public Square getOperativeSquare() {
		return newFacing.projectFrom(mover.getPosition(), 1);
	}

	public Direction getFacing() {
		return newFacing;
	}
	
	public Piece getMover() {
		return mover;
	}
	
	public void accept(CommandExecutor cex) {
		cex.execute(this);
	}
	
	public String toString() {
		return mover + " faces " + newFacing;
	}

	public TurnPieceCommand(Piece mover, Direction newFacing) {
		this.mover = mover;
		this.newFacing = newFacing;
	}

	private Piece mover;
	private Direction newFacing;
}
