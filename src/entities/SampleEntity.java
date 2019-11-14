package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import misc.Color8;
import misc.ImageTools;
import misc.MathUtils;
import misc.Vector2;
import world.Camera;

public class SampleEntity extends Entity {

	public SampleEntity(float x, float y) {
		super(x, y, 1.0f, 1.0f);
	}

	private BufferedImage arrow = ImageTools.getBufferedImage("arrow.png");
	@Override
	public void draw(Camera c) {
		c.drawImage(arrow, getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void update(float dt) {
		//this.velocity = new Vector2((float)Math.cos(age),(float)Math.sin(age));
		Entity look = this.getRegion().getWorld().getPlayer();
		if (look != null)
			this.position.r = (float)MathUtils.getAngle(centerX(), centerY(), look.centerX(), look.centerY());
	}
}
