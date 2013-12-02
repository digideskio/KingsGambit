package kingsgambit.view;

import java.awt.Graphics2D;

import jmotion.animation.Animation;
import jmotion.animation.FrameSet;
import jmotion.sprite.Sprite;
import jmotion.sprite.SpriteLayer;

public class Banner implements Sprite {

	public void render(Graphics2D g) {
		g.drawImage(frames.currentFrame(), x, y, null);
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public Animation displayAndPlay() {
		return new Animation() {
			public void stepAhead(int millis) {
				frames.advanceFrame();
				if (frames.isFinalFrame())
					layer.removeSprite(Banner.this);
			}
			
			public void start() {
				frames.beginSequence(0);
				layer.addSprite(Banner.this);
			}
			
			public boolean isFinished() {
				return frames.isFinalFrame();
			}
		};
	}
	
	public Banner(FrameSet frames, SpriteLayer layer) {
		this.frames = frames;
		this.layer = layer;
	}
	
	private int x;
	private int y;
	private FrameSet frames;
	private SpriteLayer layer;
}
