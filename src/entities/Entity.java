package entities;

import java.awt.*;
import java.util.List;

import entities.misc.TextParticle;
import entities.vehicles.Vehicle;

import java.util.*;

import misc.MathUtils;
import misc.Vector2;
import world.*;
import world.Properties;

public abstract class Entity extends WorldObject {
	protected Health health;
	
	protected Vehicle riding; //null to start
	
	private List<String> tags;
	private List<Path> paths;
	
	public Entity(float x, float y) {
		this(x,y,1.0f,1.0f);
	}
	
	public Entity(float x, float y, float width, float height) {
		super(x,y,width,height);
		setDimension(new Vector2(width,height));
		this.setVelocity(0,0,0);
		tags = new ArrayList<String>();
		addTag("entity");
		paths = new ArrayList<Path>();
		this.health = new Health(1,1);
	}
	
	@Override
	public void generalUpdate(float dt) {
		super.generalUpdate(dt);
		if (this.paths.size() > 0) {
			paths.get(0).follow(dt);
			if (paths.get(0).completed() || paths.get(0).stalled()) {
				paths.remove(0);
				this.getVelocity().zero();
			}
		}
		this.health.update(dt);
		if (this.health.isDead()) {
			this.destroy();
		}
	}
	
	public void send(Region region, float x, float y) {
		if (this.riding != null)
			this.riding.send(region, x, y);
		this.getRegion().remove(this);
		this.setRegion(region);
		region.add(this);
		this.setPosition(x, y);
		if (this.hasTag("player")) {
			this.getRegion().getWorld().setCurrentRegion(this.getRegion());
		}
	}
	
	public void send(int regionNumber, float x, float y) {
		Region destination = this.getWorld().getRegion(regionNumber);
		send(destination,x,y);
	}
	
	public Health getHealth() {
		return this.health;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	/**
	 * Checks if the entity is following a path
	 * @return
	 */
	public boolean isFollowingPath() {
		return paths.size() > 0;
	}
	
	/**
	 * Clears the path queue
	 */
	public void clearPaths() {
		this.paths.clear();
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
		if (this.getProperty(Properties.KEY_INVULNERABLE) == Properties.VALUE_INVULNERABLE_FALSE) {
			this.health.hurt(amount);
			this.getRegion().add(new TextParticle("-"+amount,Color.RED,getX(),getY(),0.25f));
		}
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
	
	private Color col = new Color(MathUtils.random(256),MathUtils.random(256),MathUtils.random(256));
	public void realDraw(Camera c) {
		c.setColor(col);
		c.fillRect(getX(),getY(),getWidth(),getHeight());
	}
	
	/**
	 * Called by the game loop to run entity type specific logic
	 * Doesn't handle general logic like updating movement and collision checking
	 * @param dt - The amount of time passed in seconds since the last call of the method.
	 */
	public abstract void update(float dt);
}
