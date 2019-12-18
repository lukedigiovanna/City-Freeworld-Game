package entities;

import java.awt.*;
import java.util.List;

import entities.vehicles.Vehicle;

import java.util.*;

import misc.Vector2;
import world.*;
import world.Properties;

public abstract class Entity extends WorldObject {
	protected Health health;
	
	protected Vehicle riding; //null to start
	
	private List<String> tags;
	private List<Path> paths;
	
	public Entity(float x, float y, float width, float height) {
		super(x,y,width,height);
		this.dimension = new Vector2(width,height);
		this.velocity = new Vector2(0,0,0);
		tags = new ArrayList<String>();
		tags.add("entity");
		paths = new ArrayList<Path>();
		this.health = new Health(1,1);
	}
	
	@Override
	public void generalUpdate(float dt) {
		super.generalUpdate(dt);
		if (this.paths.size() > 0) {
			paths.get(0).follow();
			if (paths.get(0).completed())
				paths.remove(0);
		}
		//entity general update.... just overrides the world object general update but calls that method
		if (this.health.isDead()) {
			this.destroy();
		}
	}
	
	public void send(Region region, float x, float y) {
		if (this.riding != null)
			this.riding.send(region, x, y);
		this.region.remove(this);
		this.region = region;
		this.region.add(this);
		this.setPosition(x, y);
		if (this.hasTag("player")) {
			this.region.getWorld().setCurrentRegion(this.region);
		}
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	/**
	 * Checks whether or not the entity contains a particular tag
	 * @param tag
	 * @return
	 */
	public boolean hasTag(String tag) {
		for (String t : tags) 
			if (tag.contentEquals(t))
				return true;
		return false;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void queuePath(Path path) {
		path.setEntity(this);
		this.paths.add(path);
	}
	
	/**
	 * Deals damage to the entity if it is not invulnerable
	 * @param amount
	 */
	public void hurt(float amount) {
		//only if we aren't invulnerable
		if (this.getProperty(Properties.KEY_INVULNERABLE) == Properties.VALUE_INVULNERABLE_FALSE)
			this.health.hurt(amount);
	}
	
	/**
	 * Removes reference of this entity from the entity list
	 */
	public void destroy() {
		if (this.getRegion() != null)
			this.getRegion().remove(this);
		this.destroyed = true;
	}
	
	private boolean destroyed = false;
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	/**
	 * Called by the draw method from a camera class
	 * This method should be written in terms of the 
	 * @param camera The camera to draw to
	 */
	public abstract void draw(Camera camera);
	
	/**
	 * Called by the game loop to run entity type specific logic
	 * Doesn't handle general logic like updating movement and collision checking
	 * @param dt - The amount of time passed in seconds since the last call of the method.
	 */
	public abstract void update(float dt);
}
