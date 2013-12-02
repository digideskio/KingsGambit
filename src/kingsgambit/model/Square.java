package kingsgambit.model;

import java.awt.Point;
import java.io.Serializable;

public class Square extends Point implements Serializable {
	private static final long serialVersionUID = 1L;

	public final int row;
	public final int column;
	
	public boolean equals(Object other) {
		if (other instanceof Square) {
			Square otherSquare = (Square)other;
			return otherSquare.row == row && otherSquare.column == column;
		}
		
		return false;
	}
	
	public Direction direction(Square other) {
		if (other.column > column)
			return Direction.EAST;
		if (other.column < column)
			return Direction.WEST;
		if (other.row > row)
			return Direction.SOUTH;
		return Direction.NORTH;
	}
	
	public Square plus(Square other) {
		return new Square(other.row+row, other.column+column);
	}
	
	public Square minus(Square other) {
		return new Square(row-other.row, column-other.column);
	}
	
	public int manhattenDistance(Square other) {
		return Math.abs(row-other.row) + Math.abs(column-other.column);
	}
	
	public String toString() {
		return row+","+column;
	}
	
	public int hashCode() {
		return row*1000 + column;
	}

	public Square(int row, int column) {
		super(column, row);
		this.row = row;
		this.column = column;
	}
}
