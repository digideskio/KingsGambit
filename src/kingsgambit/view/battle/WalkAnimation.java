package kingsgambit.view.battle;

import java.awt.Point;

import jmotion.animation.Animation;
import kingsgambit.model.Direction;

public class WalkAnimation extends Animation {
	public void start() {
		if (start.x > end.x)
			dx = -speed;
		else if (start.x < end.x)
			dx = speed;
		if (start.y > end.y)
			dy = -speed;
		if (start.y < end.y)
			dy = speed;
	}

	public void stepAhead(int millis) {
		if (Math.abs(pieceSprite.getX() - end.x) < Math.abs(dx))
			dx = end.x - pieceSprite.getX();
		if (Math.abs(pieceSprite.getY() - end.y) < Math.abs(dy))
			dy = end.y - pieceSprite.getY();
		
		pieceSprite.nextFrame();
		pieceSprite.move(dx, dy);
		
		isFinished = pieceSprite.getX() == end.x && pieceSprite.getY() == end.y;
		if (isFinished)
			pieceSprite.setFrames(direction, SinglePieceSprite.Action.STAND);
	}

	public boolean isFinished() {
		return isFinished;
	}

	public WalkAnimation(SinglePieceSprite pieceSprite, Point start, Point end, BoardView view, int speed) {
		this.pieceSprite = pieceSprite;
		this.speed = speed;
		this.start = start;
		this.end = end;
		direction = Direction.between(start, end);
		pieceSprite.setFrames(direction, SinglePieceSprite.Action.WALK);
	}

	private SinglePieceSprite pieceSprite;
	private boolean isFinished;
	private Direction direction;
	private int speed;
	private int dx;
	private int dy;
	private Point start;
	private Point end;
}
