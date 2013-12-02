package kingsgambit.view.battle;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jmotion.sprite.Sprite;
import kingsgambit.model.Square;

public class ArrowSprite implements Sprite {

	public void render(Graphics2D g) {
		for (int i = 0; i<images.length; ++i) {
			Point pos = positions[i];
			g.drawImage(images[i], pos.x, pos.y, null);
		}
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
		int numSquares = 1;//from.manhattenDistance(to) + 1;
		images = new Image[numSquares];
		positions = new Point[numSquares];
		try {
			switch (from.direction(to)) {
			case NORTH:
				images[numSquares-1] = ImageIO.read(new File("assets/arrowhead_up.gif"));
				break;
			case SOUTH:
				images[numSquares-1] = ImageIO.read(new File("assets/arrowhead_down.gif"));
				break;
			case EAST:
				images[numSquares-1] = ImageIO.read(new File("assets/arrowhead_right.gif"));
				break;
			case WEST:
				images[numSquares-1] = ImageIO.read(new File("assets/arrowhead_left.gif"));
				break;
			}
			
			positions[numSquares-1] = centerImage(to);
			
			for (int i = 0; i<numSquares-1; ++i) {
				//positions[i] = centerImage(to.)
			}
			
		} catch (IOException io) {
			
		}
	}
	
	private Point centerImage(Square s) {
		Point p = view.getSquareCenter(s.row, s.column);
		return new Point(p.x - 35, p.y - 35);
	}
	
	private BoardView view;
	private Image[] images;
	private Point[] positions;
}
