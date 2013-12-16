package kingsgambit.view.battle;

import java.awt.Graphics2D;

import jmotion.animation.Animation;
import jmotion.animation.ParallelAnimation;
import jmotion.sprite.SpriteSpace;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.PieceMoveEvent;
import kingsgambit.model.event.PieceTurnEvent;
import kingsgambit.model.event.RetreatEvent;
import kingsgambit.model.piece.Piece;

public class GroupPieceSprite implements PieceSprite {

	public void render(Graphics2D g) {
		for (int i = 0; i<sprites.length; ++i) {
			if (alive[i])
				sprites[i].render(g);
		}
	}

	public Piece getPiece() {
		return piece;
	}

	public Animation animate(PieceMoveEvent event) {
		Animation[] animations = new Animation[piece.getNumFigures()];
		int a = 0;
		for (int s = 0; s<sprites.length; ++s) {
			if (alive[s])
				animations[a++] = sprites[s].animate(event);
		}
		
		return new ParallelAnimation(animations);
	}

	public Animation animate(PieceTurnEvent turn) {
		Animation[] animations = new Animation[piece.getNumFigures()];
		int a = 0;
		for (int s = 0; s<sprites.length; ++s) {
			if (alive[s])
				animations[a++] = sprites[s].animate(turn);
		}
		
		return new ParallelAnimation(animations);
	}

	public Animation animate(RetreatEvent retreat) {
		Animation[] animations = new Animation[piece.getNumFigures()];
		int a = 0;
		for (int s = 0; s<sprites.length; ++s) {
			if (alive[s])
				animations[a++] = sprites[s].animate(retreat);
		}
		
		return new ParallelAnimation(animations);
	}

	public Animation animate(AttackEvent event) {
		Animation[] animations = new Animation[piece.getNumFigures()];
		int a = 0;
		for (int s = 0; s<sprites.length; ++s) {
			if (alive[s])
				animations[a++] = sprites[s].animate(event);
		}
		
		return new ParallelAnimation(animations);
	}

	public Animation takeDamage(int damage) {
		Animation[] animations = new Animation[damage];
		for (int d = 0; d<damage && aliveFigures > 0; ++d) {
			int index = getRandomSprite();
			alive[index] = false;
			--aliveFigures;
			animations[d] = sprites[index].takeDamage(1);
		}
		
		return new ParallelAnimation(animations);
	}
	
	public Animation die() {
		Animation[] animations = new Animation[aliveFigures];
		int a = 0;
		for (int s = 0; s<sprites.length; ++s) {
			if (alive[s])
				animations[a++] = sprites[s].die();
		}
		
		return new ParallelAnimation(animations);
	}

	public void setLocation(int x, int y) {
	}
	
	public void pieceUpdated() {
		for (SinglePieceSprite sprite : sprites)
			sprite.pieceUpdated();
	}

	public int getX() {
		return 0;
	}

	public int getY() {
		return 0;
	}

	public int getWidth() {
		return view.getTileWidth();
	}

	public int getHeight() {
		return view.getTileWidth();
	}
	
	public GroupPieceSprite(Piece piece, BoardView view, SpriteSpace space) {
		this.piece = piece;
		this.view = view;
		
		int offset = view.getTileWidth()/4;
		int[] xs = new int[]{-offset, offset, -offset, offset};
		int[] ys = new int[]{-offset, -offset, offset, offset};
		
		sprites = new SinglePieceSprite[piece.getNumFigures()];
		alive = new boolean[piece.getNumFigures()];
		aliveFigures = 0;
		
		for (int i = 0; i<piece.getNumFigures(); ++i) {
			SinglePieceSprite sprite = new SinglePieceSprite(piece, view, space, xs[i], ys[i]);
			sprites[i] = sprite;
			alive[i] = true;
			++aliveFigures;
		}
	}
	
	private int getRandomSprite() {
		int i;
		do {
			i = (int)(Math.random()*alive.length);
		} while (!alive[i]);
		return i;
	}
	
	private BoardView view;
	private SinglePieceSprite[] sprites;
	private boolean[] alive;
	private int aliveFigures;
	
	private Piece piece;
}
