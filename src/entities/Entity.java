package entities;

import java.awt.*;

import misc.Vector2;
import world.Camera;

public abstract class Entity {
	private Vector2 position;
	private Hitbox hitbox;
	
	public Entity(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		float[] model = {0.0f, 0.0f, 1.0f, -0.3f, 0.4f,1.2f};
		this.hitbox = new Hitbox(this, model);
	}
	
	public void move(float dx, float dy) {
		
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
	public abstract void draw(Graphics g, Camera camera);
	
	public void drawHitbox(Camera c) {
		c.setColor(Color.RED);
		
		float[] model = hitbox.model;
		for (int i = 0; i < model.length; i+=2) {
			int next = (i + 2)%model.length;
			c.drawLine(this.getX()+model[i], this.getY()+model[i+1], this.getX()+model[next], this.getY()+model[next+1]);
		}
	}
}
