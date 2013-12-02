package kingsgambit.view.battle;

import jmotion.animation.Animation;
import jmotion.animation.FrameSet;

public class PlayFramesThenReset extends Animation {

	int numSteps;
	public void stepAhead(int millis) {
		if (lastFrame) {
			finished = true;
			frameSet.beginSequence(reset);
		} else if (!firstFrame) {
			frameSet.advanceFrame();
			if (frameSet.isFinalFrame())
				lastFrame = true;
		}
		firstFrame = false;
	}

	public void start() {
		firstFrame = true;
		lastFrame = false;
		frameSet.beginSequence(sequence);
	}
	
	public boolean isFinished() {
		return finished;
	}

	public PlayFramesThenReset(FrameSet animation, int sequence, int reset) {
		this.frameSet = animation;
		this.sequence = sequence;
		this.reset = reset;
	}

	private boolean firstFrame;
	private boolean lastFrame;
	private FrameSet frameSet;
	private boolean finished;
	private int sequence;
	private int reset;
}
