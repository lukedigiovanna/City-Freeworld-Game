package entities;

import java.awt.Graphics;

import world.Camera;

public class SampleEntity extends Entity {

	public SampleEntity(float x, float y) {
		super(x, y, 1.0f, 1.0f);
	}

	@Override
	public void draw(Graphics g, Camera camera) {
//		g.setColor(Color.RED);
//		g.fillRect(0, 0, 1, 1);
	}
}
