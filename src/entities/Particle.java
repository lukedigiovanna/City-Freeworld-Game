package entities;

import java.awt.*;

import world.*;

public class Particle extends Entity {
	private Type type;
	
	private float lifeSpan = 5.0f;
	
	public Particle(Type type, float x, float y) {
		super(x-type.width/2, y-type.height/2, type.width, type.height);
		this.type = type;
		i = (i+1)%colors.length;
		this.color = colors[i];
		properties.set(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_TRUE);
		properties.set(Properties.KEY_REGENERATE_HITBOX, Properties.VALUE_REGENERATE_HITBOX_FALSE);
	}
	
	public static enum Type {
		BALL(0.15f,0.15f);
		
		float width, height;
		Type(float width, float height) {
			this.width = width;
			this.height = height;
		}
	}
	
	private static int i = 0;
	private static Color[] colors = {Color.YELLOW,Color.GREEN,Color.BLUE,Color.RED};
	
	private Color color;
	
	@Override
	public void draw(Camera c) {
		c.setColor(color);
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
