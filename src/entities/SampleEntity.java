package entities;

import java.awt.Color;
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
	private int i = 0;
	@Override
	public void draw(Camera c) {
		Color col = new Color(i,255-i,0);
		c.setColor(col);
		c.setStrokeWidth(0.025f);
		c.drawLine(getX(), getY()+getHeight()/2, getX()+getWidth(), getY()+getHeight()/2);
		c.drawLine(getX()+getWidth(), getY()+getHeight()/2, getX()+getWidth()-getWidth()/4, getY()+getHeight()/4);
		c.drawLine(getX()+getWidth(), getY()+getHeight()/2, getX()+getWidth()-getWidth()/4, getY()+getHeight()/2+getHeight()/4);
	}
	
	@Override
	public void update(float dt) {
		//this.velocity = new Vector2((float)Math.cos(age),(float)Math.sin(age));
		Entity player = getPlayer();
		if (player != null)
			this.position.r = (float)MathUtils.getAngle(centerX(), centerY(), player.centerX(), player.centerY());
		float dist = this.distanceTo(player);
		float perc = dist/7.0f;
		i = MathUtils.clip(0, 255, (int)(perc*255));
	}
}
