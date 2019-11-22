package world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cell extends WorldObject {
	public static final int PIXEL_SIZE = 8;
	
	public static enum Type {
		WATER,
		PATHWAY,
		ROAD,
		DIRT,
		GRASS
	}
	
	private BufferedImage image;

	public Cell(float x, float y) {
		super(x,y,1.0f,1.0f);
	}
	
	public void update(float dt) {
		
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
		
	public float centerX() {
		return getX() + 0.5f;
	}
	
	public float centerY() {
		return getY() + 0.5f;
	}
}
