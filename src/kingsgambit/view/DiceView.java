package kingsgambit.view;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jmotion.animation.Animation;
import jmotion.animation.AnimatorPanel;
import jmotion.animation.ParallelAnimation;
import kingsgambit.model.WeaponDieFace;
public class DiceView extends AnimatorPanel {
	private static final long serialVersionUID = 1L;
	private static final int SWORD = 0;
	private static final int BOW = 1;
	private static final int PANIC = 2;
	private static final int BLANK = 3;

	public Animation roll(WeaponDieFace[] faces) {
		Animation[] animations = new Animation[faces.length];
		
		for (DieFaceSprite sprite : sprites)
			sprite.face = BLANK;
		
		for (int i = 0; i<faces.length; ++i)
			animations[i] = sprites[i].roll(faces[i]);
		
		animation = new ParallelAnimation(animations);
		return animation;
	}
	
	protected void advanceFrame(int millis) {
		//if (animation != null && !animation.isFinished())
		//	animation.stepAhead(millis);
	}

	protected void render(Graphics2D g) {
		for (DieFaceSprite s : sprites)
			s.render(g);
	}
	
	public DiceView() {
		setSize(new Dimension(100, 100));
		setPreferredSize(new Dimension(100, 100));
		sprites = new DieFaceSprite[4];

		int[] xs = new int[]{0, 50, 0, 50};
		int[] ys = new int[]{0, 0, 50, 50};
		for (int i = 0; i<4; ++i)
			sprites[i] = new DieFaceSprite(xs[i], ys[i]);
		
		startAnimating();
	}

	private class DieFaceSprite {
		
		Animation roll(WeaponDieFace face) {
			int faceNum = 0;
			if (face == WeaponDieFace.AXE)
				faceNum = SWORD;
			else if (face == WeaponDieFace.BOW)
				faceNum = BOW;
			else if (face == WeaponDieFace.RETREAT)
				faceNum = PANIC;
			return new RollAnimation(this, faceNum);
		}
		
		void render(Graphics2D g) {
			if (face != BLANK)
				g.drawImage(images[face], x, y, null);
		}
		
		DieFaceSprite(int x, int y) {
			this.x = x;
			this.y = y;
			images = new Image[3];
			try {
				images[SWORD] = ImageIO.read(new File("assets/roll sword.png"));
				images[BOW] = ImageIO.read(new File("assets/roll bow.png"));
				images[PANIC] = ImageIO.read(new File("assets/roll panic.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			face = BLANK;
		}
		
		int face;
		Image[] images;
		int x;
		int y;
	}
	
	private class RollAnimation extends Animation {
		public void start() {
			steps = 20;
		}

		public void stepAhead(int millis) {
			--steps;
			if (steps < 0)
				sprite.face = finalFace;
			else
				sprite.face = (int)(Math.random()*3);
		}

		public boolean isFinished() {
			return steps < 0;
		}
		
		private RollAnimation(DieFaceSprite sprite, int finalFace) {
			this.sprite = sprite;
			this.finalFace = finalFace;
		}
		
		private DieFaceSprite sprite;
		private int finalFace;
		private int steps;
	}
	
	private DieFaceSprite[] sprites;
	private Animation animation;
}
