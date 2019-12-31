package entities.npcs;

import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Health;
import entities.Path;
import misc.ImageTools;
import world.Camera;

public class NPC extends Entity {
	
	public NPC(float x, float y) {
		super(x,y,11.0f/16,12.0f/16);
		this.health = new Health(20,20);
		addTag("NPC");
	}
	
	private BufferedImage image = ImageTools.getImage("assets/images/characters/character_1/idle_0.png");

	@Override
	public void draw(Camera camera) {
		camera.drawImage(image, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void update(float dt) {
		if (Math.random() < dt) {
			Path p = new Path();
		}
	}
}
