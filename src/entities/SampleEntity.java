package entities;

import java.awt.Graphics;

import misc.Color8;
import world.Camera;

public class SampleEntity extends Entity {

	public SampleEntity(float x, float y) {
		super(x, y, 1.0f, 1.0f);
	}

	@Override
	public void draw(Camera c) {
		c.setColor(Color8.BLUE);
		c.fillRect(getX(), getY(), 1.0f, 1.0f);
	}
	
	@Override
	public void update(float dt) {
		this.hitbox.rotate((float)(Math.PI*2*dt));
	}
}
