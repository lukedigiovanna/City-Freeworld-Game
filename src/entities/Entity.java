package entities;

import misc.Vector2;

public class Entity {
	private Vector2 position;
	
	public Entity(float x, float y) {
		this.position = new Vector2(x, y);
	}
}
