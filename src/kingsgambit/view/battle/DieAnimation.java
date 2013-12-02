package kingsgambit.view.battle;

import jmotion.animation.Animation;
import jmotion.sprite.Sprite;
import kingsgambit.model.piece.Piece;


public class DieAnimation extends Animation {

	public void stepAhead(int millis) {
	}

	public void start() {
		System.out.println("Beginning die animation - single piece");
		view.removeSprite(sprite);
		if (piece.isDead())
			view.removePiece(piece);
	}
	
	public boolean isFinished() {
		return true;
	}

	public DieAnimation(Piece piece, Sprite sprite, BoardView view) {
		this.piece = piece;
		this.sprite = sprite;
		this.view = view;
	}

	private Piece piece;
	private Sprite sprite;
	private BoardView view;
}
