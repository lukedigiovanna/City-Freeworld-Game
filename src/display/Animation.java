package display;

import java.util.*;
import java.util.List;
import java.awt.image.*;

public class Animation {
	private List<BufferedImage> frames;
	private int curFrame = 0;
	private int frameRate = 10; //frames per second
	private float timeCounter = 0.0f;
	
	public Animation() {
		
	}
	
	public void animate(float dt) {
		timeCounter += dt;
		float period = 1.0f/frameRate;
		if (timeCounter >= period) {
			timeCounter%=period;
			curFrame++;
			if (curFrame > frames.size()-1)
				curFrame = 0;
		}
	}
	
	public BufferedImage getCurrentFrame() {
		return frames.get(curFrame);
	}
}
