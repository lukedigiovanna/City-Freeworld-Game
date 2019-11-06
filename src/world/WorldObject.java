package world;

import entities.Entity;
import misc.Vector2;

/**
 * Super class for all objects that will be in the world : cells, entities, etc.
 *
 */
public abstract class WorldObject {
	protected Vector2 position;
	protected Hitbox hitbox;
	protected float rotation = 0.0f;
	
	public WorldObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y);
		float[] model = {0.0f,0.0f,width,0.0f,width,height,0.0f,height};
		this.hitbox = new Hitbox(this, model);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public void rotate(float radians) {
		hitbox.rotate(radians);
		rotation += radians;
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
		setPosition(new Vector2(x,y));
		
	}
	
	/**
	 * Sets the current position from a vector
	 * ALL CHANGES TO POSITION SHOULD RUN THROUGH THIS METHOD
	 * for example: when adding velocity it should call setPosition(x+vx,y+vy)
	 * @param pos the vector
	 */
	public void setPosition(Vector2 pos) {
		this.position = pos;
		//check for collision with the grid
		//lets look at the cells that the entity is overlapping with..
		//we will be using the hitbox of the entity to check for collision
		hitbox.updatePosition();
	}
	
	/**
	 * Checks if the hitboxes are colliding with each other
	 * @param other
	 * @return
	 */
	public boolean colliding(WorldObject other) {
		return this.hitbox.intersecting(other.hitbox);
	}
}
