package world;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import misc.ImageTools;

public class CellTemplate {
	
	private static CellTemplate[] cellTemps;
	
	public static void initialize(String jsonPath) {
		cellTemps = new CellTemplate[256]; //256 possible ones
		cellTemps[0] = new CellTemplate(ImageTools.getBufferedImage("grass.png"),1,"grass",0);
		cellTemps[1] = new CellTemplate(ImageTools.getBufferedImage("water.png"),1,"water",1);
	}
	
	public static CellTemplate get(int index) {
		return cellTemps[index];
	}
	
	private List<BufferedImage> images;
	private int frameRate;
	private String stringID;
	private int id;
	
	public CellTemplate(BufferedImage image, int frameRate, String sID, int id) {
		this.images = new ArrayList<BufferedImage>();
		images.add(image);
		this.frameRate = frameRate;
		this.stringID = sID;
		this.id = id;
	}
	
	public List<BufferedImage> getImages() {
		return images;
	}
	
	public int getFrameRate() {
		return frameRate;
	}
	
	public String getStringID() {
		return stringID;
	}
	
	public int getID() {
		return id;
	}
}
