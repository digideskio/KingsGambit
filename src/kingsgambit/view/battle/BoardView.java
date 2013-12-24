package kingsgambit.view.battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import jmotion.animation.Animation;
import jmotion.animation.AnimationSequence;
import jmotion.animation.AnimatorPanel;
import jmotion.sprite.Sprite;
import jmotion.sprite.SpriteLayer;
import jmotion.sprite.SpriteSpace;
import kingsgambit.model.Square;
import kingsgambit.model.battle.Board;
import kingsgambit.model.battle.BoardRegion;
import kingsgambit.model.command.AttackPieceCommand;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.MovePieceCommand;
import kingsgambit.model.command.TurnPieceCommand;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.BattleBeginEvent;
import kingsgambit.model.event.BeginTurnEvent;
import kingsgambit.model.event.DieEvent;
import kingsgambit.model.event.GameEvent;
import kingsgambit.model.event.GameEventHandler;
import kingsgambit.model.event.GameOverEvent;
import kingsgambit.model.event.PieceMoveEvent;
import kingsgambit.model.event.PiecePlaceEvent;
import kingsgambit.model.event.PieceTurnEvent;
import kingsgambit.model.event.PieceUnplacedEvent;
import kingsgambit.model.event.RetreatEvent;
import kingsgambit.model.piece.Piece;

public class BoardView extends AnimatorPanel implements GameEventHandler {

	private static final long serialVersionUID = 1L;

	public void removeSprite(Sprite s) {
		pieceLayer.removeSprite(s);
	}

	public void removePiece(Piece p) {
		pieceSprites.remove(p);
	}

	public SpriteLayer getBannerLayer() {
		return bannerLayer;
	}
	
	public void addAnimation(Animation a) {
		if (a != null) {
			a.start();
			animations.add(a);
		}
	}
	
	public Animation getAnimation(final GameEvent event) {
		eventAnimation = null;
		event.accept(this);

		return eventAnimation;
	}
	
	public void handle(PieceMoveEvent event) {
		eventAnimation = pieceSprites.get(event.mover).animate(event);
	}
	
	public void handle(PieceTurnEvent event) {
		eventAnimation = pieceSprites.get(event.turner).animate(event);
	}

	public void handle(AttackEvent event) {
		eventAnimation = new AnimationSequence(
				pieceSprites.get(event.attacker).animate(event),
				pieceSprites.get(event.defender).takeDamage(event.numKills, event.attacker)
				);
	}
	
	public void handle(DieEvent die) {
		eventAnimation = pieceSprites.get(die.piece).die();
	}
	
	public void handle(RetreatEvent retreat) {
		eventAnimation = pieceSprites.get(retreat.piece).animate(retreat);
	}
	
	public void handle(BeginTurnEvent event) {
	}

	public void handle(BattleBeginEvent begin) {
	}

	public void handle(GameOverEvent over) {
	}
	
	public void handle(PiecePlaceEvent event) {
		Piece p = event.getPiece();
		if (pieceSprites.containsKey(p)) {
			pieceSprites.get(p).pieceUpdated();
		} else {
			pieceSprites.put(p, PieceSpriteFactory.createSprite(p, this, pieceLayer));
		}
	}
	
	public void handle(PieceUnplacedEvent event) {
		Piece p = event.getPiece();
		PieceSprite sprite = pieceSprites.get(p);
		sprite.removeAllSprites();
		pieceSprites.remove(p);
	}
	
	public void previewCommand(Command c) {
		if (c instanceof AttackPieceCommand) {
			highlight(c.getOperativeSquare(), Color.RED);
		} else if (c instanceof MovePieceCommand) {
			MovePieceCommand move = (MovePieceCommand)c;
			indicatorLayer.addSprite(new ArrowSprite(move.getSource(), move.getDestination(), this));
			//highlight(c.getOperativeSquare(), Color.BLUE);
		} else if (c instanceof TurnPieceCommand) {
			TurnPieceCommand turn = (TurnPieceCommand)c;
			indicatorLayer.addSprite(new ArrowSprite(turn.getMover().getPosition(), turn.getOperativeSquare(), this));
			//highlight(c.getOperativeSquare(), Color.BLUE);
		}
	}
	
	public void clearPreviews() {
		indicatorLayer.removeAll();
	}
	
	public Point getSquarePosition(int row, int col) {
		return new Point(col*tileWidth, (row+1)*tileWidth);
	}

	public Point getSquareCenter(int row, int col) {
		return new Point(col*tileWidth + tileWidth/2, (row+1)*tileWidth + tileWidth/2);
	}
	
	public Square getSquareForPoint(Point p) {
		return new Square(p.y/tileWidth-1, p.x/tileWidth);
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public void selectPiece(Piece p) {
	}
	
	public void highlight(Square s, Color c) {
		squareColors.put(s, c);
	}
	
	public void clearHighlights() {
		squareColors.clear();
	}
	
	public BoardRegion getShroudedRegion() {
		return shroud;
	}
	
	public void setShroud(BoardRegion shroudedRegion) {
		shroud = shroudedRegion;
	}
	
	public BoardView(Board board, BattleView battleView) {
		this.battleView = battleView;
		this.board = board;
		tileWidth = 70;

		Dimension size = new Dimension(board.getColumns()*tileWidth, (board.getRows()+2)*tileWidth);
		setPreferredSize(size);
		
		indicatorLayer = new SpriteSpace();
		pieceLayer = new SpriteSpace();
		bannerLayer = new SpriteSpace();
		
		pieceSprites = new HashMap<Piece, PieceSprite>();
		squareColors = new HashMap<Square, Color>();
		shroud = BoardRegion.getFullRegion().getComplement();
		
		for (Piece p : board.getPieces())
			pieceSprites.put(p, PieceSpriteFactory.createSprite(p, this, pieceLayer));
		
		animations = new LinkedList<Animation>();
	}
	
	@Override
	protected void advanceFrame(int millis) {
		battleView.advanceFrame(millis);
		
		LinkedList<Animation> finished = new LinkedList<Animation>();
		for (Animation a : animations) {
			if (a.isFinished())
				finished.add(a);
			else
				a.stepAhead(millis);
		}
		animations.removeAll(finished);
	}

	@Override
	protected void render(Graphics2D g) {
		// Draw the background
		Image grass = null;
		try {
			grass = ImageIO.read(new File("assets/grass.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int col = 0; col<board.getColumns(); col+=2) {
			for (int row = -1; row<board.getColumns(); row+=2) {
				Point p = getSquarePosition(row, col);
				g.drawImage(grass, p.x, p.y, null);
			}
		}
		
		if (shroud != null) {
			for (int col = 0; col<board.getColumns(); ++col) {
				for (int row = -1; row<=board.getRows(); ++row) {
					if (shroud.contains(new Square(row, col)))
						shroudSquare(g, row, col);
				}
			}
		}

		Image redHighlight = null;
		Image blueHighlight = null;
		try {
			redHighlight = ImageIO.read(new File("assets/redselector.gif"));
			blueHighlight = ImageIO.read(new File("assets/blueselector.gif"));
		} catch (IOException e) {
		}
		
		for (Square s : squareColors.keySet()) {
			if (squareColors.get(s) == Color.red) {
				Point p = getSquarePosition(s.row, s.column);
				g.drawImage(redHighlight, p.x, p.y, null);
			} else if (squareColors.get(s) == Color.blue) {
				Point p = getSquarePosition(s.row, s.column);
				g.drawImage(blueHighlight, p.x, p.y, null);
			} else {
				//outlineSquare(s, squareColors.get(s), g);
			}
		}

		for (Sprite s : indicatorLayer)
			s.render(g);
		for (Sprite s : pieceLayer)
			s.render(g);
		for (Sprite s : bannerLayer)
			s.render(g);
	}
	
	private void shroudSquare(Graphics2D g, int row, int col) {
		Color shroudBase = Color.black;
		Color shroud = new Color(shroudBase.getRed(), shroudBase.getGreen(), shroudBase.getBlue(), 100);
		g.setColor(shroud);
		Point p = getSquarePosition(row, col);
		g.fillRect(p.x, p.y, tileWidth, tileWidth);
	}
	
	private Animation eventAnimation;
	
	private BattleView battleView;
	private Board board;
	private int tileWidth;
	
	private SpriteLayer indicatorLayer;
	private SpriteSpace pieceLayer;
	private SpriteLayer bannerLayer;
	
	private HashMap<Piece, PieceSprite> pieceSprites;
	private HashMap<Square, Color> squareColors;
	private BoardRegion shroud;
	
	private LinkedList<Animation> animations;
}
