package entities;

import java.awt.*;
import java.util.List;
import java.util.*;

import misc.Vector2;
import world.Camera;
import world.Region;
import world.WorldObject;

public abstract class Entity extends WorldObject {
	private Vector2 dimension;
	private Vector2 velocity;
	private Region currentRegion;
	
	protected List<String> tags;
	
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
	
	public Region getRegion() {
		return this.currentRegion;
	}
	
	public void send(Region region, float x, float y) {
		region.remove(this);
		this.currentRegion = region;
		this.currentRegion.add(this);
		this.setPosition(x, y);
	}
	
	public List<String> getTags() {
		return tags;
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
