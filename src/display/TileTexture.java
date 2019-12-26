package display;

import java.awt.image.BufferedImage;
import java.util.List;

public class TileTexture {
	private Animation animation;
	private String stringID;
	
	public TileTexture(BufferedImage image, String stringID) {
		this.animation = new Animation(image);
		this.stringID = stringID;
	}
	
	public TileTexture(List<BufferedImage> images, int frameRate, String stringID) {
		this.animation = new Animation(images,frameRate);
		this.stringID = stringID;
	}
	
	public TileTexture(Animation animation, String stringID) {
		this.animation = animation;
		this.stringID = stringID;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public String getStringID() {
		return stringID;
	}
}
