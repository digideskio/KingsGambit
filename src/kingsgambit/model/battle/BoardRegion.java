package kingsgambit.model.battle;

import java.util.Iterator;

import kingsgambit.model.Square;

public abstract class BoardRegion implements Iterable<Square> {
	public static BoardRegion createRectangle(final int row, final int col, final int width, final int height) {
		return new BoardRegion() {
			public boolean contains(Square s) {
				return s.row >= row && s.row < row+height
					&& s.column >= col && s.column < col+width;
			}
			
			public Iterator<Square> iterator() {
				return new Iterator<Square>() {
					public boolean hasNext() {
						return iRow < row + height;
					}

					public Square next() {
						Square s = new Square(iRow, iCol);
						++iCol;
						if (iCol == col+width) {
							iCol = col;
							++iRow;
						}
						return s;
					}

					public void remove() {					
					}
					
					private int iRow = row;
					private int iCol = col;
				};
			}
		};
	}
	
	public abstract boolean contains(Square s);
}
