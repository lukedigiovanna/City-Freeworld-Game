package world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import misc.ImageTools;

public class Cell extends WorldObject {
	public static final int PIXEL_SIZE = 16;
	
	public static enum Type {
		WATER,
		PATHWAY,
		ROAD,
		DIRT,
		GRASS
	}
	
	private BufferedImage image = ImageTools.IMAGE_NOT_FOUND;
	private Type type;

	public Cell(float x, float y) {
		super(x,y,1.0f,1.0f);
	}
	
	public void update(float dt) {
		
	}
	
	public void set(CellTemplate temp) {
		
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
