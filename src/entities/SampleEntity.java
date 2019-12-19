package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.player.Player;
import misc.Color8;
import misc.ImageTools;
import misc.MathUtils;
import misc.Vector2;
import world.Camera;
import world.Properties;

public class SampleEntity extends Entity {

	public SampleEntity(float x, float y) {
		super(x, y, 1.0f, 1.0f);
		this.properties.set(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
	}

	private int i = 0;
	@Override
	public void draw(Camera c) {
		Color col = new Color(i,255-i,0);
		c.setColor(col);
		c.setStrokeWidth(0.075f);
		c.drawLine(getX(), getY()+getHeight()/2, getX()+getWidth(), getY()+getHeight()/2);
		c.drawLine(getX()+getWidth(), getY()+getHeight()/2, getX()+getWidth()-getWidth()/4, getY()+getHeight()/4);
		c.drawLine(getX()+getWidth(), getY()+getHeight()/2, getX()+getWidth()-getWidth()/4, getY()+getHeight()/2+getHeight()/4);
	}
	
	@Override
	public void update(float dt) {
		//this.velocity = new Vector2((float)Math.cos(age),(float)Math.sin(age));
		List<Player> players = getPlayers();
		for (Entity player : players) {
			if (player == null)
				continue;
			this.setRotation((float)MathUtils.getAngle(centerX(), centerY(), player.centerX(), player.centerY()));
			float dist = this.distanceTo(player);
			float perc = dist/7.0f;
			i = MathUtils.clip(0, 255, (int)(perc*255));
		}
	}
}
