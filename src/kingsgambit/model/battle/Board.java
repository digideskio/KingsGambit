package kingsgambit.model.battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import kingsgambit.model.Direction;
import kingsgambit.model.Faction;
import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;

public class Board {
	public boolean containsSquare(Square s) {
		return s.row >=0 && s.row < height && s.column >=0 && s.column < width;
	}

	public boolean containsSquare(int row, int column) {
		return row >=0 && row < height && column >=0 && column < width;
	}

	public int getColumns() {
		return width;
	}
	
	public int getRows() {
		return height;
	}

	public Piece getPieceAt(Square square) {
		if (!containsSquare(square))
			return null;
		
		return board[square.column][square.row];
	}

	public Piece getPieceAt(int row, int column) {
		return board[column][row];
	}

	public boolean hasPieceAt(Square square) {
		return containsSquare(square) && board[square.column][square.row] != null;
	}

	public boolean hasPieceAt(int row, int col) {
		return containsSquare(row, col) && board[col][row] != null;
	}
	
	public void movePiece(Piece p, Square destination) {
		Square oldPosition = p.getPosition();
		if (oldPosition != null)
			board[oldPosition.column][oldPosition.row] = null;
		board[destination.column][destination.row] = p;
		p.setPosition(destination);
	}
	
	public void addPiece(Piece p) {
		p.setBoard(this);
		pieces.add(p);
		
		if (!factionPieces.containsKey(p.getFaction()))
			factionPieces.put(p.getFaction(), new HashSet<Piece>());
		factionPieces.get(p.getFaction()).add(p);
		
		movePiece(p, p.getPosition());
	}
	
	public void removePiece(Piece p) {
		pieces.remove(p);
		factionPieces.get(p.getFaction()).remove(p);
		board[p.getPosition().column][p.getPosition().row] = null;
	}

	public Iterable<Piece> getPieces() {
		return pieces;
	}
	
	public Iterable<Piece> getPieces(Faction f) {
		return factionPieces.get(f);
	}
	
	public boolean noIntervening(Square from, Square to) {
		int xInc = 0;
		if (from.column > to.column)
			xInc = -1;
		else if (from.column < to.column)
			xInc = 1;
		
		int yInc = 0;
		if (from.row > to.row)
			yInc = -1;
		else if (from.row < to.row)
			yInc = 1;
		
		int x = from.column;
		int y = from.row;
		do {
			x += xInc;
			y += yInc;

			if (x == to.column)
				xInc = 0;
			if (y == to.row)
				yInc = 0;

			if (board[x][y] != null)
				return false;
		} while (x != to.column || y != to.row);
		
		return true;
	}
	
	public HashSet<Square> whereOnBoard(Set<Square> squares) {
		HashSet<Square> on = new HashSet<Square>();
		for (Square s : squares) {
			if (containsSquare(s))
				on.add(s);
		}
		return on;
	}
	
	public HashSet<Square> openMovement(Square origin, Direction direction, int distance) {
		HashSet<Square> squares = new HashSet<Square>();
		for (int d = 1; d<=distance; ++d) {
			Square s = direction.projectFrom(origin, d);
			
			if (!containsSquare(s) || hasPieceAt(s))
				break;
			
			squares.add(s);
		}
		
		return squares;
	}
	
	/**
	 * Returns an iterable of squares tracing a line from origin, given direction d.
	 * The line extends all the way to the edge of the board, regardless of square occupation.
	 */
	public Iterable<Square> line(final Square origin, final Direction d) {
		return new Iterable<Square>() {
			public Iterator<Square> iterator() {
				return new Iterator<Square>() {
					public boolean hasNext() {
						return containsSquare(current);
					}
					public Square next() {
						Square next = current;
						current = d.projectFrom(current, 1);
						return next;
					}
					public void remove() {
					}
					Square current = origin;
				};
			}
		};
	}
	
	public boolean moveIsInDirection(Square from, Square to, Direction direction) {
		switch(direction) {
		case NORTH:
			return from.column == to.column && from.row > to.row;
		case SOUTH:
			return from.column == to.column && from.row < to.row;
		case EAST:
			return from.column < to.column && from.row == to.row;
		case WEST:
			return from.column > to.column && from.row == to.row;
		}
		return false;
	}
	
	public Board(int width, int height, BattleConfiguration config) {
		this.width = width;
		this.height = height;
		
		board = new Piece[width][height];
		pieces = new HashSet<Piece>();
		factionPieces = new HashMap<Faction, HashSet<Piece>>();
		
		for (Piece p : config.getInitialPieces())
			addPiece(p);
	}
	
	private int width;
	private int height;
	
	private Piece[][] board;
	private HashSet<Piece> pieces;
	private HashMap<Faction, HashSet<Piece>> factionPieces;
}