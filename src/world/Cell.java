package world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cell extends WorldObject {
	public static final int PIXEL_SIZE = 8;
	
	private BufferedImage image;
	private List<Attribute> attribs;
	private String typeName;
	
	public Cell(float x, float y) {
		super(x,y,1.0f,1.0f);
		attribs = new ArrayList<Attribute>();
	}
	
	public void update(float dt) {
		
	}
	
	public boolean has(Attribute attrib) {
		for (Attribute a : attribs)
			if (attrib == a)
				return true;
		return false;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void addAttrib(Attribute attrib) {
		attribs.add(attrib);
	}
	
	public void setTypeName(String name) {
		this.typeName = name;
	}
	
	public enum Attribute {
		COLLIDABLE
	}
	
	public float centerX() {
		return getX() + 0.5f;
	}
	
	public float centerY() {
		return getY() + 0.5f;
	}
}
