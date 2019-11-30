package world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import display.Animation;
import entities.Entity;
import entities.EntityObject;
import entities.player.Player;
import game.Game;
import entities.Portal;
import main.Program;
import misc.ImageTools;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;

public class World {
	private Game game;
	private Camera camera;
	private List<Region> regions;
	private int currentRegion;
	
	private float elapsedTime = 0.0f;
	
	private static final BufferedImage 
		GRASS = ImageTools.convertTo8Bit(ImageTools.getBufferedImage("grass.png")),
		WATER = ImageTools.convertTo8Bit(ImageTools.getBufferedImage("water.png")),
		TILE  =	ImageTools.convertTo8Bit(ImageTools.getBufferedImage("tile.jpg"));
	
	public World(Game game) {
		this.game = game;
		regions = new ArrayList<Region>();
		int width = 30, height = 20;
		Region temp = new Region(this,width,height);
		CellGrid grid = temp.getGrid();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell c = new Cell((float)x,(float)y);
//				if (Math.random() < 0.75)
//					c.setImage(GRASS);
//				else 
//					c.setImage(WATER);
				c.setImage(GRASS);
				grid.set(x, y, c);
			}
		}
		
		BufferedImage tree = ImageTools.getBufferedImage("twee.png");
		List<BufferedImage> redthing = ImageTools.getImages("redthing", "");
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				temp.add(new EntityObject(new Animation(redthing,14),x, y,1.0f,1.0f));

		temp.add(new Player(4.0f,4.0f));
		
		for (int i = 0; i < 50; i++) {
			float len = MathUtils.random(1.0f,4.0f);
			Vector2 ep1 = new Vector2(MathUtils.random(width),MathUtils.random(height));
			float angle = MathUtils.random((float)Math.PI*2);
			Vector2 ep2 = new Vector2(ep1.x+len*(float)Math.cos(angle),ep1.y+len*(float)Math.sin(angle));
			temp.addWall(new Line(ep1,ep2));
		}
		
		Region other = new Region(this,width,height);
		grid = other.getGrid();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell c = new Cell((float)x,(float)y);
				c.setImage(WATER);
				grid.set(x, y, c);
			}
		}
		
		Region another = new Region(this,width,height);
		grid = another.getGrid();
		for (int x = 0; x < width; x++) 
			for (int y = 0; y < height; y++) {
				Cell c = new Cell((float)x,(float)y);
				c.setImage(TILE);
				grid.set(x, y, c);
			}
		
		other.add(new Portal(new Portal.Destination(another,5.0f,2.0f),5.0f,2.0f,0.5f,0.5f));
		temp.add(new Portal(new Portal.Destination(other, 3.0f, 3.0f),5.0f,7.0f,0.5f,0.5f));
		another.add(new Portal(new Portal.Destination(temp,1.0f,7.0f),1.0f,7.0f,0.2f,0.2f));
		
		regions.add(temp);
		regions.add(other);
		regions.add(another);
		
		getCurrentRegion().update(0);
		
		int cameraWidth = Game.CAMERA_PIXEL_WIDTH, 
				    cameraHeight = Game.CAMERA_PIXEL_HEIGHT;
		float worldViewWidth = 10.0f;
		camera = new Camera(getCurrentRegion(), 0, 0, worldViewWidth, worldViewWidth/(cameraWidth/(float)cameraHeight),cameraWidth,cameraHeight);
		camera.setFocus(getPlayers().get(0));
	}
	
	/**
	 * Returns where the mouse is in on the world
	 * @return
	 */
	public Vector2 getMousePositionOnWorld() {
		Vector2 onCam = game.getPercentMousePositionOnCamera();
		float x = onCam.x*camera.getWidth(), 
			  y = onCam.y*camera.getHeight();
		return new Vector2(x,y);
	}
	
	public Region getCurrentRegion() {
		return regions.get(currentRegion);
	}
	
	private int newRegion = -1;
	public void setCurrentRegion(Region region) {
		newRegion = regions.indexOf(region);
	}
	
	public void update(float dt) {
		elapsedTime += dt;
		getCurrentRegion().update(dt);
		if (newRegion > -1) {
			currentRegion = newRegion;
			camera.linkToRegion(getCurrentRegion());
			newRegion = -1;
		}
		camera.adjustPosition(dt);
	}
	
	public void draw() {
		camera.draw();
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public List<Player> getPlayers() {
		List<Entity> entities = getCurrentRegion().getEntities().get("player");
		List<Player> players = new ArrayList<Player>();
		for (Entity e : entities)
			players.add((Player)e);
		return players;
	}
	
	public float getElapsedTime() {
		return elapsedTime;
	}
}
