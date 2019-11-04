package entities;

import java.awt.*;

import misc.Vector2;
import world.Camera;

public abstract class Entity {
	private Vector2 position;
	private Hitbox hitbox;
	
	public Entity(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		int vertices = 4;
		float[] model = new float[vertices*2];
		float radius = 0.5f;
		for (int i = 0; i < vertices; i++) {
			float vx = (float)Math.cos((double)i/vertices * Math.PI * 2)*radius+radius,
				  vy = (float)Math.sin((double)i/vertices * Math.PI * 2)*radius+radius;
			model[i*2] = vx;
			model[i*2+1] = vy;
			System.out.println(vx+", "+vy);
		}
		this.hitbox = new Hitbox(this, model);
	}
	
	/**
	 * Moves the entities position based on the delta x and delta y inputes
	 * @param dx distance to change x
	 * @param dy distance to change y
	 */
	public void move(float dx, float dy) {
		position.add(new Vector2(dx,dy));
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	/**
	 * Checks if the hitboxes are colliding with each other
	 * @param other
	 * @return
	 */
	public boolean colliding(Entity other) {
		return this.hitbox.intersecting(other.hitbox);
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
	
	/**
	 * Draws the hitbox of the entity onto a camera
	 * @param c The camera object that will draw it
	 */
	public void drawHitbox(Camera c) {
		c.setColor(Color.RED);
		
		float[] model = hitbox.model;
		for (int i = 0; i < model.length; i+=2) {
			int next = (i + 2)%model.length;
			c.drawLine(this.getX()+model[i], this.getY()+model[i+1], this.getX()+model[next], this.getY()+model[next+1]);
		}
	}
}
