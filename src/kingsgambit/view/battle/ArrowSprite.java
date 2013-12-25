package kingsgambit.view.battle;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import jmotion.sprite.Sprite;
import kingsgambit.model.Direction;
import kingsgambit.model.Square;

public class ArrowSprite implements Sprite {

	private static final Image[] arrowheads = new Image[4];
	
	static {
		for (Direction d : Direction.values())
			arrowheads[d.ordinal()] = BattleView.LOADER.readImage("arrowhead_" +d.toString().toLowerCase() + ".gif");
	}
	
	public void render(Graphics2D g) {
		g.drawImage(arrowheads[direction.ordinal()], position.x, position.y, null);
	}

	public void setLocation(int x, int y) {
	}

	public int getX() {
		return 0;
	}

	public int getY() {
		return -1;
	}

	public int getWidth() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public ArrowSprite(Square from, Square to, BoardView view) {
		this.view = view;
		direction = from.direction(to);
		position = centerImage(to);
	}
	
	private Point centerImage(Square s) {
		Point p = view.getSquareCenter(s.row, s.column);
		return new Point(p.x - 35, p.y - 35);
	}
	
	private BoardView view;
	private Point position;
	private Direction direction;
}
