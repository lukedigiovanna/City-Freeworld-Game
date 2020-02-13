package world;

import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.player.Player;
import world.regions.Region;

public class World {
	private List<Region> regions;
	private int currentRegion;
	
	private float elapsedTime = 0.0f;
	
	private String worldName;
	
	private float timeOfDay = 12.0f; //up to hour 23, at 24 it resets to 0.
	private int elapsedDays = 0;
	
	/**
	 * Instantiates a world from the specified world
	 * folder name.
	 * @param name The name of the world folder
	 */
	public World(String name) {
		this.worldName = name;
		
		regions = new ArrayList<Region>();
		
		//add in all the regions
		Region next;
		while ((next = Region.generateWorldRegion(this,worldName,regions.size())) != null)
			regions.add(next);
		
		//initialize starting region
		getCurrentRegion().update(0);
	}
	
	public Player addPlayer() {
		Region reg0 = this.getRegion(0);
		Player p = new Player(reg0.getWidth()/2,reg0.getHeight()/2);
		reg0.add(p);
		return p;
	}
	
	public Region getCurrentRegion() {
		return regions.get(currentRegion);
	}
	
	public Region getRegion(int number) {
		return regions.get(number);
	}
	
	public void setCurrentRegion(Region region) {
		currentRegion = regions.indexOf(region);
	}
	
	/**
	 * Returns the a value between [0,1] that reflects
	 * the time of day.
	 * Uses a sinusoidal function that mimics the day-night light cycle
	 * Brightest at around 1:30 PM and Darkest at 1:30 AM
	 * @return
	 */
	public float getGlobalLightValue() {
		return (float) (Math.sin(this.timeOfDay * Math.PI / 12 - 2) * 0.4 + 0.6);
	}
	
	public void update(float dt) {
		elapsedTime += dt;
		
		//where 1 second = 1/60 of an hour.
		timeOfDay += (dt/60.0f);

		//timeOfDay+=dt;
		
		if (timeOfDay >= 24.0f) {
			elapsedDays++;
			timeOfDay%=24.0f;
		}
		
		getCurrentRegion().update(dt);
	}
	
	public int getElapsedDays() {
		return this.elapsedDays;
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
