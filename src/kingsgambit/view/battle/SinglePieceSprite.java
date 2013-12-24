package kingsgambit.view.battle;

import java.awt.Graphics2D;
import java.awt.Point;

import jmotion.animation.ActionAnimation;
import jmotion.animation.Animation;
import jmotion.animation.AnimationSequence;
import jmotion.animation.FrameSet;
import jmotion.animation.PlayFramesAnimation;
import jmotion.sprite.Sprite;
import jmotion.sprite.SpriteSpace;
import kingsgambit.model.Direction;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.PieceMoveEvent;
import kingsgambit.model.event.PieceTurnEvent;
import kingsgambit.model.event.RetreatEvent;
import kingsgambit.model.piece.Piece;

public class SinglePieceSprite implements PieceSprite {

	public static enum Action {
		STAND, WALK, FIGHT, DIE
	}

	public Piece getPiece() {
		return piece;
	}

	public void removeAllSprites() {
		space.removeSprite(this);
	}

	public Animation animate(PieceMoveEvent event) {
		Point center = view.getSquareCenter(event.to.row, event.to.column);
		Point destination = new Point(center.x + xDisplacement, center.y + yDisplacement);
		return new WalkAnimation(this, new Point(x, y), destination, view, 6);
	}

	public Animation animate(AttackEvent event) {
		return new PlayFramesThenReset(frames, sequenceNum(piece.getFacing(), Action.FIGHT), sequenceNum(piece.getFacing(), Action.STAND));
	}

	public Animation animate(PieceTurnEvent event) {
		return new TurnAnimation(this, event.turnTowards);
	}
	
	public Animation animate(RetreatEvent retreat) {
		System.out.println("Animating retreat from " + retreat.from + " to " + retreat.to);
		Point center = view.getSquareCenter(retreat.to.row, retreat.to.column);
		Point destination = new Point(center.x + xDisplacement, center.y + yDisplacement);
		return new AnimationSequence(
			new ActionAnimation() {
				public void action() {
					setPanic(true);
				}
			},
			new TurnAnimation(this, Direction.between(retreat.from, retreat.to)),
			new WalkAnimation(this, new Point(x, y), destination, view, 6),
			new ActionAnimation() {
				public void action() {
					setPanic(false);
				}
			}
		);
	}
	
	public Animation takeDamage(int damage, Piece attacker) {
		final FrameSet hitEffect = BattleView.LOADER.getFrames("attack_" + attacker.getType());
		
		Animation addEffect = new Animation() {
			public void stepAhead(int millis) {
			}
			public void start() {
				effect = hitEffect;
				effect.setSequence(0);
			}
			public boolean isFinished() {
				return true;
			}
		};
		
		effectAnimation = new AnimationSequence(
				addEffect,
				new PlayFramesAnimation(hitEffect, 0),
				new DieAnimation(piece, this, view)
				);
		return effectAnimation;
	}
	
	public void setPanic(boolean panic) {
		isPanic = panic;
	}
	
	public Animation die() {
		return new DieAnimation(piece, this, view);
	}
	
	public void nextFrame() {
		frames.advanceFrame();
	}

	public void pieceUpdated() {
		Point center = view.getSquareCenter(piece.getPosition().row, piece.getPosition().column);
		x = center.x + xDisplacement;
		y = center.y + yDisplacement;
	}
	
	public void render(Graphics2D g) {
		if (view.getShroudedRegion().contains(piece.getPosition()))
			return;

		g.drawImage(frames.currentFrame(), x-getWidth()/2, y-getHeight(), null);
		
		if (effectAnimation == null || effectAnimation.isFinished()) {
			effectAnimation = null;
			effect = null;
		}
		if (effect != null)
			g.drawImage(effect.currentFrame(), x-effect.getWidth()/2, y-effect.getHeight(), null);
			
		if (isPanic)
			panicEffect.render(g);
	}

	public void setLocation(int x, int y) {
		 this.x = x;
		 this.y = y;
	}

	public void move(int deltaX, int deltaY) {
		space.moveSprite(this, deltaX, deltaY);
		panicEffect.setLocation(panicEffect.getX()+deltaX, panicEffect.getY()+deltaY);
	}

	public void moveTo(int x, int y) {
		move(x-this.x, y-this.y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return frames.getWidth();
	}

	public int getHeight() {
		return frames.getHeight();
	}

	public void setFrames(Direction d, Action a) {
		frames.setSequence(sequenceNum(d, a));
	}
	
	public SinglePieceSprite(Piece piece, BoardView view, SpriteSpace space) {
		this.piece = piece;
		this.view = view;
		this.space = space;
		frames = BattleView.LOADER.getFrames(piece.getType());
		setFrames(piece.getFacing(), Action.STAND);
		
		Point center = view.getSquareCenter(piece.getPosition().row, piece.getPosition().column);
		x = center.x;
		y = center.y;
		space.addSprite(this);

		panicEffect = BattleView.LOADER.getStaticSprite("panic.gif");
		panicEffect.setLocation(x-panicEffect.getWidth()/2, y-frames.getHeight()-panicEffect.getHeight());
	}

	protected SinglePieceSprite(Piece piece, BoardView view, SpriteSpace space, int xDisplacement, int yDisplacement) {
		this(piece, view, space);
		this.xDisplacement = xDisplacement;
		this.yDisplacement = yDisplacement;
		move(xDisplacement, yDisplacement);
	}

	private int sequenceNum(Direction d, Action a) {
		int dirIndex = 0;
		if (d == Direction.EAST)
			dirIndex = 1;
		else if (d == Direction.SOUTH)
			dirIndex = 2;
		else if (d == Direction.WEST)
			dirIndex = 3;
		return a.ordinal() + dirIndex * Action.values().length;
	}

	private Piece piece;
	private int x;
	private int y;
	private int xDisplacement;
	private int yDisplacement;
	private FrameSet frames;
	private SpriteSpace space;
	private BoardView view;

	private FrameSet effect;
	private Animation effectAnimation;
	private boolean isPanic;
	private Sprite panicEffect;
}
