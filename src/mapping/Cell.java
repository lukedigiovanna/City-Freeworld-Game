package mapping;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cell {
	public static final int PIXEL_SIZE = 8;
	
	private BufferedImage image;
	private List<Attribute> attribs;
	private String typeName;
	
	public Cell() {
		attribs = new ArrayList<Attribute>();
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
		OBSTACLE,//entities cant walk throuh
		ROAD, //cars can travel on
		PATH;
	}
}
