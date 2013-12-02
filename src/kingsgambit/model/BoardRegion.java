package kingsgambit.model;

public class BoardRegion {
	public final int minRow;
	public final int maxRow;
	public final int minColumn;
	public final int maxColumn;
	
	public BoardRegion(int minRow, int maxRow, int minColumn, int maxColumn) {
		this.minRow = minRow;
		this.maxRow = maxRow;
		this.minColumn = minColumn;
		this.maxColumn = maxColumn;
	}
}
