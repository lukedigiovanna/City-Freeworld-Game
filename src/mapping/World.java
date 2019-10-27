package mapping;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import misc.ImageTools;

public class World {
	private List<Region> regions;
	private int currentRegion;
	private Camera camera;
	
	public World() {
		regions = new ArrayList<Region>();
		int width = 30, height = 20;
		Region temp = new Region(width,height);
		CellGrid grid = temp.getGrid();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell c = new Cell();
				c.setImage(ImageTools.getBufferedImage("grass.png"));
				grid.set(x, y, c);
			}
		}
		regions.add(temp);
		camera = new Camera(0,0);
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public Region getCurrentRegion() {
		return regions.get(currentRegion);
	}
}
