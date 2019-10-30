package world;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import entities.SampleEntity;
import misc.ImageTools;

 public class World {
	private List<Region> regions;
	private int currentRegion;
	
	public World() {
		regions = new ArrayList<Region>();
		int width = 30, height = 20;
		Region temp = new Region(width,height);
		CellGrid grid = temp.getGrid();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell c = new Cell();
				if (Math.random() < 0.5)
					c.setImage(ImageTools.convertTo8Bit(ImageTools.getBufferedImage("grass.png")));
				else {
					c.setImage(ImageTools.convertTo8Bit(ImageTools.getBufferedImage("water.png")));
					c.addAttrib(Cell.Attribute.OBSTACLE);
				}
				grid.set(x, y, c);
			}
		}
		temp.getEntities().add(new SampleEntity(3.0f,3.0f));
		temp.getEntities().add(new SampleEntity(3.3f,3.3f));
		
		regions.add(temp);
	}
	
	public Region getCurrentRegion() {
		return regions.get(currentRegion);
	}
}
