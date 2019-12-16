package world;

import java.util.List;

import entities.Entity;
import entities.player.Player;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;

/**
 * Super class for all objects that will be in the world : cells, entities, etc.
 *
 */
public abstract class WorldObject {
	public static final float MIN_HEIGHT = 0, MAX_HEIGHT = 25;
	
	protected PositionHistory positionHistory;
	protected Region region;
	protected Vector2 position, velocity;
	protected float height; //the z-axis of position.. follows same unit scale as the x and y axis
	protected Hitbox hitbox; //identifies the physical boundaries with walls
	protected float mass; //in kilograms
	protected Properties properties;
	
	protected float age;
	
	private static final float regenPeriod = 30.0f;
	private float regenTimer = MathUtils.random(regenPeriod);
	
	public WorldObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y,0);
		this.height = MIN_HEIGHT; //by default
		this.velocity = new Vector2(0,0,0); //initial velocity is 0
		float[] model = {0.0f,0.0f,width,0.0f,width,height,0.0f,height};
		this.hitbox = new Hitbox(this, model);
		//this staggers regeneration so not every object regenerates its hitbox at the same time
		// *reduces the chance of a lag spike
		this.properties = new Properties();
		this.positionHistory = new PositionHistory(this);
	}
	
	public void setModel(float ... model) {
		this.hitbox = new Hitbox(this, model);
	}
	
	public float getHeight() {
		return this.height;
	}
	
	/**
	 * Sets the height of the object
	 * Limits it between the minimum and maximum heights
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = MathUtils.clip(MIN_HEIGHT, MAX_HEIGHT, height);
	}
	
	/**
	 * Gets the vector typed velocity
	 * @return
	 */
	public Vector2 getVelocity() {
		return this.velocity;
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
		}
		
		//update the position history..
		this.positionHistory.update(dt);
		
		this.move(dt);
		
		age += dt;
	}
	
	public void regenerateHitbox() {
		if (this.properties.get(Properties.KEY_REGENERATE_HITBOX) == Properties.VALUE_REGENERATE_HITBOX_TRUE)
			this.hitbox.generateLines();
	}
	
	public World getWorld() {
		return this.region.getWorld();
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public Vector2 getPosition() {
		return this.position;
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
	
	public void setVelocity(float vx, float vy) {
		this.velocity.x = vx;
		this.velocity.y = vy;
	}
	
	public void setVelocity(Vector2 vel) {
		this.velocity = vel;
	}
	
	public void rotate(float radians) {
//		hitbox.rotate(radians);
		position.r += radians;
	}
	
	/**
	 * Returns the angle between the center of the two objects
	 * @param other
	 * @return
	 */
	public float angleTo(WorldObject other) {
		return angleTo(other.centerX(),other.centerY());
	}
	
	/**
	 * Gets the angle between the center of this object
	 * to the vector formatted point input
	 * @param point
	 * @return
	 */
	public float angleTo(Vector2 point) {
		return angleTo(point.x,point.y);
	}
	
	/**
	 * Returns the angle from the center of this object to
	 * the point inputed.
	 * @param x
	 * @param y
	 * @return
	 */
	public float angleTo(float x, float y) {
		return (float)MathUtils.getAngle(centerX(), centerY(), x, y);
	}
	
	public void drawHitbox(Camera c) {
		hitbox.draw(c);
	}
	
	/**
	 * Moves the object based on its instantaenous velocity
	 * Will stop if it hits a wall
	 * @param dt
	 */
	public void move(float dt) {
		if (velocity.x == 0 && velocity.y == 0 && velocity.r == 0)
			return; //no movement.. so quit
		int intervals = 10;
		float step = dt/intervals;
		List<Line> walls = this.getRegion().getWalls().getWalls();
		Vector2 intersection;
		boolean cont = true;
		for (int i = 0; i < intervals; i++) {
			move(step*velocity.x,step*velocity.y,step*velocity.r);
			//now check for collision
			for (Line l : walls) {
				intersection = this.hitbox.intersecting(l);
				if (intersection != null) {
					//we done
					cont = false;
					move(-step*velocity.x,-step*velocity.y,-step*velocity.r);
					this.velocity.zero();
					break; //out of the line for loop
				}
			}
			if (cont == false)
				break;
		}
		
	}
	
	/**
	 * Moves the entities position based on the delta x and delta y inputs
	 * @param dx distance to change x
	 * @param dy distance to change y
	 * @param dr amount of rotation about the midpoint of this object
	 */
	public void move(float dx, float dy, float dr) {	
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
	 * Checks if the hitbox lines are colliding with each other
	 * And returns at what point the lines intersect.
	 * This is method is not recommended for determining whether or not two
	 * objects are colliding. For that use the colliding method which takes
	 * advantage of SAT.
	 * @param other
	 * @return
	 */
	public Vector2 collisionPosition(WorldObject other) {
		return this.hitbox.intersecting(other.hitbox);
	}
	
	/**
	 * Uses Separate Axis Theorem for determining whether or
	 * not the two hitboxes are colliding. The actual method logic
	 * is used as Hitbox.this.satIntersecting(Hitbox other)
	 * @param other
	 * @return
	 */
	public boolean colliding(WorldObject other) {
		//makes use of sat from hitbox
		return this.hitbox.satIntersecting(other.hitbox);
	}
	
	/**
	 * Gets the active player of the game
	 * @return the player
	 */
	public List<Player> getPlayers() {
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
	
	public Properties.Value getProperty(Properties.Key key) {
		return properties.get(key);
	}
}
