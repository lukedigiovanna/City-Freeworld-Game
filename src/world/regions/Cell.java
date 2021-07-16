package world.regions;

import java.awt.image.BufferedImage;

import display.Animation;
import display.textures.Texture;
import display.textures.TexturePack;
import misc.ImageTools;
import world.Properties;
import world.WorldObject;

public class Cell extends WorldObject {
	
	private transient Animation animation;
	private int orientation = ImageTools.ROTATE_0; //0 = 0deg, 1 = 90deg, 2 = 180deg, 3 = 270deg
	private boolean flipped = false;
	private transient Texture texture;
	private int id;
	
	public Cell(int id, int rotation, int flip, float x, float y) {
		super(x,y,1.0f,1.0f);
		this.orientation = rotation;
		this.setVerticalHeight(WorldObject.MIN_HEIGHT); //all tiles are at the bottom level.
		this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
		this.id = id;
		this.flipped = flip == 1;
		loadAssets();
	}
	
	public void loadAssets() { 
		this.texture = TexturePack.current().getTileTexture(id);
		this.animation = texture.createAnimation();
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public String getStringID() {
		return this.texture.getStringID();
	}
	
	public void setRotation(int orientation) {
		this.orientation = orientation;
	}
	
	public void update(float dt) {
		animation.animate(dt);
	}
	
	public BufferedImage getImage() {
		BufferedImage aniFrame = this.animation.getCurrentFrame();
		if (this.flipped)
			aniFrame = ImageTools.flipVertical(aniFrame);
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
