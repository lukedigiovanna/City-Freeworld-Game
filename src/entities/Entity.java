package entities;

import java.awt.*;
import java.util.List;
import java.util.*;

import misc.Vector2;
import world.Camera;
import world.Region;
import world.WorldObject;

public abstract class Entity extends WorldObject {
	protected Vector2 dimension;
	protected Vector2 velocity;
	
	private List<String> tags;
	
	public static enum Attribs {
		INVULNERABLE,
		NO_COLLISION,
		
	}
	
	public Entity(float x, float y, float width, float height) {
		super(x,y,width,height);
		this.dimension = new Vector2(width,height);
		tags = new ArrayList<String>();
		tags.add("entity");
	}
	
	@Override
	public void generalUpdate(float dt) {
		super.generalUpdate(dt);
		//entity general update.... just overrides the world object general update but calls that method
	}
	
	public void send(Region region, float x, float y) {
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
	
	public float centerX() {
		if (dimension == null)
			return 0; //fuck you
		return getX() + dimension.x/2;
	}
	
	public float centerY() {
		if (dimension == null)
			return 0;
		return getY() + dimension.y/2;
	}
	
	public float getWidth() {
		return dimension.x;
	}
	
	public float getHeight() {
		return dimension.y;
	}
	
	/**
	 * Called by the draw method from a camera class
	 * This method should be written in terms of the 
	 * @param g
	 */
	public abstract void draw(Camera camera);
	
	/**
	 * Called by the game loop to run entity type specific logic
	 * Doesn't handle general logic like updating movement and collision checking
	 * @param dt - The amount of time passed in seconds since the last call of the method.
	 */
	public abstract void update(float dt);
}
