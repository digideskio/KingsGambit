package kingsgambit.view.battle;

import jmotion.animation.Animation;
import kingsgambit.model.Direction;

public class TurnAnimation extends Animation {

	public void start() {
		sprite.setFrames(dir, SinglePieceSprite.Action.STAND);
	}

	public void stepAhead(int millis) {
	}

	public boolean isFinished() {
		return true;
	}

	public TurnAnimation(SinglePieceSprite sprite, Direction dir) {
		this.sprite = sprite;
		this.dir = dir;
	}
	
	private SinglePieceSprite sprite;
	private Direction dir;
}
