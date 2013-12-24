package kingsgambit.model.command;

import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;

public class UnplacePieceCommand implements Command {

	public void accept(CommandExecutor cex) {
		cex.execute(this);
	}
	
	public Piece getPiece() {
		return piece;
	}

	public Square getOperativeSquare() {
		return square;
	}

	public int getCost() {
		return 0;
	}
	
	public String toString() {
		return "UnplacePieceCommand(" + piece + ", " + square + ")";
	}

	public UnplacePieceCommand(Piece piece, Square square) {
		this.piece = piece;
		this.square = square;
	}
	
	private Piece piece;
	private Square square;
}
