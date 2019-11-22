package world;

import java.util.List;

import entities.Entity;
import entities.Particle;
import entities.Player;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;

/**
 * Super class for all objects that will be in the world : cells, entities, etc.
 *
 */
public abstract class WorldObject {
	protected PositionHistory positionHistory;
	protected Region region;
	protected Vector2 position, velocity;
	protected Hitbox hitbox;
	protected Properties properties;
	
	protected float age;
	
	private static final float regenPeriod = 5.0f;
	private float regenTimer = 0.0f;
	
	public WorldObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y,0);
		this.velocity = new Vector2(0,0,0);
		float[] model = {0.0f,0.0f,width,0.0f,width,height,0.0f,height};
		this.hitbox = new Hitbox(this, model);
		//this staggers regeneration so not every object regenerates its hitbox at the same time
		// *reduces the chance of a lag spike
		regenTimer = (float)Math.random()*regenPeriod; 
		regenTimer = 0;
		this.properties = new Properties();
		this.positionHistory = new PositionHistory(this);
	}
	
	public void setModel(float ... model) {
		this.hitbox = new Hitbox(this, model);
	}
	
	/**
	 * An update method for logic that every single world object
	 * should have
	 * @param dt amount of time in seconds since last call
	 */
	public void generalUpdate(float dt) {
		//if the region is gone.. this entity effectively doesn't exist. dont update
		if (this.getRegion() == null)
			return;
		
		//logic for regenerating a hitbox
		//this is needed because sometimes the hitbox and entity's rotation fall out of sync
		regenTimer += dt;
		
		if (regenTimer >= regenPeriod) {
			regenTimer = 0;
			regenerateHitbox();
			System.out.println(regenTimer+"/"+regenPeriod);
		}
		
		//update the position history..
		this.positionHistory.update(dt);
		
		this.move(velocity.x*dt, velocity.y*dt, velocity.r*dt);
		
		age += dt;
	}
	
	public void regenerateHitbox() {
		if (this.properties.get(Properties.KEY_REGENERATE_HITBOX) == Properties.VALUE_REGENERATE_HITBOX_TRUE)
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
	
	public float angleTo(WorldObject other) {
		return (float)MathUtils.getAngle(centerX(), centerY(), other.centerX(), other.centerY());
	}
	
	public void drawHitbox(Camera c) {
		hitbox.draw(c);
	}
	
	/**
	 * Moves the entities position based on the delta x and delta y inputes
	 * @param dx distance to change x
	 * @param dy distance to change y
	 * @param dr amount of rotation about the midpoint of this object
	 */
	public void move(float dx, float dy, float dr) {
		if (dx == 0 && dy == 0 && dr == 0)
			return; //no point in doing anything if we dont want to move.
		
		//if we dont have collision there is no need to test for it when we move.
		if (this.properties.get(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE) {
			rotate(dr);
			setPosition(getX()+dx,getY()+dy);
			return;
		}
		
		//need to modify these dx, dy in correspondence with walls
		List<Line> walls = this.getRegion().getWalls().getWalls();
		Vector2[] eps = this.hitbox.getVertices();
		
		for (Vector2 ep1 : eps) {
			Vector2 ep2 = new Vector2(ep1.x+dx,ep1.y+dy);
			Line l = new Line(ep1,ep2);
			for (Line wall : walls) {
				Vector2 intersection = l.intersects(wall);
				if (intersection == null)
					continue;
//				if (dx < 0)
//					dx = intersection.x-ep1.x+0.01f;
//				else if (dx > 0)
//					dx = intersection.x-ep1.x-0.01f;
//				if (dy < 0)
//					dy = intersection.y-ep1.y+0.01f;
//				else if (dy > 0)
//					dy = intersection.y-ep1.y-0.01f;
				dx = intersection.x-ep1.x-MathUtils.sign(dx)*0.01f; 
				dy = intersection.y-ep1.y-MathUtils.sign(dy)*0.01f;
			}
		}
		
		rotate(dr);
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
	
	/**
	 * Gets the active player of the game
	 * @return the player
	 */
	public List<Entity> getPlayers() {
		return region.getWorld().getPlayers();
	}
	
	/**
	 * Gets the difference between the centers of the two objects
	 * @param other
	 * @return
	 */
	public float distanceTo(WorldObject other) {
		if (other == null)
			return MathUtils.INFINITY;
		return MathUtils.distance(centerX(),centerY(),other.centerX(),other.centerY());
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Region getRegion() {
		return region;
	}
}
