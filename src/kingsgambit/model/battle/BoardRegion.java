package kingsgambit.model.battle;

import kingsgambit.model.Square;

public abstract class BoardRegion {
	public static BoardRegion createRectangle(final int row, final int col, final int width, final int height) {
		return new BoardRegion() {
			public boolean contains(Square s) {
				return s.row >= row && s.row < row+height
					&& s.column >= col && s.column < col+width;
			}
		};
	}
	
	public abstract boolean contains(Square s);
}
