package kingsgambit.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kingsgambit.model.battle.Board;
import kingsgambit.model.command.Command;
import kingsgambit.model.piece.Piece;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	public final Faction faction;
	
	public String toString() {
		return faction + " player";
	}
	
	public int getMovesPerTurn() {
		return movesPerTurn;
	}
	
	public int getMovesLeft() {
		return movesLeft;
	}
	
	public void spendMoves(int moves) {
		movesLeft = Math.max(0, movesLeft - moves);
	}
	
	public void resetTurn() {
		movesLeft = movesPerTurn;
	}
	
	public List<Command> getLegalCommands(Piece p) {
		LinkedList<Command> commands = new LinkedList<Command>();
		
		for (Command c : p.getCommands()) {
			if (c.getCost() <= movesLeft)
				commands.add(c);
		}
		
		return commands;
	}
	
	public List<Command> getLegalCommands() {

		LinkedList<Command> commands = new LinkedList<Command>();
		
		for (Piece p : board.getPieces(faction)) {
			for (Command c : p.getCommands()) {
				if (c.getCost() <= movesLeft)
					commands.add(c);
			}
		}
		
		return commands;
	}
	
	public Player(Faction faction, Board board, int movesPerTurn) {
		this.faction = faction;
		this.movesPerTurn = movesPerTurn;
		this.board = board;
		resetTurn();
	}
	
	private Board board;
	private int movesLeft;
	private int movesPerTurn;
}
