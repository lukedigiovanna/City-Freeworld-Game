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
	
	private String worldName = "krzworld";
	
	public World(Game game) {
		this.game = game;
		regions = new ArrayList<Region>();

		Region temp = new Region(this,worldName,0);
		
		for (int x = 0; x < temp.getWidth(); x+=2)
			for (int y = 0; y < temp.getHeight(); y+=2)
				temp.add(new EntityObject(0,x,y));
		
		temp.add(new Player(temp.getWidth()/2.0f,temp.getHeight()/2.0f));
		temp.add(new Car(Car.Model.RED_CAR,temp.getWidth()/2.0f+4,temp.getHeight()/2.0f));

		regions.add(temp);
		
		regions.add(new Region(this,worldName,1));
		
		regions.add(new Region(this,worldName,2));
		
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
	
	public Region getRegion(int number) {
		return regions.get(number);
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
