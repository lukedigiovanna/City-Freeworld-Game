package entities;

import java.awt.Color;

import world.Camera;

public class Particle extends Entity {
	private Type type;
	
	private float lifeSpan = 2.5f;
	
	public Particle(Type type, float x, float y) {
		super(x-type.width/2, y-type.height/2, type.width, type.height);
		this.type = type;
	}
	
	public static enum Type {
		BALL;
		
		Color color;
		float width, height;
		Type() {
			width = 0.15f;
			height = 0.15f;
		}
	}
	
	@Override
	public void draw(Camera c) {
		c.setColor(Color.WHITE);
		switch (this.type) {
		case BALL:
			c.fillOval(getX(), getY(), getWidth(), getHeight());
			break;
		}
	}

	@Override
	public void update(float dt) {
		switch (type) {
		case BALL:
			velocity.y = -1;
			break;
		}
		if (this.age >= lifeSpan)
			this.destroy();
	}
}
