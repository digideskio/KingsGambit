package kingsgambit.view.battle;

import jmotion.animation.Animation;
import jmotion.sprite.Sprite;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.PieceMoveEvent;
import kingsgambit.model.event.PieceTurnEvent;
import kingsgambit.model.event.RetreatEvent;
import kingsgambit.model.piece.Piece;

public interface PieceSprite extends Sprite {
	public Piece getPiece();
	public Animation animate(PieceMoveEvent event);
	public Animation animate(PieceTurnEvent event);
	public Animation animate(AttackEvent event);
	public Animation animate(RetreatEvent event);
	public Animation takeDamage(int damage, Piece attacker);
	public Animation die();
	public void pieceUpdated();
}
