package world;

import entities.Entity;
import misc.Vector2;

/**
 * Super class for all objects that will be in the world : cells, entities, etc.
 *
 */
public abstract class WorldObject {
	protected PositionHistory positionHistory;
	protected Region region;
	protected Vector2 position;
	protected Hitbox hitbox;
	
	protected float age;
	
	private static final float regenPeriod = 0.45f;
	private float regenTimer = 0.0f;
	
	public WorldObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y,0);
		float[] model = {0.0f,0.0f,width,0.0f,width,height,0.0f,height};
		this.hitbox = new Hitbox(this, model);
		//this staggers regeneration so not every object regenerates its hitbox at the same time
		// *reduces the chance of a lag spike
		regenTimer = (float)Math.random()*regenPeriod; 
		this.positionHistory = new PositionHistory(this);
	}
	
	/**
	 * An update method for logic that every single world object
	 * should have
	 * @param dt amount of time in seconds since last call
	 */
	public void generalUpdate(float dt) {
		//logic for regenerating a hitbox
		//this is needed because sometimes the hitbox and entity's rotation fall out of sync
		regenTimer += dt;
		
		if (regenTimer >= regenPeriod) {
			regenerateHitbox();
			regenTimer = 0;
		}
		
		//update the position history..
		this.positionHistory.update(dt);
		
		age += dt;
	}
	
	public void regenerateHitbox() {
		this.hitbox.generateLines();
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getRotation() {
		return position.r;
	}
	
	public float centerX() {
		return getX() + 0.5f;
	}
	
	public float centerY() {
		return getY() + 0.5f;
	}
	
	public Vector2 center() {
		return new Vector2(centerX(),centerY());
	}
	
	public void rotate(float radians) {
		hitbox.rotate(radians);
		position.r += radians;
	}
	
	public void drawHitbox(Camera c) {
		hitbox.draw(c);
	}
	
	/**
	 * Moves the entities position based on the delta x and delta y inputes
	 * @param dx distance to change x
	 * @param dy distance to change y
	 */
	public void move(float dx, float dy) {
		setPosition(getX()+dx,getY()+dy);
	}
	
	/**
	 * Sets the position given two floats
	 * @param x x-coord
	 * @param y y-coord
	 */
	public void setPosition(float x, float y) {
		setPosition(new Vector2(x,y,position.r));
		
	}
	
	/**
	 * Sets the current position from a vector
	 * ALL CHANGES TO POSITION SHOULD RUN THROUGH THIS METHOD
	 * for example: when adding velocity it should call setPosition(x+vx,y+vy)
	 * @param pos the vector
	 */
	public void setPosition(Vector2 pos) {
		hitbox.translate(pos.x-position.x,pos.y-position.y);
		this.position = pos;
		//check for collision with the grid
		//lets look at the cells that the entity is overlapping with..
		//we will be using the hitbox of the entity to check for collision
	}
	
	/**
	 * Checks if the hitboxes are colliding with each other
	 * @param other
	 * @return
	 */
	public boolean colliding(WorldObject other) {
		return this.hitbox.intersecting(other.hitbox);
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Region getRegion() {
		return region;
	}
}
