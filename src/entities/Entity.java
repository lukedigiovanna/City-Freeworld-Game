package entities;

import java.awt.Graphics;

import misc.Vector2;

public abstract class Entity {
	private Vector2 position;
	private Hitbox hitbox;
	
	public Entity(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.hitbox = new Hitbox(this, width, height);
	}
	
	public void move(float dx, float dy) {
		
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public abstract void draw(Graphics g);
}
