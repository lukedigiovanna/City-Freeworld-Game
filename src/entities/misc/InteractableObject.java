package entities.misc;

import java.awt.Color;

import entities.Entity;
import shops.Shop;
import world.Camera;

/*
 * Allows the player to interact with this object
 * Opens a UI depending on the type
 */
public class InteractableObject extends Entity {	
	
	private Shop.Type type;
	public InteractableObject(Shop.Type type, float x, float y) {
		super(x,y,1.0f,1.0f);
		this.type = type;
	}
	
	public void update(float dt) {
		
	}
	
	public void draw(Camera c) {
		c.setColor(new Color(250,250,250,150));
		c.fillOval(getX(), getY(), getWidth(), getHeight());
	}
}
