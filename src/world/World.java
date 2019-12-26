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
	
	private String worldName = "sample_world";
	
	public World(Game game) {
		this.game = game;
		regions = new ArrayList<Region>();

		Region temp = new Region(this,worldName,0);
		
		temp.add(new Player(temp.getWidth()/2.0f,temp.getHeight()/2.0f));
		temp.add(new Car(Car.Model.RED_CAR,temp.getWidth()/2.0f+4,temp.getHeight()/2.0f));
		
		Region other = new Region(this,worldName,1);
		
		Region another = new Region(this,worldName,2);
		
		other.add(new Portal(new Portal.Destination(another,5.0f,2.0f),5.0f,2.0f,0.5f,0.5f));
		temp.add(new Portal(new Portal.Destination(other, 3.0f, 3.0f),5.0f,7.0f,0.5f,0.5f));
		another.add(new Portal(new Portal.Destination(temp,1.0f,7.0f),1.0f,7.0f,0.2f,0.2f));
		
		regions.add(temp);
		regions.add(other);
		regions.add(another);
		
		//initialize starting region
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
