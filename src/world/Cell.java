package world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import display.Animation;
import display.textures.TexturePack;
import misc.ImageTools;
import misc.MathUtils;

public class Cell extends WorldObject {
	
	private Animation animation;
	private int orientation = ImageTools.ROTATE_0; //0 = 0deg, 1 = 90deg, 2 = 180deg, 3 = 270deg

	public Cell(int id, int rotation, float x, float y) {
		super(x,y,1.0f,1.0f);
		this.orientation = rotation;
		this.setVerticalHeight(WorldObject.MIN_HEIGHT); //all tiles are at the bottom level.
		this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		this.animation = TexturePack.current().getTileTexture(id).getAnimation().copy();
	}
	
	public void setRotation(int orientation) {
		this.orientation = orientation;
	}
	
	public void update(float dt) {
		this.rotate(0.01f);
		animation.animate(dt);
	}
	
	public BufferedImage getImage() {
		BufferedImage aniFrame = this.animation.getCurrentFrame();
		BufferedImage image = ImageTools.rotate(aniFrame, orientation);
		return image;
	}
		
	public float centerX() {
		return getX() + 0.5f;
	}
	
	public float centerY() {
		return getY() + 0.5f;
	}
}
