package world;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.Player;
import entities.Portal;
import entities.SampleEntity;
import main.Program;
import misc.ImageTools;

public class World {
	private Camera camera;
	private List<Region> regions;
	private int currentRegion;
	
	public World() {
		regions = new ArrayList<Region>();
		int width = 30, height = 20;
		Region temp = new Region(this,width,height);
		CellGrid grid = temp.getGrid();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell c = new Cell((float)x,(float)y);
				if (Math.random() < 0.5)
					c.setImage(ImageTools.convertTo8Bit(ImageTools.getBufferedImage("grass.png")));
				else {
					c.setImage(ImageTools.convertTo8Bit(ImageTools.getBufferedImage("water.png")));
					c.addAttrib(Cell.Attribute.COLLIDABLE);
				}
				grid.set(x, y, c);
			}
		}
		temp.add(new SampleEntity(3.0f,3.0f));
		//temp.getEntities().add(new SampleEntity(3.3f,3.3f));
		
		temp.add(new Player(4.0f,4.0f));
		
		Region other = new Region(this,width,height);
		grid = other.getGrid();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell c = new Cell((float)x,(float)y);
				c.setImage(ImageTools.convertTo8Bit(ImageTools.getBufferedImage("water.png")));
				grid.set(x, y, c);
			}
		}
		
		other.add(new Portal(new Portal.Destination(temp,5.0f,2.0f),5.0f,2.0f,0.5f,0.5f));
		temp.add(new Portal(new Portal.Destination(other, 3.0f, 3.0f),5.0f,7.0f,0.5f,0.5f));
		
		regions.add(temp);
		regions.add(other);
		
		float sidePadding = 0.25f;
		int cameraWidth = (int)(Program.DISPLAY_WIDTH*(1-sidePadding)), 
				    cameraHeight = (int)(Program.DISPLAY_HEIGHT*(1-sidePadding));
		float worldViewWidth = 10.0f;
		camera = new Camera(0, 0, worldViewWidth, worldViewWidth/(cameraWidth/(float)cameraHeight));
		camera.linkToRegion(getCurrentRegion());
		camera.setFocus(getPlayer());
	}
	
	public Region getCurrentRegion() {
		return regions.get(currentRegion);
	}
	
	private int newRegion = -1;
	public void setCurrentRegion(Region region) {
		newRegion = regions.indexOf(region);
		System.out.println(newRegion);
	}
	
	public void update(float dt) {
		getCurrentRegion().update(dt);
		if (newRegion > -1) {
			currentRegion = newRegion;
			camera.linkToRegion(getCurrentRegion());
			newRegion = -1;
		}
	}
	
	public void draw() {
		camera.draw();
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public Player getPlayer() {
		List<Entity> players = getCurrentRegion().getEntities().get("player");
		if (players.size() > 0)
			return (Player)players.get(0);
		else
			return null;
	}
}
