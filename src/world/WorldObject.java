package world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.player.Player;
import misc.Line;
import misc.MathUtils;
import misc.Vector2;
import world.event.*;
import world.regions.Cell;
import world.regions.Region;

/**
 * Super class for all objects that will be in the world : cells, entities, etc.
 *
 */
public abstract class WorldObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final float MIN_HEIGHT = 0, MAX_HEIGHT = 25;
	
	private PositionHistory positionHistory;
	private Region region;
	private Vector2 position, velocity, dimension;
	private float verticalHeight; //the z-axis of position.. follows same unit scale as the x and y axis
	private Hitbox hitbox; //identifies the physical boundaries with walls
	private float mass; //in kilograms
	private Properties properties;
	private float fieldOfView = 0;
	
	private float lightValue = 1.0f; //value from 0 - 1 that indicates light, 0 is pitch black, 1 is bright
	private float lightEmission = 0.0f; //value that indicates distance of light production
	
	private float age; //the number of real seconds the object has existed in the world
	
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
		
		this.mass = 1;
		
		this.objectCollisionEvents = new ArrayList<ObjectCollisionEvent>();
		
		this.collisionEvents = new ArrayList<CollisionEvent>();
		addCollisionEvent(CollisionEvent.STOP); //default collision event
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
	
	public void enableHitbox() {
		this.hitbox.enable();
	}
	
	public void disableHitbox() {
		this.hitbox.disable();
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getMass() {
		return this.mass;
	}
	
	// for calculations that deal with the angular physical world
	public float getMomentOfInertia() {
		// we will assume that all objects are rectangular in nature and 
		// rotate about their center
		// we can then deduce that the I = 1/12 * bh(b^2 + h^2) * M... 
		// where b = the width and h = the height of this object.
		return this.mass/12f * this.getWidth() * this.getHeight() * (this.getWidth() * this.getWidth() + this.getHeight() * this.getHeight());
	}
	
	/**
	 * Sets the height of the object
	 * Limits it between the minimum and maximum heights
	 * @param height
	 */
	public void setVerticalHeight(float height) {
		this.verticalHeight = MathUtils.clip(MIN_HEIGHT, MAX_HEIGHT, height);
	}
	
	public Hitbox getHitbox() {
		return this.hitbox;
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
		
		this.updateRigidLines();
		
		//update the position history..
		this.positionHistory.update(dt);
		
		//move with the current velocity
		this.move(dt);
		
		//collision correction
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_TRUE)
			this.correctOutOfWalls();
		
		//this.regenerateHitbox();
		
		updateLightValue();
		
		age += dt;
	}
	
	public void updateLightValue() {
		if (region == null || region.getWorld() == null)
			return;
		float globalValue = region.getLightLevel();
		float thisVal = globalValue;
		for (Entity e : this.getRegion().getEntities().get()) {
			if (e.getLightEmissionValue() > 0 && e.hasDirectPathTo(this)) {
				//make sure a wall is not in the way
				float d = this.squaredDistanceTo(e);
				//use the inverse square law to determine light intensity
				float light = MathUtils.ceil(1.0f, 1/d);
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
	
	public Cell getCellOn() {
		return this.region.getGrid().get((int)this.centerX(),(int)this.centerY());
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
	
	public static final float DEFAULT_FIELD_OF_VIEW = (float) (Math.PI - Math.PI/6.0f);
	
	/**
	 * Checks if there is a direct view from between the objects
	 * that is not obstructed by any rigid lines such as walls
	 * @param other
	 * @return
	 */
	public boolean hasDirectPathTo(WorldObject other) {
		if (other == null)
			return false;
		
		Line l = new Line(this.center(),other.center());
		//now check if it is intersecting a rigid line
		for (Line r : this.getRigidLines())
			if (l.intersects(r) != null) 
				return false;
		
		return true;
	}
	
	/**
	 * Checks if the other object is within the FOV of this object
	 * @param other
	 * @return
	 */
	public boolean canSee_IgnoreWalls(WorldObject other) {
		if (!this.hasFieldOfView())
			return true; //if no FOV, then we assume the object has a line of sight to everything on the region (360 deg FOV)
		
		Vector2[] vertices = this.hitbox.getVertices();
		float[] anglesToTest = new float[1 + vertices.length];
		
		anglesToTest[0] = (new Line(this.center(),other.center())).angle();
		//now add the other angles from the hitbox vertices
		int count = 1;
		for (Vector2 vertex : vertices) {
			anglesToTest[count++] = (new Line(this.center(),vertex)).angle();
		}
		
		float thisAngle = this.getRotation();
		
		//get limits of the field of view.
		float lowerLimit = thisAngle - this.fieldOfView/2,
			  upperLimit = thisAngle + this.fieldOfView/2;
		
		for (float sightAngle : anglesToTest) {
			//limit the value to be within [0,2pi)
			while (sightAngle < 0)
				sightAngle += Math.PI * 2;
			while (sightAngle >= Math.PI * 2)
				sightAngle -= Math.PI * 2;
			
			//check if the other object is within this objects field of view
			if (MathUtils.isAngleWithin(lowerLimit, upperLimit, sightAngle))
				return true;
		}
		
		return false; //if none of those test points worked.
	}
	
	/**
	 * Returns true if the object is within this objects
	 * field of view and the sight is unobstructed.
	 * @param other
	 * @return
	 */
	public boolean canSee(WorldObject other) {
		if (other == null)
			return false;
		
		boolean hasPathTo = this.hasDirectPathTo(other);
		
		if (!hasPathTo)
			return false;
		
		//now we check to make sure we are in the correct field of view
		return this.canSee_IgnoreWalls(other);
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
	
	/**
	 * Sets the rotation of the object accounting for collision
	 * Different from setR(), which sets the value no matter what.
	 * @param radians
	 */
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
		c.setColor(java.awt.Color.BLUE);
		c.fillOval(getX()-0.05f, getY()-0.05f, 0.1f, 0.1f);
		c.fillOval(centerX()-0.05f, centerY()-0.05f, 0.1f, 0.1f);
	}
	
	public void drawFieldOfView(Camera c) {
		if (!this.hasFieldOfView()) //only humans have a field of view
			return;
		
		c.setStrokeWidth(0.025f);
		c.setColor(java.awt.Color.BLUE);
		float length = 5.0f;
		float angle = this.getRotation() + this.fieldOfView/2;
		c.drawLine(this.centerX(),this.centerY(),this.centerX()+length*(float)Math.cos(angle),this.centerY()+length*(float)Math.sin(angle));
		angle = this.getRotation() - this.fieldOfView/2;
		c.drawLine(this.centerX(),this.centerY(),this.centerX()+length*(float)Math.cos(angle),this.centerY()+length*(float)Math.sin(angle));
	}
	
	public boolean hasFieldOfView() {
		return (this.fieldOfView > 0);
	}
	
	public void setFieldOfView(float fov) {
		this.fieldOfView = fov;
	}
	
	public float getFieldOfView() {
		return this.fieldOfView;
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
		Line hitX = moveX(dx);
		Line hitY = moveY(dy);
		Line hitR = moveR(dr);
		if (hitX != null)
			onCollisionWithWall(hitX);
		else if (hitY != null)
			onCollisionWithWall(hitY);
		else if (hitR != null)
			onCollisionWithWall(hitR);
	}
	
	private List<ObjectCollisionEvent> objectCollisionEvents;
	
	public void addObjectCollisionEvent(ObjectCollisionEvent event) {
		this.objectCollisionEvents.add(event);
	}
	
	public void removeObjectCollisionEvent(ObjectCollisionEvent event) {
		this.objectCollisionEvents.remove(event);
	}
	
	public void onObjectCollision(WorldObject other) {
		for (ObjectCollisionEvent event : this.objectCollisionEvents) {
			event.run(this, other);
		}
	}
	
	private List<CollisionEvent> collisionEvents;
	
	public void addCollisionEvent(CollisionEvent event) {
		this.collisionEvents.add(event);
	}
	
	public void removeCollisionEvent(CollisionEvent event) {
		this.collisionEvents.remove(event);
	}

	public void onCollisionWithWall(Line wall) {
		for (CollisionEvent event : this.collisionEvents) {
			event.run(this, wall);
		}
	}
	
	/**
	 * How many intervals to move the object when checking for collision
	 * A higher number indicates more precise collision detection
	 * A lower number indicates less precise collision detection.
	 */
	private static final float COLLISION_CHECK_STEP = 0.01f,
							   COLLISION_ROTATION_CHECK_STEP = (float)Math.PI/3.0f;
	
	/**
	 * Moves the object along the x-axis using collision detection
	 * @param dx
	 */
	public Line moveX(float dx) {
		if (dx == 0)
			return null; //no movement
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE) {
			this.setX(this.getX() + dx);
			return null;
		}
		float checkStep = COLLISION_CHECK_STEP*MathUtils.sign(dx);
		List<Line> walls = this.getRigidLines();
		Vector2 intersection;
		int iterations = (int)(dx/checkStep)+1;
		for (int i = 0; i < iterations; i++) {
			this.setX(getX()+checkStep);
			//now check for collision
			for (Line l : walls) {
				intersection = this.hitbox.intersecting(l);
				if (intersection != null) {
					this.setX(getX()-checkStep); //go back out of the collision zone
					return l;
				}
			}
		}
		float extraMovement = checkStep * iterations - dx;
		this.setX(getX() - extraMovement);
		return null;
	}
	
	/**
	 * Moves the object along the y-axis using collision detection
	 * @param dy
	 */
	public Line moveY(float dy) {
		if (dy == 0)
			return null;
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE) {
			this.setY(this.getY() + dy);
			return null;
		}
		float checkStep = COLLISION_CHECK_STEP*MathUtils.sign(dy);
		List<Line> walls = this.getRigidLines();
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
					return l;
				}
			}
		}
		float extraMovement = checkStep * iterations - dy;
		this.setY(getY() - extraMovement);
		return null;
	}
	
	/**
	 * Rotates the object around its center using collision detection
	 * Returns true if the object collided with a wall during movement
	 * @param dr
	 */
	public Line moveR(float dr) {
		if (dr == 0)
			return null;
		if (this.getProperty(Properties.KEY_HAS_COLLISION) == Properties.VALUE_HAS_COLLISION_FALSE || this.getRegion() == null) {
			this.setR(this.getRotation() + dr);
			return null;
		}
		float checkStep = COLLISION_ROTATION_CHECK_STEP*MathUtils.sign(dr);
		List<Line> walls = this.getRigidLines();
		Vector2 intersection;
		int iterations = (int)(dr/checkStep);
		for (int i = 0; i < iterations; i++) {
			this.setR(this.position.r + checkStep);
			//now check for collision
			for (Line l : walls) {
				intersection = this.hitbox.intersecting(l);
				if (intersection != null) {
					//we done
					//go back out of the collision zone
					this.setR(this.position.r - checkStep);
					return l;
				}
			}
		}
		float extraMovement = iterations * checkStep - dr;
		this.setR(this.position.r - extraMovement);
		return null;
	}
	
	public void correctOutOfWalls() {
		//check if we are intersecting a wall
		for (Line l : this.getRigidLines()) {
			Vector2 intersection = this.hitbox.intersecting(l);
			float angle = 0.0f;
			while (intersection != null) {
				//correct
				//get the angle to go out with
				angle = l.angleTo(this.center());
				float dx = (float)Math.cos(angle) * 0.01f,
					  dy = (float)Math.sin(angle) * 0.01f; 
				this.setX(this.getX() + dx);
				this.setY(this.getY() + dy);
				intersection = this.hitbox.intersecting(l);
			}
		}
	}
	
	private List<Line> rigidLines = new ArrayList<Line>();
	/**
	 * Updates the list of rigid lines for the object
	 */
	private void updateRigidLines() {
		rigidLines.clear();
		rigidLines.addAll(this.getRegion().getWalls().getWalls());
		//add in the lines from rigid entities.
		if (this.getProperty(Properties.KEY_HAS_RIGID_BODY) == Properties.VALUE_HAS_RIGID_BODY_TRUE)
			for (Entity e : this.getRegion().getEntities().get())
				if (e != this && e.getVerticalHeight() >= this.verticalHeight && e.getProperty(Properties.KEY_HAS_RIGID_BODY) == Properties.VALUE_HAS_RIGID_BODY_TRUE) 
					for (Line l : e.getHitbox().getLines()) 
						rigidLines.add(l);
	}
	
	public List<Line> getRigidLines() {
		return rigidLines;
	}
	
	/**
	 * Sets the position given two floats
	 * @param x x-coord
	 * @param y y-coord
	 */
	public void setPosition(float x, float y) {
		setPosition(new Vector2(x,y,position.r));
	}
	
	public void setPosition(float x, float y, float r) {
		setPosition(new Vector2(x,y,r));
	}
	
	/**
	 * Sets the current position from a vector
	 * ALL CHANGES TO POSITION SHOULD RUN THROUGH THIS METHOD
	 * for example: when adding velocity it should call setPosition(x+vx,y+vy)
	 * @param pos the vector
	 */
	public void setPosition(Vector2 pos) {
		Vector2 copy = this.position.copy();
		this.position.x = pos.x;
		this.position.y = pos.y;
		this.position.r = pos.r;
		hitbox.translate(pos.x-copy.x,pos.y-copy.y);
		hitbox.rotate(pos.r-copy.r);
	}
	
	public void setX(float x) {
		setPosition(x,getY());
	}
	
	public void setY(float y) {
		setPosition(getX(),y);
	}
	
	public void setR(float r) {
		setPosition(getX(),getY(),r);
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
				this.setPosition(constPos);
				return true; //we found collision.. cut out now.
			}
		}
		
		//reset the position
		this.setPosition(constPos);
	
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
	
	/**
	 * This method can optionally be implemented in subclasses
	 * that use animations or buffered images or other data types
	 * so that serialization can be done without serializing an image.
	 */
	public void loadAssets() {
		
	}
}