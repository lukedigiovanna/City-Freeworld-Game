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
	
	private PositionHistory positionHistory;
	private Region region;
	private Vector2 position, velocity, dimension;
	private float verticalHeight; //the z-axis of position.. follows same unit scale as the x and y axis
	private Hitbox hitbox; //identifies the physical boundaries with walls
	private float mass; //in kilograms
	private Properties properties;
	
	private float lightValue = 1.0f; //value from 0 - 1 that indicates light, 0 is pitch black, 1 is bright
	private float lightEmission = 0.0f; //value that indicates distance of light production
	
	private float age; //the number of real seconds the object has existed in the world
	
	private static final float regenPeriod = 30.0f;
	private float regenTimer = MathUtils.random(regenPeriod);
	
	public WorldObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y,0);
		this.verticalHeight = MIN_HEIGHT; //by default
		this.velocity = new Vector2(0,0,0); //initial velocity is 0
		this.setDimension(width, height);
		this.setHitboxToDimension();
		//this staggers regeneration so not every object regenerates its hitbox at the same time
		// *reduces the chance of a lag spike
		this.properties = new Properties();
		this.positionHistory = new PositionHistory(this);
	}
	
	public void setModel(float ... model) {
		this.hitbox = new Hitbox(this, model);
	}
	
	public void setProperty(Properties.Key key, Properties.Value value) {
		this.properties.set(key, value);
	}
	
	public float getVerticalHeight() {
		return this.verticalHeight;
	}
	
	/**
	 * Sets the height of the object
	 * Limits it between the minimum and maximum heights
	 * @param height
	 */
	public void setVerticalHeight(float height) {
		this.verticalHeight = MathUtils.clip(MIN_HEIGHT, MAX_HEIGHT, height);
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
		
		this.position.round(0.005f);
		
		//logic for regenerating a hitbox
		//this is needed because sometimes the hitbox and entity's rotation fall out of sync
		regenTimer += dt;
		
		if (regenTimer >= regenPeriod) {
			regenTimer = 0;
			regenerateHitbox();
		}
		
		//update the position history..
		this.positionHistory.update(dt);
		
		//move with the current velocity
		this.move(dt);
		
		//collision correction
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_TRUE)
			this.correctOutOfWalls();
		
		updateLightValue();
		
		age += dt;
	}
	
	public void updateLightValue() {
		float thisVal = this.getRegion().getWorld().getGlobalLightValue();
		for (Entity e : this.getRegion().getEntities().get()) {
			if (e.getLightEmissionValue() > 0) {
				float d = this.squaredDistanceTo(e);
				float light = (float) (-2/(1+Math.pow(2, -0.2 * d))+2);
				thisVal += light * e.getLightEmissionValue();
			}
		}
		thisVal = MathUtils.clip(0, 1, thisVal);
		this.setLightValue(thisVal);
	}
	
	public float getLightEmissionValue() {
		return this.lightEmission;
	}
	
	public void setLightEmissionValue(float val) {
		this.lightEmission = MathUtils.clip(0,1,val);
	}
	
	/**
	 * Uses the dimensional width and height to create a hitbox
	 * that follows those dimensions for the object.
	 */
	public void setHitboxToDimension() {
		float[] model = {0.0f,0.0f,getWidth(),0.0f,getWidth(),getHeight(),0.0f,getHeight()};
		this.hitbox = new Hitbox(this, model);
	}
	
	public void regenerateHitbox() {
		if (this.properties.get(Properties.KEY_REGENERATE_HITBOX) == Properties.VALUE_REGENERATE_HITBOX_TRUE)
			this.hitbox.generateLines();
	}
	
	public World getWorld() {
		return this.region.getWorld();
	}
	
	public float getAge() {
		return this.age;
	}
	
	public void setLightValue(float val) {
		this.lightValue = MathUtils.clip(0, 1, val);
	}
	
	public float getLightValue() {
		return this.lightValue;
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
		if (dimension == null)
			return 0; 
		return getX() + dimension.x/2;
	}
	
	public float centerY() {
		if (dimension == null)
			return 0;
		return getY() + dimension.y/2;
	}
	
	public Vector2 center() {
		return new Vector2(centerX(),centerY());
	}
	
	public float getWidth() {
		return dimension.x;
	}
	
	public float getHeight() {
		return dimension.y;
	}
	
	public void setVelocity(float vx, float vy) {
		setVelocity(vx,vy,this.velocity.r);
	}
	
	public void setVelocity(float vx, float vy, float vr) {
		this.velocity.x = vx;
		this.velocity.y = vy;
		this.velocity.r = vr;
	}
	
	public void setVelocity(Vector2 vel) {
		this.velocity = vel;
	}
	
	public void rotate(float radians) {
		move(0,0,radians);
	}
	
	public void setRotation(float radians) {
		this.moveR(radians-this.getRotation());
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
		this.move(this.velocity.x*dt,this.velocity.y*dt,this.velocity.r*dt);
	}
	
	/**
	 * Moves the entities position based on the delta x and delta y inputs
	 * @param dx distance to change x
	 * @param dy distance to change y
	 * @param dr amount of rotation about the midpoint of this object
	 */
	public void move(float dx, float dy, float dr) {
		moveX(dx);
		moveY(dy);
		moveR(dr);
	}
	
	/**
	 * How many intervals to move the object when checking for collision
	 * A higher number indicates more precise collision detection
	 * A lower number indicates less precise collision detection.
	 */
	private static final float COLLISION_CHECK_STEP = 0.01f,
							   COLLISION_ROTATION_CHECK_STEP = (float)Math.PI/300.0f;
	
	/**
	 * Moves the object along the x-axis using collision detection
	 * @param dx
	 */
	public void moveX(float dx) {
		if (dx == 0)
			return; //no movement
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE) {
			this.setX(this.getX() + dx);
			return;
		}
		float checkStep = COLLISION_CHECK_STEP*MathUtils.sign(dx);
		List<Line> walls = this.getRegion().getWalls().getWalls();
		Vector2 intersection;
		int iterations = (int)(dx/checkStep)+1;
		for (int i = 0; i < iterations; i++) {
			this.setX(getX()+checkStep);
			//now check for collision
			for (Line l : walls) {
				intersection = this.hitbox.intersecting(l);
				if (intersection != null) {
					this.setX(getX()-checkStep); //go back out of the collision zone
					this.velocity.x = 0; //no more movement in X direction now
					return;
				}
			}
		}
		float extraMovement = checkStep * iterations - dx;
		this.setX(getX() - extraMovement);
	}
	
	/**
	 * Moves the object along the y-axis using collision detection
	 * @param dy
	 */
	public void moveY(float dy) {
		if (dy == 0)
			return;
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE) {
			this.setY(this.getY() + dy);
			return;
		}
		float checkStep = COLLISION_CHECK_STEP*MathUtils.sign(dy);
		List<Line> walls = this.getRegion().getWalls().getWalls();
		Vector2 intersection;
		int iterations = (int)(dy/checkStep)+1;
		for (int i = 0; i < iterations; i++) {
			this.setY(getY()+checkStep);
			//now check for collision
			for (Line l : walls) {
				intersection = this.hitbox.intersecting(l);
				if (intersection != null) {
					//we done
					this.setY(getY()-checkStep); //go back out of the collision zone
					this.velocity.y = 0; //no more movement in y direction now
					return;
				}
			}
		}
		float extraMovement = checkStep * iterations - dy;
		this.setY(getY() - extraMovement);
	}
	
	/**
	 * Rotates the object around its center using collision detection
	 * @param dr
	 */
	public void moveR(float dr) {
		if (dr == 0)
			return;
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE) {
			this.position.r += dr;
			return;
		}
		float checkStep = COLLISION_ROTATION_CHECK_STEP*MathUtils.sign(dr);
		if (this.getRegion() == null) {
			this.position.r += dr;
			this.hitbox.rotate(dr);
			return;
		}
		List<Line> walls = this.getRegion().getWalls().getWalls();
		Vector2 intersection;
		int iterations = (int)(dr/checkStep);
		for (int i = 0; i < iterations; i++) {
			this.position.r += checkStep;
			this.hitbox.rotate(checkStep);
			//now check for collision
			for (Line l : walls) {
				intersection = this.hitbox.intersecting(l);
				if (intersection != null) {
					//we done
					this.position.r -= checkStep; //go back out of the collision zone
					this.hitbox.rotate(-checkStep);
					this.velocity.r = 0; //no more movement in X direction now
					return;
				}
			}
		}
		float extraMovement = iterations * checkStep - dr;
		this.position.r -= extraMovement;
		this.hitbox.rotate(-extraMovement);
	}
	
	public void correctOutOfWalls() {
		//check if we are intersecting a wall
		for (Line l : this.getRegion().getWalls().getWalls()) {
			Vector2 intersection = this.hitbox.intersecting(l);
			while (intersection != null) {
				//correct
				//get the angle to go out with
				float angle = l.angleTo(this.center());
				float dx = (float)Math.cos(angle) * 0.01f,
					  dy = (float)Math.sin(angle) * 0.01f; 
				this.setX(this.getX() + dx);
				this.setY(this.getY() + dy);
				intersection = this.hitbox.intersecting(l);
			}
		}
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
	}
	
	public void setX(float x) {
		setPosition(x,getY());
	}
	
	public void setY(float y) {
		setPosition(getX(),y);
	}
	
	/**
	 * Sets the width and height of the world object
	 * given a Vector object
	 * @param dimension
	 */
	public void setDimension(Vector2 dimension) {
		dimension.x = MathUtils.floor(0, dimension.x);
		dimension.y = MathUtils.floor(0, dimension.y);
		dimension.r = 0;
		this.dimension = dimension;
	}
	
	/**
	 * Sets the width and height of the world object
	 * given two floating point numbers.
	 * @param width
	 * @param height
	 */
	public void setDimension(float width, float height) {
		setDimension(new Vector2(width,height));
	}
	
	/**
	 * Sets the dimensional height of the object
	 * @param height
	 */
	public void setHeight(float height) {
		setDimension(new Vector2(getWidth(),height));
	}
	
	/**
	 * Sets the dimensional width of the object
	 * @param width
	 */
	public void setWidth(float width) {
		setDimension(new Vector2(width,getHeight()));
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
	
	private static final float OBJECT_COLLISION_CHECK_STEP = 0.1f;
	/**
	 * Uses Separate Axis Theorem for determining whether or
	 * not the two hitboxes are colliding. The actual method logic
	 * is used as Hitbox.this.satIntersecting(Hitbox other)
	 * @param other
	 * @return
	 */
	public boolean colliding(WorldObject other) {
		//save the position
		Vector2 constPos = this.position.copy();
		
		//get the last position the object was at
		Vector2 lastPosition = this.positionHistory.getPosition(1);
		if (lastPosition == null)
			return hitbox.satIntersecting(other.hitbox);
		
		//if we are already intersecting, then return true.. no need to check past
		if (hitbox.satIntersecting(other.hitbox))
			return true;
		
		//loop between the two positions
		float distance = MathUtils.distance(constPos, lastPosition);
		int checks = (int)(distance/OBJECT_COLLISION_CHECK_STEP);
		float dr = (constPos.r - lastPosition.r)/checks;
		float dx = (constPos.x - lastPosition.x)/checks;
		float dy = (constPos.y - lastPosition.y)/checks;
		this.setPosition(lastPosition);
		for (int i = 0; i < checks; i++) {
			this.setX(this.getX() + dx);
			this.setY(this.getY() + dy);
			this.setRotation(this.getRotation() + dr);
			if(hitbox.satIntersecting(other.hitbox)) {
				this.position = constPos;
				return true; //we found collision.. cut out now.
			}
		}
		
		//reset the position
		this.position = constPos;
	
		return false;
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
	
	public float squaredDistanceTo(WorldObject other) {
		if (other == null)
			return MathUtils.INFINITY;
		return MathUtils.squaredDistance(centerX(),centerY(),other.centerX(),other.centerY());
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public Properties.Value getProperty(Properties.Key key) {
		return properties == null ? null : properties.get(key);
	}
	
	public PositionHistory getPositionHistory() {
		return this.positionHistory;
	}
}