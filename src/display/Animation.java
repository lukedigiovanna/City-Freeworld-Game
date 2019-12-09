package display;

import java.util.*;
import java.util.List;

import misc.ImageTools;
import misc.MathUtils;

import java.awt.image.*;
import java.io.File;

public class Animation {
	private List<BufferedImage> frames;
	private int curFrame = 0;
	private int frameRate = 10; //frames per second
	private float timeCounter = 0.0f;
	
	/**
	 * Constructs the animation from a folder that contains every frame
	 * Each frame in the folder should be titled: prefix+"_"+index
	 * Where frame 1 is prefix_0, 2 is prefix_1 and so on.
	 * Also takes in an integer for the frame rate
	 */
	public Animation(String folderPath, String prefix, int frameRate) {
		this(ImageTools.getImages(folderPath, prefix),frameRate);
	}
	
	public Animation(List<BufferedImage> frames, int frameRate) {
		this.frames = frames;
		this.frameRate = frameRate;
	}
	
	
	/**
	 * Updates the current frame based on the elapsed time.
	 * @param dt
	 */
	public void animate(float dt) {
		timeCounter += dt;
		float period = 1.0f/frameRate;
		if (timeCounter >= period) {
			curFrame+=(int)(timeCounter/period);
			timeCounter%=period;
			if (curFrame > frames.size()-1)
				curFrame = 0;
		}
	}
	
	public BufferedImage getCurrentFrame() {
		if (frames.size() == 0)
			return ImageTools.IMAGE_NOT_FOUND;
		curFrame%=frames.size();
		return frames.get(curFrame);
	}
	
	public List<BufferedImage> getFrames() {
		return frames;
	}
	
	public int getFrameRate() {
		return this.frameRate;
	}
	
	public void randomize() {
		curFrame = MathUtils.random(frames.size());
	}
}
