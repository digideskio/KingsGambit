package kingsgambit.model.piece;

import java.util.HashSet;
import java.util.Set;

import kingsgambit.model.Direction;
import kingsgambit.model.Square;

public class ScanPattern {
	public boolean scan(Square from, Direction facing, Square target) {
		Square fromOrigin = target.minus(from);
		Square reoriented = facing.reverseTransform(fromOrigin).plus(origin);
		int row = reoriented.row;
		int column = reoriented.column;

		return column >=0 && column < pattern[0].length && row >= 0 && row < pattern.length && pattern[row][column];
	}
	
	public Set<Square> getCoverage(Square from, Direction facing) {
		HashSet<Square> coverage = new HashSet<Square>();

		for (int column = 0; column<pattern[0].length; ++column) {
			for (int row = 0; row<pattern.length; ++row) {
				if (pattern[row][column]) {
					Square fromOrigin = new Square(row-origin.row, column-origin.column);
					Square absolute = from.plus(facing.transform(fromOrigin));
					coverage.add(absolute);
				}
			}
		}
		
		return coverage;
	}
	
	public ScanPattern(boolean[][] pattern, Square origin) {
		this.origin = origin;
		this.pattern = pattern;
	}
	
	private Square origin;
	private boolean[][] pattern;
}
