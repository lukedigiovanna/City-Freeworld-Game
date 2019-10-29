package entities;

import java.awt.Graphics;

import misc.Vector2;
import world.Camera;

public abstract class Entity {
	private Vector2 position;
	private Hitbox hitbox;
	
	public Entity(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		float[] model = {0.0f, 0.0f, width, 0.0f, width, height, 0.0f, height};
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
	 * Called by the draw method from a camera class
	 * Assumes 0, 0 is the top left
	 * @param g
	 */
	public abstract void draw(Graphics g, Camera camera);
	
	public void drawHitbox(Graphics g, Camera camera) {
		
	}

}
