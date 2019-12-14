package world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import display.Animation;
import entities.Entity;
import entities.EntityObject;
import entities.player.Player;
import entities.vehicles.Car;
import game.Game;
import entities.Portal;
import entities.SampleEntity;
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
		GRASS = ImageTools.convertTo8Bit(ImageTools.getImage("grass.png")),
		WATER = ImageTools.convertTo8Bit(ImageTools.getImage("water.png")),
		TILE  =	ImageTools.convertTo8Bit(ImageTools.getImage("tile.jpg"));
	
	public World(Game game) {
		this.game = game;
		regions = new ArrayList<Region>();
		int width = 30, height = 20;
		Region temp = new Region(this,"sample_world/regions/reg-0");
		
		for (int x = 0; x < temp.getWidth(); x+=2) {
			for (int y = 0; y < temp.getHeight(); y+=2) {
				temp.add(new SampleEntity(x,y));
			}
		}
		
		temp.add(new Player(temp.getWidth()/2.0f,temp.getHeight()/2.0f));
		temp.add(new Car(temp.getWidth()/2.0f+4,temp.getHeight()/2.0f));
		
		float r = 8.0f;
		int vertices = 60;
		double inc = (1.0/vertices*Math.PI*2);
		for (double t = 0.4; t < Math.PI*2; t+=inc) {
			float x1 = (float)Math.cos(t)*r+temp.getWidth()/2, y1 = (float)Math.sin(t)*r+temp.getHeight()/2;
			float x2 = (float)Math.cos(t+inc)*r+temp.getWidth()/2, y2 = (float)Math.sin(t+inc)*r+temp.getHeight()/2;
			temp.addWall(new Line(new Vector2(x1,y1), new Vector2(x2,y2)));
		}
		
		Region other = new Region(this,width,height);
		CellGrid grid = other.getGrid();
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
		float worldViewWidth = 13.0f;
		camera = new Camera(getCurrentRegion(), 0, 0, worldViewWidth, worldViewWidth/(cameraWidth/(float)cameraHeight),cameraWidth,cameraHeight);
		camera.setFocus(getPlayers().get(0));
	}
	
	public void loadFromFile() {
		
	}
	
	public void saveToFile() {
		
	}
	
	/**
	 * Returns where the mouse is in on the world
	 * @return
	 */
	public Vector2 getMousePositionOnWorld() {
		Vector2 onCam = game.getPercentMousePositionOnCamera();
		float x = camera.getX()+onCam.x*camera.getWidth(), 
			  y = camera.getY()+onCam.y*camera.getHeight();
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
