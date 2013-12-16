package kingsgambit.model.piece;

import java.util.LinkedList;
import java.util.List;

import kingsgambit.model.Direction;
import kingsgambit.model.Faction;
import kingsgambit.model.Square;
import kingsgambit.model.battle.Board;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.MovePieceCommand;
import kingsgambit.model.command.TurnPieceCommand;

public class Piece {

	public String toString() {
		return faction + " " + type;
	}
	
	public String getType() {
		return type;
	}

	public int getNumFigures() {
		return figures;
	}

	public int takeHits(int numHits) {
		int kills = Math.min(figures, numHits/healthPerFigure);
		figures -= kills;
		
		return kills;
	}

	public void die() {
		figures = 0;
	}
	
	public boolean isDead() {
		return figures == 0;
	}

	public Square getPosition() {
		return position;
	}

	public void setPosition(Square pos) {
		position = pos;
	}

	public Faction getFaction() {
		return faction;
	}

	public Direction getFacing() {
		return direction;
	}

	public void setFacing(Direction newFacing) {
		direction = newFacing;
	}

	public boolean hasActed() {
		return hasActed;
	}

	public void setHasActed(boolean a) {
		hasActed = a;
	}
	
	public LinkedList<Command> getCommands() {
		LinkedList<Command> commands = new LinkedList<Command>();
		if (!hasActed) {
			commands.addAll(getLegalMoves());
			commands.addAll(ability.getCommands(this, board));
		}
		return commands;
	}

	public LinkedList<Command> getLegalMoves() {
		LinkedList<Command> moves = new LinkedList<Command>();
		
		// Add moves
		Square moveTo = position;
		for (int i = 1; i<=speed; ++i) {
			moveTo = direction.projectFrom(moveTo, 1);
			if (!board.containsSquare(moveTo) || board.hasPieceAt(moveTo))
				break;
			moves.add(new MovePieceCommand(this, moveTo, 1));
		}
		
		// Add turns
		moves.add(new TurnPieceCommand(this, direction.leftFace(), 1));
		moves.add(new TurnPieceCommand(this, direction.rightFace(), 1));
		moves.add(new TurnPieceCommand(this, direction.aboutFace(), 1));
		
		return moves;
	}
	
	public void setBoard(Board b) {
		board = b;
	}
	
	public void addProperty(PieceProperty prop) {
		properties.add(prop);
	}
	
	public boolean hasProperty(PieceProperty prop) {
		return properties.contains(prop);
	}

	public Piece(String type, Faction faction, int numFigures, int healthPerFigure, Ability ability) {
		this(type, faction, 1, numFigures, healthPerFigure, ability);
	}
	
	public Piece(String type, Faction faction, int speed, int numFigures, int healthPerFigure, Ability ability) {
		this.type = type;
		this.faction = faction;
		this.figures = numFigures;
		this.healthPerFigure = healthPerFigure;
		this.ability = ability;
		this.speed = speed;
		properties = new LinkedList<PieceProperty>();
		position = new Square(0, 0);
	}
	
	protected final String type;
	protected final int healthPerFigure;
	protected final int speed;
	protected final Ability ability;

	protected Board board;
	protected Square position;
	protected int figures;
	protected Faction faction;
	protected Direction direction;
	protected boolean hasActed;
	
	protected List<PieceProperty> properties;
}