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
		properties.set(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
	}
	
	public static enum Type {
		BALL;
		
		float width, height;
		Type() {
			width = 0.15f;
			height = 0.15f;
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
			//velocity.y = -1;
			break;
		}
		if (this.age >= lifeSpan)
			this.destroy();
	}
}
