package world;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.misc.EntityObject;
import entities.npcs.NPC;
import entities.player.Player;
import entities.vehicles.Car;
import game.Game;
import misc.*;

public class World {
	private Game game;
	private Camera camera;
	private List<Region> regions;
	private int currentRegion;
	
	private float elapsedTime = 0.0f;
	
	private String worldName = "krzworld";
	
	private float timeOfDay = 0.0f; //up to hour 23, at 24 it resets to 0.
	private int elapsedDays = 0;
	
	public World(Game game) {
		this.game = game;
		regions = new ArrayList<Region>();

		Region temp = new Region(this,worldName,0);

		temp.add(new Player(temp.getWidth()/2.0f,temp.getHeight()/2.0f));
		temp.add(new Car(Car.Model.RED_CAR,temp.getWidth()/2.0f+4,temp.getHeight()/2.0f));
		for (int i = 0; i < 7; i++)
		temp.add(new NPC(10+MathUtils.random(-3f,3f),10+MathUtils.random(-3f,3f)));
		
		regions.add(temp);
		
		regions.add(new Region(this,worldName,1));
		regions.add(new Region(this,worldName,2));
		regions.add(new Region(this,worldName,3));
		regions.add(new Region(this,worldName,4));
		
		//initialize starting region
		getCurrentRegion().update(0);
		
		int cameraWidth = Game.CAMERA_PIXEL_WIDTH, 
				    cameraHeight = Game.CAMERA_PIXEL_HEIGHT;
		float worldViewWidth = 13.0f;
		camera = new Camera(getCurrentRegion(), 0, 0, worldViewWidth, worldViewWidth/(cameraWidth/(float)cameraHeight),cameraWidth,cameraHeight);
		camera.setFocus(getPlayers().get(0));
	}
	
	public Game getGame() {
		return this.game;
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
	
	/**
	 * Returns the a value between [0,1] that reflects
	 * the time of day.
	 * Uses a sinusoidal function that mimics the day-night light cycle
	 * Brightest at around 1:30 PM and Darkest at 1:30 AM
	 * @return
	 */
	public float getGlobalLightValue() {
		//12 = 1.0f
		return (float) (Math.sin(this.timeOfDay * Math.PI / 12 - 2) * 0.4 + 0.6);
	}
	
	public void update(float dt) {
		elapsedTime += dt;
		
		//where 1 second = 1/60 of an hour.
		//timeOfDay += (dt/60.0f);

		timeOfDay+=dt;
		
		if (timeOfDay >= 24.0f) {
			elapsedDays++;
			timeOfDay%=24.0f;
		}
		
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
	
	public String getStringTime() {
		int hour = (int)this.timeOfDay;
		int minute = (int)(this.timeOfDay%1.0f*60);
		int hour12 = hour%12;
		if (hour12 == 0)
			hour12 = 12;
		String h = hour12+"";
		if (hour12 < 10)
			h = "0"+h;
		String m = minute+"";
		if (minute < 10)
			m = "0"+m;
		String time = h+":"+m;
		if (hour >= 12)
			time+=" PM";
		else
			time+=" AM";
		return time;
	}
}
