package display.textures;

import java.awt.image.BufferedImage;
import java.util.List;

import display.Animation;
import misc.MathUtils;
import misc.Vector2;

public class Texture {
	/*
	 * Note the integer id is stored as the value in the hash map.
	 */
	
	private Vector2 dimension;
	private Animation animation;
	private String stringID;
	private float lightEmission = 0.0f;
	private float verticalHeight = 0.0f;
	
	public Texture(BufferedImage image, String stringID) {
		this(image, 1.0f, 1.0f, stringID);
	}
	
	public Texture(List<BufferedImage> images, int frameRate, String stringID) {
		this(images,frameRate, 1.0f, 1.0f,stringID);
	}
	
	public Texture(BufferedImage image, float width, float height, String stringID) {
		this(new Animation(image),width,height,stringID);
	}
	
	public Texture(List<BufferedImage> images, int frameRate, float width, float height, String stringID) {
		this(new Animation(images,frameRate),width,height,stringID);
	}
	
	public Texture(Animation animation, String stringID) {
		this(animation,1.0f,1.0f,stringID);	
	}
	
	public Texture(Animation animation, float width, float height, String stringID) {
		this.animation = animation;
		this.stringID = stringID;
		this.dimension = new Vector2(width,height);
	}
	
	public Texture setLightEmission(float value) {
		this.lightEmission  = MathUtils.clip(0, 1, value);
		return this;
	}
	
	public Texture setVerticalHeight(float value) {
		this.verticalHeight = value;
		return this;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public Animation createAnimation() {
		return this.animation.copy();
	}
	
	public String getStringID() {
		return stringID;
	}
	
	public float getLightEmissionValue() {
		return this.lightEmission;
	}
	
	public float getVerticalHeight() {
		return this.verticalHeight;
	}
	
	public Vector2 getDimension() {
		return this.dimension;
	}
	
	public float getWidth() {
		return dimension.x;
	}
	
	public float getHeight() {
		return dimension.y;
	}
}
