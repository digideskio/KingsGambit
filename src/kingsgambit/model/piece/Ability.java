package kingsgambit.model.piece;

import java.util.LinkedList;

import kingsgambit.model.Board;
import kingsgambit.model.Square;
import kingsgambit.model.WeaponDieFace;
import kingsgambit.model.command.AttackPieceCommand;
import kingsgambit.model.command.Command;

public class Ability {	
	public LinkedList<Command> getCommands(Piece piece, Board board) {
		LinkedList<Command> commands = new LinkedList<Command>();
		
		for (Square s : pattern.getCoverage(piece.getPosition(), piece.getFacing())) {
			if (board.hasPieceAt(s)) {
				Piece other = board.getPieceAt(s);
				if (other.faction != piece.getFaction())
					commands.add(new AttackPieceCommand(piece, other, rollsPerFigure*piece.getNumFigures(), faces, 1));
			}
		}
		
		return commands;
	}
	
	public Ability(ScanPattern pattern, int rollsPerFigure, WeaponDieFace... faces) {
		this.pattern = pattern;
		this.rollsPerFigure = rollsPerFigure;
		this.faces = faces;
	}

	private int rollsPerFigure;
	private ScanPattern pattern;
	private WeaponDieFace[] faces;
}
